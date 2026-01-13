package dev.mcd.chess.feature.history

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.mcd.chess.feature.history.data.GameHistoryRepositoryImpl
import dev.mcd.chess.feature.history.data.SaveGameImpl
import dev.mcd.chess.feature.history.domain.GameHistoryRepository
import dev.mcd.chess.feature.history.domain.SaveGame
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class HistoryModule {
    @Binds
    @Singleton
    abstract fun gameHistoryRepository(impl: GameHistoryRepositoryImpl): GameHistoryRepository

    @Binds
    @Singleton
    abstract fun saveGame(impl: SaveGameImpl): SaveGame
}
