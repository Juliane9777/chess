package dev.mcd.chess.feature.auth.domain

data class AuthenticatedUser(
    val id: String,
    val username: String,
    val role: UserRole,
)

enum class UserRole {
    ADMIN,
    USER,
}
