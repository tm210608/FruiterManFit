package com.ejemplo.myapp.data.repository

import com.ejemplo.myapp.data.local.dao.FitnessDao
import com.ejemplo.myapp.data.local.entities.UserEntity
import kotlinx.coroutines.flow.Flow

class UserRepository(private val fitnessDao: FitnessDao) {

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

    suspend fun loginUser(email: String, password: String): Boolean {
        // En una app real, aquí harías una consulta específica para validar credenciales
        // Por ahora mantenemos la simplicidad de la estructura de Room
        return true 
    }
}
