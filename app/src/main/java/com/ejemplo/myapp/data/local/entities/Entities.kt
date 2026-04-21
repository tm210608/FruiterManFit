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
    val category: String = ""
)

@Entity(tableName = "workout_sessions")
data class WorkoutSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val startTime: Long,
    val duration: Long,
    val totalCalories: Int
)

@Entity(tableName = "session_exercises")
data class SessionExerciseEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionId: Long,
    val exerciseId: String,
    val exerciseName: String,
    val accentColorHex: String,
    val gifUrl: String = ""
)

@Entity(tableName = "exercise_sets")
data class ExerciseSetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sessionExerciseId: Long,
    val setNumber: Int,
    val weight: Double,
    val reps: Int,
    val isDone: Boolean
)

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: Int = 1,
    val name: String,
    val email: String,
    val password: String,
    val rank: String,
    val weeklyGoal: Int = 5,
    val avatarUrl: String? = null
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
    val category: String // e.g., "DAILY", "WEEKLY"
)
