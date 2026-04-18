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
