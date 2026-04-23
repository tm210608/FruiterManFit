package com.ejemplo.myapp.data.repository

import android.util.Log
import com.ejemplo.myapp.data.local.dao.FitnessDao
import com.ejemplo.myapp.data.local.entities.*
import com.ejemplo.myapp.data.models.*
import com.ejemplo.myapp.data.remote.ExerciseApiService
import com.ejemplo.myapp.data.remote.ExerciseDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Star
import javax.inject.Inject

class FitnessRepository @Inject constructor(
    private val fitnessDao: FitnessDao,
    private val exerciseApiService: ExerciseApiService
) {
    
    // Fruit Challenges
    fun getFruitChallenges(): Flow<List<FruitChallenge>> {
        return fitnessDao.getAllChallenges().map { entities ->
            if (entities.isEmpty()) {
                val initial = listOf(
                    FruitChallengeEntity("1", "Apple Power", "Complete 3 chest exercises", "APPLE", 0f, 3f, false, false, "DAILY"),
                    FruitChallengeEntity("2", "Banana Boost", "Train 3 days in a row", "BANANA", 1f, 3f, false, false, "WEEKLY")
                )
                fitnessDao.insertChallenges(initial)
                initial.map { it.toDomain() }
            } else {
                entities.map { it.toDomain() }
            }
        }
    }

    suspend fun claimChallenge(id: String) {
        fitnessDao.claimChallenge(id)
    }

    private fun FruitChallengeEntity.toDomain() = FruitChallenge(
        id = id,
        title = title,
        description = description,
        icon = iconType,
        progress = progress / target,
        isCompleted = isCompleted,
        isClaimed = isClaimed
    )

    // Exercises from Local DB
    fun getExercises(): Flow<List<Exercise>> {
        return fitnessDao.getAllExercises().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    suspend fun getExerciseById(id: String): Exercise? {
        return fitnessDao.getExerciseById(id)?.toDomain()
    }

    // CARGA DESDE EXERCISEDB (1300+ EJERCICIOS CON GIFS)
    suspend fun refreshExercises(apiKey: String) = withContext(Dispatchers.IO) {
        Log.d("FruiterMan", "Iniciando descarga desde ExerciseDB...")
        try {
            val allExercises = exerciseApiService.getFullDataset()
            
            if (allExercises.isEmpty()) {
                Log.e("FruiterMan", "Error: El dataset vino vacío.")
                return@withContext
            }
            
            Log.d("FruiterMan", "Descargados ${allExercises.size} ejercicios con éxito.")

            val entities = allExercises
                .filter { it.id != null }
                .distinctBy { it.id }
                .map { it.toEntity() }
            
            fitnessDao.clearExercises() 
            fitnessDao.insertExercises(entities)
            val finalCount = fitnessDao.getExerciseCount()
            Log.d("FruiterMan", "Sincronización finalizada. Total real en BD: $finalCount")
        } catch (e: Exception) {
            Log.e("FruiterMan", "Error crítico en sincronización: ${e.message}")
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

    fun getAllSessions(): Flow<List<WorkoutSessionEntity>> {
        return fitnessDao.getFullSessions().map { fullSessions ->
            fullSessions.map { it.session }
        }
    }

    // Dashboard Data - Calculated from Room
    fun getRealUserStats(): Flow<UserStats> {
        return combine(
            fitnessDao.getFullSessions(),
            fitnessDao.getUser()
        ) { fullSessions, user ->
            val totalCalories = fullSessions.sumOf { it.session.totalCalories }
            val sessionCount = fullSessions.size
            
            val allSets = fullSessions.flatMap { session -> 
                session.exercises.flatMap { it.sets }
            }
            val totalVolume = allSets.filter { it.isDone }.sumOf { it.weight * it.reps }
            
            val now = System.currentTimeMillis()
            val dayMillis = 24 * 60 * 60 * 1000L
            val weekStart = now - (7 * dayMillis)
            
            val sessionsThisWeek = fullSessions.filter { it.session.startTime >= weekStart }
            val weeklySessionsCount = sessionsThisWeek.size

            val badges = listOf(
                Badge("1", "Primer Paso", Icons.Default.Star, sessionCount >= 1),
                Badge("2", "Guerrero Frutal", Icons.Default.FlashOn, sessionCount >= 10),
                Badge("3", "Leyenda Cítrica", Icons.Default.EmojiEvents, sessionCount >= 50)
            )

            val weeklyVolume = (0..6).map { dayOffset ->
                val dayStart = now - (6 - dayOffset + 1) * dayMillis
                val dayEnd = now - (6 - dayOffset) * dayMillis
                
                fullSessions
                    .filter { it.session.startTime in dayStart..dayEnd }
                    .flatMap { it.exercises }
                    .flatMap { it.sets }
                    .filter { it.isDone }
                    .sumOf { it.weight * it.reps }
            }

            UserStats(
                userName = user?.name ?: "Fresh Fruit",
                level = (sessionCount / 5) + 1,
                rank = user?.rank ?: when {
                    sessionCount > 50 -> "Fruit Legend"
                    sessionCount > 20 -> "Fruit Ninja"
                    else -> "Fresh Fruit"
                },
                streak = calculateStreak(fullSessions.map { it.session }),
                calories = if (totalCalories >= 1000) "${String.format("%.1f", totalCalories / 1000.0)}k" else totalCalories.toString(),
                goalReached = (sessionCount % 10) * 10,
                totalVolume = totalVolume,
                weeklyVolume = weeklyVolume,
                weeklySessionsCount = weeklySessionsCount,
                weeklyGoal = user?.weeklyGoal ?: 5,
                badges = badges
            )
        }
    }

    private fun calculateStreak(sessions: List<WorkoutSessionEntity>): Int {
        if (sessions.isEmpty()) return 0
        
        val dates = sessions.map { 
            java.util.Calendar.getInstance().apply { 
                timeInMillis = it.startTime 
                set(java.util.Calendar.HOUR_OF_DAY, 0)
                set(java.util.Calendar.MINUTE, 0)
                set(java.util.Calendar.SECOND, 0)
                set(java.util.Calendar.MILLISECOND, 0)
            }.timeInMillis 
        }.distinct().sortedDescending()

        var streak = 0
        var currentDay = java.util.Calendar.getInstance().apply {
            set(java.util.Calendar.HOUR_OF_DAY, 0)
            set(java.util.Calendar.MINUTE, 0)
            set(java.util.Calendar.SECOND, 0)
            set(java.util.Calendar.MILLISECOND, 0)
        }.timeInMillis

        for (date in dates) {
            if (date == currentDay || date == currentDay - 86400000L) {
                streak++
                currentDay = date
            } else {
                break
            }
        }
        return streak
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
        accentColor = androidx.compose.ui.graphics.Color(android.graphics.Color.parseColor(accentColorHex)),
        description = description,
        difficulty = difficulty,
        category = category
    )

    private fun ExerciseDto.toEntity() = ExerciseEntity(
        id = id ?: "",
        name = name ?: "Unknown",
        bodyPart = bodyPart ?: "Various",
        equipment = (equipment ?: "body weight").lowercase(),
        gifUrl = if (!images.isNullOrEmpty()) {
            val imagePath = images[0]
            val url = "https://raw.githubusercontent.com/yuhonas/free-exercise-db/main/exercises/$imagePath"
            url
        } else {
            gifUrl?.replace("http://", "https://") ?: ""
        },
        target = target ?: "General",
        secondaryMuscles = secondaryMuscles ?: emptyList(),
        instructions = when (instructions) {
            is List<*> -> instructions.map { it.toString() }
            is String -> instructions.split(". ").filter { it.isNotBlank() }
            else -> emptyList()
        },
        description = description ?: "",
        difficulty = difficulty ?: "",
        category = category ?: "",
        accentColorHex = when(bodyPart?.lowercase()) {
            "chest" -> "#FF4B4B"
            "back" -> "#4B7BFF"
            "shoulders" -> "#FFB84B"
            "upper arms", "lower arms" -> "#BC4BFF"
            "upper legs", "lower legs" -> "#4BFF81"
            "waist" -> "#FF4BEB"
            "cardio" -> "#FF4B4B"
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
