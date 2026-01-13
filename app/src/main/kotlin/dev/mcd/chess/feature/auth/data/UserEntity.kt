package dev.mcd.chess.feature.auth.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    val id: String,
    val username: String,
    @ColumnInfo(name = "password_hash")
    val passwordHash: String,
    val role: String,
)
