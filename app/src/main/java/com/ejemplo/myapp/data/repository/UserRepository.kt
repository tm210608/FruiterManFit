package com.ejemplo.myapp.data.repository

import com.ejemplo.myapp.data.local.dao.FitnessDao
import com.ejemplo.myapp.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepository @Inject constructor(private val fitnessDao: FitnessDao) {

    fun getCurrentUser(): Flow<UserEntity?> = fitnessDao.getUser()

    suspend fun registerUser(name: String, email: String, password: String) {
        val newUser = UserEntity(
            name = name,
            email = email,
            password = password,
            rank = "Newbie" // Default rank
        )
        fitnessDao.insertUser(newUser)
    }

    suspend fun loginUser(email: String, password: String): UserEntity? {
        val user = fitnessDao.getUserByEmail(email)
        return if (user != null && user.password == password) {
            user
        } else {
            null
        }
    }
}
