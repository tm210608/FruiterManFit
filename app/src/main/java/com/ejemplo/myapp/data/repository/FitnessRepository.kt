package com.ejemplo.myapp.data.repository

import android.util.Log
import com.ejemplo.myapp.data.local.dao.FitnessDao
import com.ejemplo.myapp.data.local.entities.*
import com.ejemplo.myapp.data.models.*
import com.ejemplo.myapp.data.remote.ExerciseApiService
import com.ejemplo.myapp.data.remote.ExerciseDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FitnessRepository @Inject constructor(
    private val fitnessDao: FitnessDao,
    private val exerciseApiService: ExerciseApiService
) {
    
    // Exercises from Local DB
    fun getExercises(): Flow<List<Exercise>> {
        return fitnessDao.getAllExercises().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getExerciseById(id: String): Exercise? {
        return fitnessDao.getExerciseById(id)?.toDomain()
    }

    // Fetch from ExerciseDB API and Save to Local DB
    suspend fun refreshExercises(apiKey: String) = withContext(Dispatchers.IO) {
        Log.d("FruiterMan", "Iniciando descarga incremental por categorías...")
        try {
            // 1. Obtener lista de partes del cuerpo
            val bodyParts = exerciseApiService.getBodyPartList(apiKey = apiKey)
            Log.d("FruiterMan", "Categorías encontradas: ${bodyParts.size}")

            val allExercises = mutableListOf<ExerciseDto>()
            
            // 2. Descargar ejercicios por categoría
            bodyParts.forEach { bodyPart ->
                Log.d("FruiterMan", "Descargando ejercicios para: $bodyPart")
                try {
                    val exercises = exerciseApiService.getExercisesByBodyPart(apiKey = apiKey, bodyPart = bodyPart)
                    allExercises.addAll(exercises)
                } catch (e: Exception) {
                    Log.e("FruiterMan", "Error descargando $bodyPart: ${e.message}")
                }
            }

            Log.d("FruiterMan", "Total descargados: ${allExercises.size}")
            
            val entities = allExercises.map { it.toEntity() }
            fitnessDao.insertExercises(entities)
            Log.d("FruiterMan", "¡Sincronización con ExerciseDB completada!")
        } catch (e: Exception) {
            Log.e("FruiterMan", "Error crítico en refreshExercises: ${e.message}")
            e.printStackTrace()
        }
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
                    accentColorHex = String.format("#%06X", (0xFFFFFF and exercise.accentColor.toArgb())),
                    gifUrl = exercise.gifUrl
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
                level = (sessionCount / 5) + 1,
                rank = when {
                    sessionCount > 50 -> "Fruit Legend"
                    sessionCount > 20 -> "Fruit Ninja"
                    else -> "Fresh Fruit"
                },
                streak = calculateStreak(sessions),
                calories = if (totalCalories >= 1000) "${String.format("%.1f", totalCalories / 1000.0)}k" else totalCalories.toString(),
                goalReached = (sessionCount % 10) * 10
            )
        }
    }

    private fun calculateStreak(sessions: List<WorkoutSessionEntity>): Int {
        if (sessions.isEmpty()) return 0
        return sessions.size 
    }

    fun getTodaysWorkout() = Workout(
        id = "1",
        title = "CITRUS\nSHRED",
        duration = "45 Mins",
        level = "Advanced"
    )

    // Data Mapping Extensions
    private fun ExerciseEntity.toDomain() = Exercise(
        id = id,
        name = name,
        bodyPart = bodyPart,
        equipment = equipment,
        gifUrl = gifUrl,
        target = target,
        secondaryMuscles = secondaryMuscles,
        instructions = instructions,
        accentColor = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(accentColorHex))
    )

    private fun ExerciseDto.toEntity() = ExerciseEntity(
        id = id ?: "",
        name = name ?: "Unknown",
        bodyPart = bodyPart ?: "Various",
        equipment = equipment ?: "No equipment",
        gifUrl = gifUrl ?: "",
        target = target ?: "General",
        secondaryMuscles = secondaryMuscles ?: emptyList(),
        instructions = instructions ?: emptyList(),
        accentColorHex = when(bodyPart?.lowercase()) {
            "chest" -> "#FF4B4B"
            "back" -> "#4B7BFF"
            "shoulders" -> "#FFB84B"
            "upper arms", "lower arms" -> "#BC4BFF"
            "upper legs", "lower legs" -> "#4BFF81"
            "waist" -> "#FF4BEB"
            else -> "#00D4FF"
        }
    )

    private fun androidx.compose.ui.graphics.Color.toArgb(): Int {
        return (this.alpha * 255).toInt() shl 24 or
                ((this.red * 255).toInt() shl 16) or
                ((this.green * 255).toInt() shl 8) or
                (this.blue * 255).toInt()
    }
}
