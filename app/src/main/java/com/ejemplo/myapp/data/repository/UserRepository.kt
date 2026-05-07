package com.ejemplo.myapp.data.repository

import com.ejemplo.myapp.data.local.dao.FitnessDao
import com.ejemplo.myapp.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import org.mindrot.jbcrypt.BCrypt
import javax.inject.Inject

class UserRepository @Inject constructor(private val fitnessDao: FitnessDao) {

    fun getCurrentUser(): Flow<UserEntity?> = fitnessDao.getUser()

    suspend fun registerUser(name: String, email: String, password: String) {
        val hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt())
        val newUser = UserEntity(
            name = name,
            email = email,
            password = "", // no longer store plaintext
            passwordHash = hashedPassword,
            rank = "Newbie"
        )
        fitnessDao.insertUser(newUser)
    }

    suspend fun loginUser(email: String, password: String): UserEntity? {
        val user = fitnessDao.getUserByEmail(email) ?: return null

        // Modern: check against passwordHash
        if (user.passwordHash.isNotEmpty()) {
            return if (BCrypt.checkpw(password, user.passwordHash)) user else null
        }

        // Legacy fallback: plaintext comparison for users created before v9
        return if (user.password == password) {
            // Migrate to hash on successful legacy login
            val hashed = BCrypt.hashpw(password, BCrypt.gensalt())
            fitnessDao.insertUser(user.copy(password = "", passwordHash = hashed))
            user.copy(password = "", passwordHash = hashed)
        } else {
            null
        }
    }
}
