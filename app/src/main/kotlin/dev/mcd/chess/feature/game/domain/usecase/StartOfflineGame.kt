package dev.mcd.chess.feature.game.domain.usecase

interface StartOfflineGame {
    suspend operator fun invoke()
}
