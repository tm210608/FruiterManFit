package com.ejemplo.myapp.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ejemplo.myapp.R
import com.ejemplo.myapp.navigation.Screen
import com.ejemplo.myapp.ui.components.*
import com.ejemplo.myapp.ui.theme.*
import com.ejemplo.myapp.ui.viewmodels.DashboardViewModel

@Composable
fun DashboardScreen(
    navController: androidx.navigation.NavController, // Añadido para navegación
    viewModel: DashboardViewModel = viewModel(),
    onLaunchWorkout: () -> Unit,
    onSeeAllPlans: () -> Unit
) {
    val workout by viewModel.workout.collectAsState()
    val stats by viewModel.stats.collectAsState()
    val challenges by viewModel.challenges.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        MainHeader(
            stringResource(R.string.dashboard_title_brand),
            stringResource(R.string.dashboard_title_subtitle)
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.dashboard_stat_weekly_label),
                value = "${stats.weeklySessionsCount}/${stats.weeklyGoal}",
                bottomText = stringResource(R.string.dashboard_stat_workouts_done),
                containerColor = Surface,
                valueColor = BrightBlue,
                icon = null
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = stringResource(R.string.dashboard_stat_streak_label),
                value = stringResource(R.string.dashboard_stat_streak_value, stats.streak),
                bottomText = null,
                containerColor = BrightLime,
                valueColor = Background,
                icon = Icons.Default.FlashOn,
                iconColor = Background
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        SectionHeader(
            stringResource(R.string.dashboard_section_today_workout),
            stringResource(R.string.dashboard_action_see_all),
            onActionClicked = onSeeAllPlans
        )
        
        FeaturedWorkoutCard(
            title = workout.title,
            duration = workout.duration,
            level = workout.level,
            onLaunch = { onLaunchWorkout() }
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        SectionHeader(
            stringResource(R.string.dashboard_section_strength_progress),
            stringResource(R.string.dashboard_section_volume)
        )
        VolumeChart(weeklyVolume = stats.weeklyVolume)

        Spacer(modifier = Modifier.height(32.dp))
        SectionHeader(
            stringResource(R.string.challenges_title),
            stringResource(R.string.challenges_subtitle)
        )
        ChallengesSection(
            challenges = challenges,
            onClaim = { viewModel.claimChallenge(it) }
        )

        Spacer(modifier = Modifier.height(32.dp))
        
        SectionHeader(
            stringResource(R.string.dashboard_section_next)
        )
        ActivityItem(
            stringResource(R.string.dashboard_activity_mobility),
            stringResource(R.string.dashboard_activity_mobility_time),
            Icons.Default.Schedule,
            BrightBlue,
            onClick = { /* Implementar navegación o acción */ }
        )
        Spacer(modifier = Modifier.height(12.dp))
        ActivityItem(
            stringResource(R.string.dashboard_activity_nutrition),
            stringResource(R.string.dashboard_activity_nutrition_time),
            Icons.Default.Balance,
            BrightLime,
            onClick = { navController.navigate(Screen.Nutrition.route) }
        )
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}
