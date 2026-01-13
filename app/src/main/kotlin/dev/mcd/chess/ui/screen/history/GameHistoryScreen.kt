package dev.mcd.chess.ui.screen.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import dev.mcd.chess.R
import dev.mcd.chess.feature.auth.domain.UserRole
import dev.mcd.chess.feature.history.domain.GameResult
import dev.mcd.chess.feature.history.domain.SavedGame
import dev.mcd.chess.ui.screen.history.GameHistoryViewModel.State
import org.orbitmvi.orbit.compose.collectAsState
import java.text.DateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameHistoryScreen(
    viewModel: GameHistoryViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit,
) {
    val state by viewModel.collectAsState()
    val clipboardManager = LocalClipboardManager.current
    val selectedGame = remember { mutableStateOf<SavedGame?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(R.string.game_history)) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = 24.dp, vertical = 16.dp)
                .fillMaxSize(),
        ) {
            when (val current = state) {
                is State.Loading -> Unit
                is State.Content -> {
                    val title = if (current.user.role == UserRole.ADMIN) {
                        stringResource(R.string.all_games)
                    } else {
                        stringResource(R.string.my_games)
                    }
                    Text(text = title, style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(12.dp))
                    if (current.games.isEmpty()) {
                        Text(text = stringResource(R.string.no_games_yet))
                    } else {
                        GameList(
                            games = current.games,
                            showUsername = current.user.role == UserRole.ADMIN,
                            onGameSelected = { selectedGame.value = it },
                        )
                    }
                }
            }
        }
    }

    selectedGame.value?.let { game ->
        AlertDialog(
            onDismissRequest = { selectedGame.value = null },
            title = { Text(text = stringResource(R.string.game_moves_title)) },
            text = {
                SelectionContainer {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        Text(text = game.pgn, style = MaterialTheme.typography.bodySmall)
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { clipboardManager.setText(AnnotatedString(game.pgn)) },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ContentCopy,
                        contentDescription = stringResource(R.string.copy_pgn),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = stringResource(R.string.copy_pgn))
                }
            },
            dismissButton = {
                TextButton(onClick = { selectedGame.value = null }) {
                    Text(text = stringResource(R.string.dismiss))
                }
            },
        )
    }
}

@Composable
private fun GameList(
    games: List<SavedGame>,
    showUsername: Boolean,
    onGameSelected: (SavedGame) -> Unit,
) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        items(games, key = { it.id }) { game ->
            GameCard(
                game = game,
                showUsername = showUsername,
                onClick = { onGameSelected(game) },
            )
        }
    }
}

@Composable
private fun GameCard(
    game: SavedGame,
    showUsername: Boolean,
    onClick: () -> Unit,
) {
    val date = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.SHORT)
        .format(Date(game.createdAt))
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = gameResultLabel(game.result),
                style = MaterialTheme.typography.titleSmall,
            )
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall,
            )
            if (showUsername) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = game.username,
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }
    }
}

@Composable
private fun gameResultLabel(result: GameResult): String {
    return when (result) {
        GameResult.DRAW -> stringResource(R.string.game_result_draw)
        GameResult.WHITE_WIN_CHECKMATE -> stringResource(R.string.game_result_white_checkmate)
        GameResult.BLACK_WIN_CHECKMATE -> stringResource(R.string.game_result_black_checkmate)
        GameResult.WHITE_RESIGNED -> stringResource(R.string.game_result_white_resigned)
        GameResult.BLACK_RESIGNED -> stringResource(R.string.game_result_black_resigned)
        GameResult.UNKNOWN -> stringResource(R.string.game_result_unknown)
    }
}
