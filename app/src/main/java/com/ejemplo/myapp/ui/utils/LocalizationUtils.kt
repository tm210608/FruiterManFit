package com.ejemplo.myapp.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ejemplo.myapp.R
import com.ejemplo.myapp.ui.utils.ExerciseTranslator

@Composable
fun translateCategory(category: String): String {
    return ExerciseTranslator.translateBodyPart(category)
}

fun translateEquipment(equipment: String): String {
    return ExerciseTranslator.translateEquipment(equipment)
}

fun translateMuscle(muscle: String): String {
    return ExerciseTranslator.translateMuscle(muscle)
}

fun translateDifficulty(difficulty: String): String {
    return ExerciseTranslator.translateDifficulty(difficulty)
}
