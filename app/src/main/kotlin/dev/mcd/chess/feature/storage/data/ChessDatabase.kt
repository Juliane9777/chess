package dev.mcd.chess.feature.storage.data

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.mcd.chess.feature.auth.data.UserDao
import dev.mcd.chess.feature.auth.data.UserEntity
import dev.mcd.chess.feature.history.data.GameDao
import dev.mcd.chess.feature.history.data.GameEntity

@Database(
    entities = [
        UserEntity::class,
        GameEntity::class,
    ],
    version = 1,
)
abstract class ChessDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun gameDao(): GameDao
}
