package dev.mcd.chess.feature.storage

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.mcd.chess.feature.auth.data.AuthDefaults
import dev.mcd.chess.feature.auth.data.PasswordHasher
import dev.mcd.chess.feature.auth.data.UserDao
import dev.mcd.chess.feature.history.data.GameDao
import dev.mcd.chess.feature.storage.data.ChessDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun database(
        @ApplicationContext context: Context,
        passwordHasher: PasswordHasher,
    ): ChessDatabase {
        val database = Room.databaseBuilder(context, ChessDatabase::class.java, "chess.db").build()
        CoroutineScope(Dispatchers.IO).launch {
            val userDao = database.userDao()
            if (userDao.countUsers() == 0) {
                userDao.insertAll(AuthDefaults.defaultUsers(passwordHasher))
            }
        }
        return database
    }

    @Provides
    fun userDao(database: ChessDatabase): UserDao = database.userDao()

    @Provides
    fun gameDao(database: ChessDatabase): GameDao = database.gameDao()
}
