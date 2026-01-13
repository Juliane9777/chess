package dev.mcd.chess.feature.history.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "games")
data class GameEntity(
    @PrimaryKey
    val id: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long,
    val mode: String,
    val result: String,
    val pgn: String,
)
