package dev.mcd.chess.ui.screen.choosemode

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Extension
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.PersonSearch
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.mcd.chess.R

@Composable
fun ChooseModeScreen(
    onPlayOffline: () -> Unit,
    onPlayBot: () -> Unit,
    onSolvePuzzle: () -> Unit,
    onNavigateSettings: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = stringResource(id = R.string.app_name)) },
                actions = {
                    IconButton(onClick = { onNavigateSettings() }) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Rounded.Settings),
                            contentDescription = stringResource(R.string.settings),
                        )
                    }
                },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(1f)
                    .padding(24.dp),
            ) {
                PlayOfflineButton(onClick = onPlayOffline)
                Spacer(modifier = Modifier.height(24.dp))
                PlayComputerButton(onClick = onPlayBot)
                Spacer(modifier = Modifier.height(24.dp))
                SolvePuzzleButton(onClick = onSolvePuzzle)
            }
        }
    }
}

@Composable
private fun PlayComputerButton(
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(42.dp),
                painter = rememberVectorPainter(image = Icons.Rounded.Memory),
                contentDescription = stringResource(R.string.play_computer),
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = stringResource(id = R.string.play_computer),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun SolvePuzzleButton(
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(42.dp),
                painter = rememberVectorPainter(image = Icons.Rounded.Extension),
                contentDescription = stringResource(R.string.solve_puzzle),
            )
            Spacer(modifier = Modifier.width(24.dp))
            Text(
                text = stringResource(id = R.string.solve_puzzle),
                style = MaterialTheme.typography.titleMedium,
            )
        }
    }
}

@Composable
private fun PlayOfflineButton(
    onClick: () -> Unit,
) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() }
                .padding(vertical = 16.dp)
                .padding(start = 16.dp, end = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(42.dp),
                painter = rememberVectorPainter(image = Icons.Rounded.PersonSearch),
                contentDescription = stringResource(R.string.play_offline),
            )
            Spacer(modifier = Modifier.width(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.play_offline),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }
    }
}
