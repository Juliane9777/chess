package dev.mcd.chess.ui.screen.login

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.mcd.chess.feature.auth.domain.SessionManager
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sessionManager: SessionManager,
) : ViewModel(), ContainerHost<LoginViewModel.State, LoginViewModel.SideEffect> {

    override val container = container<State, SideEffect>(State())

    fun updateUsername(username: String) {
        intent {
            reduce { state.copy(username = username, errorMessage = null) }
        }
    }

    fun updatePassword(password: String) {
        intent {
            reduce { state.copy(password = password, errorMessage = null) }
        }
    }

    fun login() {
        intent {
            if (state.isLoading) return@intent
            reduce { state.copy(isLoading = true, errorMessage = null) }
            val success = sessionManager.login(state.username.trim(), state.password)
            if (success) {
                postSideEffect(SideEffect.LoginSuccess)
            } else {
                reduce { state.copy(isLoading = false, errorMessage = LoginError.InvalidCredentials) }
            }
        }
    }

    sealed interface SideEffect {
        object LoginSuccess : SideEffect
    }

    @Stable
    data class State(
        val username: String = "",
        val password: String = "",
        val isLoading: Boolean = false,
        val errorMessage: LoginError? = null,
    )
}

enum class LoginError {
    InvalidCredentials,
}
