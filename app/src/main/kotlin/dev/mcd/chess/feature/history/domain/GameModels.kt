package dev.mcd.chess.feature.history.domain

import com.github.bhlangonijr.chesslib.Side
import dev.mcd.chess.common.game.TerminationReason

enum class GameMode {
    OFFLINE,
}

enum class GameResult {
    WHITE_WIN_CHECKMATE,
    BLACK_WIN_CHECKMATE,
    DRAW,
    WHITE_RESIGNED,
    BLACK_RESIGNED,
    UNKNOWN,
    ;

    companion object {
        fun fromTermination(reason: TerminationReason): GameResult {
            return when {
                reason.draw -> DRAW
                reason.sideMated != null -> {
                    if (reason.sideMated == Side.WHITE) {
                        BLACK_WIN_CHECKMATE
                    } else {
                        WHITE_WIN_CHECKMATE
                    }
                }
                reason.resignation != null -> {
                    if (reason.resignation == Side.WHITE) {
                        WHITE_RESIGNED
                    } else {
                        BLACK_RESIGNED
                    }
                }
                else -> UNKNOWN
            }
        }
    }
}

data class SavedGame(
    val id: String,
    val userId: String,
    val username: String,
    val createdAt: Long,
    val mode: GameMode,
    val result: GameResult,
    val pgn: String,
)
