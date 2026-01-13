package dev.mcd.chess.feature.auth.domain

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor(
    private val authRepository: AuthRepository,
) {
    private val currentUserFlow = MutableStateFlow<AuthenticatedUser?>(null)

    val currentUser: StateFlow<AuthenticatedUser?> = currentUserFlow.asStateFlow()

    suspend fun login(username: String, password: String): Boolean {
        val authenticatedUser = authRepository.authenticate(username, password)
        currentUserFlow.value = authenticatedUser
        return authenticatedUser != null
    }

    fun logout() {
        currentUserFlow.value = null
    }
}
