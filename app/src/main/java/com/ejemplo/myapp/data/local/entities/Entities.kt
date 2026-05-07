package com.ejemplo.myapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ejemplo.myapp.data.local.converters.StringListConverter

@Entity(tableName = "exercises")
@TypeConverters(StringListConverter::class)
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val bodyPart: String,
    val equipment: String,
    val gifUrl: String,
    val target: String,
    val secondaryMuscles: List<String>,
    val instructions: List<String>,
    val accentColorHex: String,
    val description: String = "",
    val difficulty: String = "",
    val category: String = "",
    val remoteId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val startTime: Long,
    val duration: Long,
    val totalCalories: Int,
    val remoteId: String? = null,
    val syncStatus: String = "SYNCED", // PENDING, SYNCED, DIRTY
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "session_exercises")
data class SessionExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: Long,
    val exerciseId: String,
    val exerciseName: String,
    val accentColorHex: String,
    val gifUrl: String = "",
    val remoteId: String? = null,
    val syncStatus: String = "SYNCED",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "exercise_sets")
data class ExerciseSetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionExerciseId: Long,
    val setNumber: Int,
    val weight: Double,
    val reps: Int,
    val isDone: Boolean,
    val remoteId: String? = null,
    val syncStatus: String = "SYNCED",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val userUuid: String = java.util.UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val password: String, // LEGACY: plaintext stored during v7 and earlier. Migrate to passwordHash on login.
    val passwordHash: String = "",
    val rank: String,
    val weeklyGoal: Int = 5,
    val avatarUrl: String? = null,
    val remoteId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "fruit_challenges")
data class FruitChallengeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val iconType: String, // e.g., "APPLE", "BANANA", "CHERRY"
    val progress: Float,
    val target: Float,
    val isCompleted: Boolean = false,
    val isClaimed: Boolean = false,
    val category: String, // e.g., "DAILY", "WEEKLY"
    val remoteId: String? = null,
    val syncStatus: String = "SYNCED",
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
