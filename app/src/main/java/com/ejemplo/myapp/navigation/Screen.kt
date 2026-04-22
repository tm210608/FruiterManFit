package com.ejemplo.myapp.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector? = null, val label: String = "") {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home", Icons.Default.Home, "HOME")
    object Plans : Screen("plans", Icons.Default.QueryStats, "PLANS")
    object Session : Screen("session", Icons.Default.FlashOn, "PLAY")
    object Social : Screen("social", Icons.Default.Groups, "SOCIAL")
    object Profile : Screen("profile", Icons.Default.Person, "PROFILE")
    object Settings : Screen("settings", Icons.Default.Settings, "SETTINGS")
    object History : Screen("history")
    object Nutrition : Screen("nutrition", Icons.Default.Restaurant)
    object Mobility : Screen("mobility")
    object ExerciseDetail : Screen("exercise_detail/{exerciseId}") {
        fun createRoute(exerciseId: String) = "exercise_detail/$exerciseId"
    }
}
