package dev.mcd.chess.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dev.mcd.chess.ui.puzzle.PuzzleScreen
import dev.mcd.chess.ui.screen.botgame.BotGameScreen
import dev.mcd.chess.ui.screen.botselection.BotSelectionScreen
import dev.mcd.chess.ui.screen.choosemode.ChooseModeScreen
import dev.mcd.chess.ui.screen.history.GameHistoryScreen
import dev.mcd.chess.ui.screen.login.LoginScreen
import dev.mcd.chess.ui.screen.onlinegame.OnlineGameScreen
import dev.mcd.chess.ui.screen.settings.SettingsScreen
import dev.mcd.chess.ui.screen.offlinegame.OfflineGameScreen

@Composable
fun Routing() {
    val navController = rememberNavController()
    NavHost(navController, "/login") {
        composable("/login") {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate("/choosemode") {
                        popUpTo("/login") { inclusive = true }
                    }
                },
            )
        }
        composable("/choosemode") {
            ChooseModeScreen(
                //onPlayOnline = { navController.navigate("/game/online") },
                onPlayOffline = { navController.navigate("/game/offline") },
                onPlayBot = { navController.navigate("/selectbot") },
                onSolvePuzzle = { navController.navigate("/puzzle") },
                onViewHistory = { navController.navigate("/history") },
                onNavigateSettings = { navController.navigate("/settings") },
                //onNavigateExistingGame = { navController.navigate("/game/online?gameId=$it") },
            )
        }
        composable("/selectbot") {
            BotSelectionScreen(
                onBotSelected = { bot, side -> navController.navigate("/game/bot/$bot/$side") },
                onDismiss = { navController.popBackStack() },
            )
        }
        composable(
            "/game/bot/{bot}/{side}",
            listOf(
                navArgument("bot") { type = NavType.StringType },
                navArgument("side") { type = NavType.StringType },
            ),
        ) {
            BotGameScreen {
                navController.popBackStack()
            }
        }
        composable(
            "/game/online?gameId={gameId}",
            listOf(
                navArgument("gameId") {
                    type = NavType.StringType
                    nullable = true
                },
            ),
        ) {
            OnlineGameScreen {
                navController.popBackStack()
            }
        }
        composable("/game/offline") {
            OfflineGameScreen {
                navController.popBackStack()
            }
        }
        composable("/puzzle") {
            PuzzleScreen()
        }
        composable("/settings") {
            SettingsScreen(
                onDismiss = { navController.popBackStack() },
                onLogout = {
                    navController.navigate("/login") {
                        popUpTo("/login") { inclusive = true }
                    }
                },
            )
        }
        composable("/history") {
            GameHistoryScreen {
                navController.popBackStack()
            }
        }
    }
}
