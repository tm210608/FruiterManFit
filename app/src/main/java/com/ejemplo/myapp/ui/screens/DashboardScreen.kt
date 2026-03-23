package com.ejemplo.myapp.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ejemplo.myapp.ui.components.*
import com.ejemplo.myapp.ui.theme.*
import com.ejemplo.myapp.ui.viewmodels.DashboardViewModel

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = viewModel(),
    onLaunchWorkout: () -> Unit,
    onSeeAllPlans: () -> Unit
) {
    val workout by viewModel.workout.collectAsState()
    val stats by viewModel.stats.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        MainHeader("FRUITERMAN", "Fit Dashboard")
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard(
                modifier = Modifier.weight(1f),
                label = "This Week",
                value = "4/5",
                bottomText = "Workouts Done",
                containerColor = Surface,
                valueColor = BrightBlue,
                icon = null
            )
            StatCard(
                modifier = Modifier.weight(1f),
                label = "Streak",
                value = "${stats.streak} DAYS",
                bottomText = null,
                containerColor = BrightLime,
                valueColor = Background,
                icon = Icons.Default.FlashOn,
                iconColor = Background
            )
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        SectionHeader("Today's Workout", "See All", onActionClicked = onSeeAllPlans)
        
        FeaturedWorkoutCard(
            title = workout.title,
            duration = workout.duration,
            level = workout.level,
            onLaunch = onLaunchWorkout
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        SectionHeader("Next Up")
        ActivityItem("Morning Mobility", "Starts in 2 hours", Icons.Default.Schedule, BrightBlue)
        Spacer(modifier = Modifier.height(12.dp))
        ActivityItem("Nutrition Log", "Update required", Icons.Default.Balance, BrightLime)
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}
