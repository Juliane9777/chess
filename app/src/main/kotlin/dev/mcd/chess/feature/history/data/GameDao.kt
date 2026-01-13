package dev.mcd.chess.feature.history.data

import androidx.room.Dao
import androidx.room.Embedded
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

data class GameWithUserEntity(
    @Embedded
    val game: GameEntity,
    val username: String,
)

@Dao
interface GameDao {
    @Query(
        """
        SELECT games.*, users.username AS username
        FROM games
        INNER JOIN users ON games.user_id = users.id
        WHERE games.user_id = :userId
        ORDER BY games.created_at DESC
        """,
    )
    fun observeGamesForUser(userId: String): Flow<List<GameWithUserEntity>>

    @Query(
        """
        SELECT games.*, users.username AS username
        FROM games
        INNER JOIN users ON games.user_id = users.id
        ORDER BY games.created_at DESC
        """,
    )
    fun observeAllGames(): Flow<List<GameWithUserEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: GameEntity)
}
