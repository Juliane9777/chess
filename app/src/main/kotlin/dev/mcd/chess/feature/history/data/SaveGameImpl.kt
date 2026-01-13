package dev.mcd.chess.feature.history.data

import dev.mcd.chess.common.game.GameSession
import dev.mcd.chess.feature.history.domain.GameHistoryRepository
import dev.mcd.chess.feature.history.domain.GameMode
import dev.mcd.chess.feature.history.domain.GameResult
import dev.mcd.chess.feature.history.domain.SaveGame
import dev.mcd.chess.feature.history.domain.SavedGame
import dev.mcd.chess.feature.share.domain.GeneratePGN
import java.util.UUID
import javax.inject.Inject

class SaveGameImpl @Inject constructor(
    private val gameHistoryRepository: GameHistoryRepository,
    private val generatePGN: GeneratePGN,
) : SaveGame {
    override suspend fun invoke(
        session: GameSession,
        userId: String,
        username: String,
        mode: GameMode,
        result: GameResult,
    ) {
        val pgn = generatePGN(session)
        val game = SavedGame(
            id = UUID.randomUUID().toString(),
            userId = userId,
            username = username,
            createdAt = System.currentTimeMillis(),
            mode = mode,
            result = result,
            pgn = pgn,
        )
        gameHistoryRepository.saveGame(game)
    }
}
