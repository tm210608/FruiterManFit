package com.ejemplo.myapp.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.ejemplo.myapp.R

@Composable
fun translateCategory(category: String): String {
    return when (category.lowercase()) {
        "waist" -> stringResource(R.string.category_waist)
        "chest" -> stringResource(R.string.category_chest)
        "back" -> stringResource(R.string.category_back)
        "cardio" -> stringResource(R.string.category_cardio)
        "upper arms" -> stringResource(R.string.category_upper_arms)
        "lower arms" -> stringResource(R.string.category_lower_arms)
        "upper legs" -> stringResource(R.string.category_upper_legs)
        "lower legs" -> stringResource(R.string.category_lower_legs)
        "shoulders" -> stringResource(R.string.category_shoulders)
        "neck" -> stringResource(R.string.category_neck)
        else -> category
    }
}
