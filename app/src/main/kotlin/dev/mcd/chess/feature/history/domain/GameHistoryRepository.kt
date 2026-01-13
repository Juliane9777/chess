package dev.mcd.chess.feature.history.domain

import kotlinx.coroutines.flow.Flow

interface GameHistoryRepository {
    fun observeGamesForUser(userId: String): Flow<List<SavedGame>>
    fun observeAllGames(): Flow<List<SavedGame>>
    suspend fun saveGame(game: SavedGame)
}
