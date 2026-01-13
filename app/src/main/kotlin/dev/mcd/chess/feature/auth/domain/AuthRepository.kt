package dev.mcd.chess.feature.auth.domain

interface AuthRepository {
    suspend fun authenticate(username: String, password: String): AuthenticatedUser?
}
