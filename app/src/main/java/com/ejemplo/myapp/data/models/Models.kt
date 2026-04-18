package com.ejemplo.myapp.data.models

import androidx.compose.ui.graphics.Color

data class Workout(
    val id: String,
    val title: String,
    val duration: String,
    val level: String,
    val color: Color? = null
)

data class Exercise(
    val id: String,
    val name: String,
    val bodyPart: String,
    val equipment: String,
    val gifUrl: String,
    val target: String,
    val secondaryMuscles: List<String>,
    val instructions: List<String>,
    val accentColor: Color? = null,
    val description: String = "",
    val difficulty: String = "",
    val category: String = ""
)

data class UserStats(
    val level: Int,
    val rank: String,
    val streak: Int,
    val calories: String,
    val goalReached: Int,
    val totalVolume: Double = 0.0,
    val weeklyVolume: List<Double> = emptyList(),
    val weeklySessionsCount: Int = 0,
    val weeklyGoal: Int = 5
)

data class SessionSet(
    val number: Int,
    val weight: String,
    val reps: String,
    val isDone: Boolean
)

data class ActiveExercise(
    val exerciseId: String,
    val name: String,
    val subtitle: String,
    val accentColor: Color,
    val sets: List<SessionSet>,
    val gifUrl: String = ""
)
