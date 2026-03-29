package com.ejemplo.myapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ejemplo.myapp.ui.screens.*
import com.ejemplo.myapp.ui.viewmodels.*

@Composable
fun SetupNavGraph(
    navController: NavHostController
) {
    // Compartimos el WorkoutSessionViewModel para que persista mientras dura la sesión
    // y para que la pantalla de selección pueda añadirle ejercicios
    val sessionViewModel: WorkoutSessionViewModel = hiltViewModel()

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
            val viewModel: DashboardViewModel = hiltViewModel()
            DashboardScreen(
                viewModel = viewModel,
                onLaunchWorkout = { navController.navigate(Screen.Session.route) },
                onSeeAllPlans = { navController.navigate(Screen.Plans.route) }
            )
        }
        composable(Screen.Plans.route) {
            val viewModel: ExerciseLibraryViewModel = hiltViewModel()
            ExerciseLibraryScreen(
                onExerciseClick = { exerciseId -> 
                    // Si venimos de la sesión, añadimos el ejercicio y volvemos
                    sessionViewModel.addExerciseById(exerciseId)
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }
        composable(Screen.Session.route) {
            WorkoutLogScreen(
                viewModel = sessionViewModel,
                onFinish = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Session.route) { inclusive = true }
                    }
                },
                onAddExercise = { 
                    // Vamos a la librería pero con la intención de seleccionar uno
                    navController.navigate(Screen.Plans.route) 
                }
            )
        }
        composable(Screen.Social.route) {
            SocialScreen()
        }
        composable(Screen.Profile.route) {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                viewModel = viewModel
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
