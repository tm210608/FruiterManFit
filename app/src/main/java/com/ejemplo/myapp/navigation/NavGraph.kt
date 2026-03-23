package com.ejemplo.myapp.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ejemplo.myapp.ui.screens.*
import com.ejemplo.myapp.ui.viewmodels.FitnessViewModelFactory

@Composable
fun SetupNavGraph(
    navController: NavHostController,
    factory: FitnessViewModelFactory
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen {
                navController.navigate(Screen.Login.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            }
        }
        composable(Screen.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                },
                onSignupClick = { navController.navigate(Screen.Signup.route) }
            )
        }
        composable(Screen.Signup.route) {
            SignupScreen(
                onSignupSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Signup.route) { inclusive = true }
                    }
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Home.route) {
            DashboardScreen(
                viewModel = viewModel(factory = factory),
                onLaunchWorkout = { navController.navigate(Screen.Session.route) },
                onSeeAllPlans = { navController.navigate(Screen.Plans.route) }
            )
        }
        composable(Screen.Plans.route) {
            ExerciseLibraryScreen(
                onExerciseClick = { /* Ver detalle si quieres */ },
                viewModel = viewModel(factory = factory)
            )
        }
        composable(Screen.Session.route) {
            WorkoutLogScreen(
                viewModel = viewModel(factory = factory),
                onFinish = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Session.route) { inclusive = true }
                    }
                },
                onAddExercise = { navController.navigate(Screen.Plans.route) }
            )
        }
        composable(Screen.Social.route) {
            SocialScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                viewModel = viewModel(factory = factory)
            )
        }
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}
