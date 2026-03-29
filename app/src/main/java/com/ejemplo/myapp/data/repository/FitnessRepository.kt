package com.ejemplo.myapp.data.repository

import com.ejemplo.myapp.data.local.dao.FitnessDao
import com.ejemplo.myapp.data.local.entities.*
import com.ejemplo.myapp.data.models.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FitnessRepository @Inject constructor(private val fitnessDao: FitnessDao) {
    
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

    // Sessions
    suspend fun saveWorkoutSession(title: String, durationMillis: Long, calories: Int, activeExercises: List<ActiveExercise>) {
        val sessionId = fitnessDao.insertSession(
            WorkoutSessionEntity(
                title = title,
                startTime = System.currentTimeMillis(),
                duration = durationMillis,
                totalCalories = calories
            )
        )

        activeExercises.forEach { exercise ->
            val sessionExerciseId = fitnessDao.insertSessionExercise(
                SessionExerciseEntity(
                    sessionId = sessionId,
                    exerciseId = exercise.exerciseId,
                    exerciseName = exercise.name,
                    accentColorHex = String.format("#%06X", (0xFFFFFF and exercise.accentColor.toArgb()))
                )
            )

            exercise.sets.forEach { set ->
                fitnessDao.insertSet(
                    ExerciseSetEntity(
                        sessionExerciseId = sessionExerciseId,
                        setNumber = set.number,
                        weight = set.weight.toDoubleOrNull() ?: 0.0,
                        reps = set.reps.toIntOrNull() ?: 0,
                        isDone = set.isDone
                    )
                )
            }
        }
    }

    fun getAllSessions(): Flow<List<WorkoutSessionEntity>> = fitnessDao.getAllSessions()

    // Dashboard Data - Calculated from Room
    fun getRealUserStats(): Flow<UserStats> {
        return fitnessDao.getAllSessions().map { sessions ->
            val totalCalories = sessions.sumOf { it.totalCalories }
            val sessionCount = sessions.size
            
            UserStats(
                level = (sessionCount / 5) + 1, // Ejemplo: cada 5 entrenos subes de nivel
                rank = when {
                    sessionCount > 50 -> "Fruit Legend"
                    sessionCount > 20 -> "Fruit Ninja"
                    else -> "Fresh Fruit"
                },
                streak = calculateStreak(sessions),
                calories = if (totalCalories >= 1000) "${String.format("%.1f", totalCalories / 1000.0)}k" else totalCalories.toString(),
                goalReached = (sessionCount % 10) * 10 // Ejemplo: meta de 10 entrenos
            )
        }
    }

    private fun calculateStreak(sessions: List<WorkoutSessionEntity>): Int {
        if (sessions.isEmpty()) return 0
        // Lógica simple de racha (contar sesiones en días consecutivos)
        // Por ahora devolvemos el conteo total para simplificar
        return sessions.size 
    }

    fun getTodaysWorkout() = Workout(
        id = "1",
        title = "CITRUS\nSHRED",
        duration = "45 Mins",
        level = "Advanced"
    )

    // Data Mapping Extension
    private fun ExerciseEntity.toDomain() = Exercise(
        id = id,
        name = name,
        level = level,
        category = category,
        accentColor = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(accentColorHex))
    )

    private fun androidx.compose.ui.graphics.Color.toArgb(): Int {
        return (this.alpha * 255).toInt() shl 24 or
                ((this.red * 255).toInt() shl 16) or
                ((this.green * 255).toInt() shl 8) or
                (this.blue * 255).toInt()
    }
}
