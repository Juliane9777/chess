package dev.mcd.chess.ui.screen.offlinegame

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.bhlangonijr.chesslib.Side
import com.github.bhlangonijr.chesslib.move.Move
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mcd.chess.common.game.GameSession
import dev.mcd.chess.common.game.MoveResult
import dev.mcd.chess.common.game.TerminationReason
import dev.mcd.chess.feature.common.domain.AppPreferences
import dev.mcd.chess.feature.game.domain.GameSessionRepository
import dev.mcd.chess.feature.game.domain.usecase.StartOfflineGame
import dev.mcd.chess.feature.share.domain.CopySessionPGNToClipboard
import dev.mcd.chess.feature.sound.domain.GameSessionSoundWrapper
import dev.mcd.chess.feature.sound.domain.SoundSettings
import dev.mcd.chess.ui.compose.StableHolder
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OfflineGameViewModel @Inject constructor(
    private val gameSessionRepository: GameSessionRepository,
    private val startOfflineGame: StartOfflineGame,
    private val soundWrapper: GameSessionSoundWrapper,
    private val appPreferences: AppPreferences,
    private val copyPGN: CopySessionPGNToClipboard,
) : ViewModel(), ContainerHost<OfflineGameViewModel.State, OfflineGameViewModel.SideEffect> {

    override val container = container<State, SideEffect>(State.Loading) {
        viewModelScope.launch {
            gameSessionRepository.activeGame()
                .filterNotNull()
                //.filter { it.termination() == null }
                .collectLatest { game ->
                    intent {
                        reduce {
                            State.Game(StableHolder(game))
                        }
                    }
                    intent {
                        handleTermination(game.awaitTermination())
                    }
                    intent {
                        val soundSettings = SoundSettings(
                            enableNotify = true,
                            enabled = appPreferences.soundsEnabled(),
                        )
                        soundWrapper.attachSession(game, soundSettings)
                    }
                }
        }
        startGame()
    }

    fun onRestart() {
        startGame()
    }

    fun onCopyPGN() {
        intent {
            runCatching {
                val session = gameSessionRepository.activeGame().value ?: return@intent
                copyPGN(session)
                postSideEffect(SideEffect.NotifyGameCopied)
            }.onFailure {
                Timber.e(it, "copying PGN")
            }
        }
    }

    fun onPlayerMove(move: Move) {
        intent {
            gameSessionRepository.activeGame().value?.run {
                if (move(move.toString()) != MoveResult.Moved) {
                    Timber.e("Illegal Move: $move")
                }
            }
        }
    }



    private fun startGame() {
        intent {
            startOfflineGame()
        }
    }

    private fun handleTermination(reason: TerminationReason) {
        intent {
            postSideEffect(
                SideEffect.AnnounceTermination(
                    sideMated = reason.sideMated,
                    draw = reason.draw,
                    resignation = reason.resignation,
                ),
            )
        }
    }

    sealed interface State {
        object Loading : State

        data class Game(
            val gameHolder: StableHolder<GameSession>,
        ) : State
    }

    sealed interface SideEffect {
        @Stable
        data class AnnounceTermination(
            val sideMated: Side? = null,
            val draw: Boolean = false,
            val resignation: Side? = null,
        ) : SideEffect

        object NotifyGameCopied : SideEffect
    }
}
