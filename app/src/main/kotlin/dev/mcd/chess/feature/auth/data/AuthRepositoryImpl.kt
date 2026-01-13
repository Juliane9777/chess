package dev.mcd.chess.feature.auth.data

import dev.mcd.chess.feature.auth.domain.AuthRepository
import dev.mcd.chess.feature.auth.domain.AuthenticatedUser
import dev.mcd.chess.feature.auth.domain.UserRole
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val passwordHasher: PasswordHasher,
) : AuthRepository {
    override suspend fun authenticate(username: String, password: String): AuthenticatedUser? {
        val user = userDao.findByUsername(username) ?: return null
        if (!passwordHasher.matches(password, user.passwordHash)) return null
        return AuthenticatedUser(
            id = user.id,
            username = user.username,
            role = UserRole.valueOf(user.role),
        )
    }
}
