package dev.mcd.chess.feature.auth.data

import dev.mcd.chess.feature.auth.domain.UserRole

object AuthDefaults {
    const val adminUsername = "admin"
    const val adminPassword = "admin"
    const val userUsername = "user"
    const val userPassword = "user"

    fun defaultUsers(passwordHasher: PasswordHasher): List<UserEntity> {
        return listOf(
            UserEntity(
                id = "admin",
                username = adminUsername,
                passwordHash = passwordHasher.hash(adminPassword),
                role = UserRole.ADMIN.name,
            ),
            UserEntity(
                id = "user",
                username = userUsername,
                passwordHash = passwordHasher.hash(userPassword),
                role = UserRole.USER.name,
            ),
        )
    }
}
