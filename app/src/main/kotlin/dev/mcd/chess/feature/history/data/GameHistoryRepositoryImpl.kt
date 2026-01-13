package dev.mcd.chess.feature.history.data

import dev.mcd.chess.feature.history.domain.GameHistoryRepository
import dev.mcd.chess.feature.history.domain.GameMode
import dev.mcd.chess.feature.history.domain.GameResult
import dev.mcd.chess.feature.history.domain.SavedGame
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GameHistoryRepositoryImpl @Inject constructor(
    private val gameDao: GameDao,
) : GameHistoryRepository {
    override fun observeGamesForUser(userId: String): Flow<List<SavedGame>> {
        return gameDao.observeGamesForUser(userId).map { games ->
            games.map { it.toDomain() }
        }
    }

    override fun observeAllGames(): Flow<List<SavedGame>> {
        return gameDao.observeAllGames().map { games ->
            games.map { it.toDomain() }
        }
    }

    override suspend fun saveGame(game: SavedGame) {
        gameDao.insert(game.toEntity())
    }
}

private fun GameWithUserEntity.toDomain(): SavedGame {
    return SavedGame(
        id = game.id,
        userId = game.userId,
        username = username,
        createdAt = game.createdAt,
        mode = GameMode.valueOf(game.mode),
        result = GameResult.valueOf(game.result),
        pgn = game.pgn,
    )
}

private fun SavedGame.toEntity(): GameEntity {
    return GameEntity(
        id = id,
        userId = userId,
        createdAt = createdAt,
        mode = mode.name,
        result = result.name,
        pgn = pgn,
    )
}
