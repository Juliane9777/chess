package dev.mcd.chess.feature.history.domain

import dev.mcd.chess.common.game.GameSession

interface SaveGame {
    suspend operator fun invoke(
        session: GameSession,
        userId: String,
        username: String,
        mode: GameMode,
        result: GameResult,
    )
}
