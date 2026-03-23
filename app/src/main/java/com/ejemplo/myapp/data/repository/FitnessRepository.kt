package com.ejemplo.myapp.data.repository

import com.ejemplo.myapp.data.local.dao.FitnessDao
import com.ejemplo.myapp.data.local.entities.ExerciseEntity
import com.ejemplo.myapp.data.models.*
import com.ejemplo.myapp.ui.theme.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FitnessRepository(private val fitnessDao: FitnessDao) {
    
    // Exercises
    fun getExercises(): Flow<List<Exercise>> {
        return fitnessDao.getAllExercises().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun refreshExercises() {
        val remoteExercises = listOf(
            ExerciseEntity("1", "Diamond Pushups", "Intermediate", "Chest", "#00D4FF"),
            ExerciseEntity("2", "Bulgarian Split", "Advanced", "Legs", "#938F99"),
            ExerciseEntity("3", "Pull-ups", "Advanced", "Back", "#00D4FF"),
            ExerciseEntity("4", "Tricep Dips", "Beginner", "Arms", "#FF007A")
        )
        fitnessDao.insertExercises(remoteExercises)
    }

    // Dashboard Data
    fun getTodaysWorkout() = Workout(
        id = "1",
        title = "CITRUS\nSHRED",
        duration = "45 Mins",
        level = "Advanced"
    )

    fun getUserStats() = UserStats(
        level = 24,
        rank = "Fruit Ninja",
        streak = 12,
        calories = "4.2k",
        goalReached = 85
    )

    // Data Mapping Extension
    private fun ExerciseEntity.toDomain() = Exercise(
        id = id,
        name = name,
        level = level,
        category = category,
        accentColor = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(accentColorHex))
    )
}
