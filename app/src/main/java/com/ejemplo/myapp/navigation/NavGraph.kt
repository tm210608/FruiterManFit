package com.ejemplo.myapp.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ejemplo.myapp.data.repository.FitnessRepository
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
            SplashScreen(
                onNavigateToDashboard = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
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
                navController = navController,
                viewModel = viewModel,
                onLaunchWorkout = { navController.navigate(Screen.Session.route) },
                onSeeAllPlans = { navController.navigate(Screen.Plans.route) }
            )
        }
        composable(Screen.Plans.route) {
            val viewModel: ExerciseLibraryViewModel = hiltViewModel()
            ExerciseLibraryScreen(
                onExerciseClick = { exerciseId ->
                    navController.navigate(Screen.ExerciseDetail.createRoute(exerciseId))
                },
                onAddExercise = { exerciseId ->
                    sessionViewModel.addExerciseById(exerciseId)
                    // Notificamos visualmente que se añadió, o navegamos a la sesión
                    navController.navigate(Screen.Session.route)
                },
                viewModel = viewModel
            )
        }
        composable(
            route = Screen.ExerciseDetail.route,
            arguments = listOf(navArgument("exerciseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId") ?: ""
            val detailViewModel: ExerciseDetailViewModel = hiltViewModel()
            
            ExerciseDetailScreen(
                exerciseId = exerciseId,
                repository = detailViewModel.repository,
                onBack = { navController.popBackStack() },
                onStartExercise = { id ->
                    sessionViewModel.addExerciseById(id)
                    navController.navigate(Screen.Session.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                    }
                }
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
                    navController.navigate("select_exercise") 
                }
            )
        }
        composable("select_exercise") {
            val viewModel: ExerciseLibraryViewModel = hiltViewModel()
            ExerciseLibraryScreen(
                onExerciseClick = { exerciseId ->
                    navController.navigate(Screen.ExerciseDetail.createRoute(exerciseId))
                },
                onAddExercise = { exerciseId ->
                    sessionViewModel.addExerciseById(exerciseId)
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }
        composable(Screen.Social.route) {
            SocialScreen()
        }
        composable(Screen.Profile.route) {
            val viewModel: ProfileViewModel = hiltViewModel()
            ProfileScreen(
                onSettingsClick = { navController.navigate(Screen.Settings.route) },
                onHistoryClick = { navController.navigate(Screen.History.route) },
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
        composable(Screen.History.route) {
            val detailViewModel: ExerciseDetailViewModel = hiltViewModel() // Using detailViewModel for repo access or create a dedicated one
            HistoryScreen(
                repository = detailViewModel.repository,
                onBack = { navController.popBackStack() }
            )
        }
        composable(Screen.Nutrition.route) {
            NutritionLogScreen(onBack = { navController.popBackStack() })
        }
        composable(Screen.Mobility.route) {
            MobilityScreen(onBack = { navController.popBackStack() })
        }
    }
}
