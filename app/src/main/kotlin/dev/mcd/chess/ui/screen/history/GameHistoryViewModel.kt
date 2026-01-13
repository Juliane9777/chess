package dev.mcd.chess.ui.screen.history

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mcd.chess.feature.auth.domain.AuthenticatedUser
import dev.mcd.chess.feature.auth.domain.SessionManager
import dev.mcd.chess.feature.auth.domain.UserRole
import dev.mcd.chess.feature.history.domain.GameHistoryRepository
import dev.mcd.chess.feature.history.domain.SavedGame
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class GameHistoryViewModel @Inject constructor(
    sessionManager: SessionManager,
    gameHistoryRepository: GameHistoryRepository,
) : ViewModel(), ContainerHost<GameHistoryViewModel.State, Nothing> {

    private val userFlow = sessionManager.currentUser.filterNotNull()

    override val container = container<State, Nothing>(State.Loading) {
        viewModelScope.launch {
            userFlow.flatMapLatest { user ->
                val gamesFlow = if (user.role == UserRole.ADMIN) {
                    gameHistoryRepository.observeAllGames()
                } else {
                    gameHistoryRepository.observeGamesForUser(user.id)
                }
                gamesFlow.map { games -> State.Content(user = user, games = games) }
            }.collect { state ->
                intent {
                    reduce { state }
                }
            }
        }
    }

    sealed interface State {
        object Loading : State

        @Stable
        data class Content(
            val user: AuthenticatedUser,
            val games: List<SavedGame>,
        ) : State
    }
}
