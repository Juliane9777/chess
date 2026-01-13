package dev.mcd.chess.feature.auth.data

import android.util.Base64
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordHasher @Inject constructor() {
    fun hash(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hashedBytes = digest.digest(password.toByteArray())
        return Base64.encodeToString(hashedBytes, Base64.NO_WRAP)
    }

    fun matches(password: String, hash: String): Boolean = hash(password) == hash
}
