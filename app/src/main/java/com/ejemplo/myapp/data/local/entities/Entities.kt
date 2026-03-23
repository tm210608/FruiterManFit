package com.ejemplo.myapp.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val level: String,
    val category: String,
    val accentColorHex: String
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
    val accentColorHex: String
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
