package dev.mcd.chess.ui.settings

import android.content.Intent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.mcd.chess.R

@Composable
fun DebugSettings(
    modifier: Modifier = Modifier,
    onLogout: () -> Unit,
) {
    val context = LocalContext.current

    val restart = {
        val intent = context.packageManager.getLaunchIntentForPackage(context.packageName)!!
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    Button(
        modifier = modifier,
        onClick = onLogout,
    ) {
        Text(text = stringResource(R.string.logout))
    }
    Spacer(modifier = Modifier.height(16.dp))
    LazyRow(
        Modifier
            .padding(24.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        item {
            TextButton(
                onClick = {
                    restart()
                },
            ) {
                Text(text = stringResource(R.string.restart))
            }
        }
    }
}
