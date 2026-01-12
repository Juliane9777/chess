package dev.mcd.chess.ui.screen.offlinegame

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mcd.chess.R
import dev.mcd.chess.ui.game.GameTermination
import dev.mcd.chess.ui.game.GameView
import dev.mcd.chess.ui.game.board.interaction.GameSettings
import dev.mcd.chess.ui.screen.offlinegame.OfflineGameViewModel.SideEffect.AnnounceTermination
import dev.mcd.chess.ui.screen.offlinegame.OfflineGameViewModel.SideEffect.NotifyGameCopied
import dev.mcd.chess.ui.screen.offlinegame.OfflineGameViewModel.State.Game
import dev.mcd.chess.ui.screen.offlinegame.OfflineGameViewModel.State.Loading
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Composable
fun OfflineGameScreen(
    viewModel: OfflineGameViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(viewModel::onCopyPGN) {
                        Icon(
                            imageVector = Icons.Rounded.ContentCopy,
                            contentDescription = stringResource(R.string.copy_pgn),
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            val state by viewModel.collectAsState()
            val context = LocalContext.current

            var showTermination by remember { mutableStateOf<AnnounceTermination?>(null) }

            BackHandler {
                navigateBack()
            }

            viewModel.collectSideEffect { effect ->
                when (effect) {
                    is AnnounceTermination -> showTermination = effect
                    is NotifyGameCopied -> Toast.makeText(context, R.string.game_copied, Toast.LENGTH_SHORT).show()
                }
            }

            when (val s = state) {
                is Game -> {
                    GameView(
                        gameHolder = s.gameHolder,
                        onMove = viewModel::onPlayerMove,
                        settings = GameSettings(allowResign = false, allowBothSides = true),
                    )
                }
                is Loading -> Unit
            }

            showTermination?.let { (sideMated, draw, resignation) ->
                GameTermination(
                    sideMated = sideMated,
                    draw = draw,
                    resignation = resignation,
                    onRestart = { viewModel.onRestart() },
                    onDismiss = { showTermination = null },
                )
            }
        }
    }
}
