package com.ejemplo.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ejemplo.myapp.navigation.Screen
import com.ejemplo.myapp.navigation.SetupNavGraph
import com.ejemplo.myapp.ui.theme.*
import com.ejemplo.myapp.ui.viewmodels.FitnessViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FruiterManTheme {
                MainContainer()
            }
        }
    }
}

@Composable
fun MainContainer() {
    val context = LocalContext.current
    val app = context.applicationContext as FitnessApplication
    val factory = FitnessViewModelFactory(app.repository)
    
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val noBottomBarScreens = listOf(
        Screen.Splash.route,
        Screen.Login.route,
        Screen.Signup.route,
        Screen.Session.route,
        Screen.Settings.route
    )

    Scaffold(
        bottomBar = {
            if (currentRoute !in noBottomBarScreens) {
                BottomNavBar(navController = navController)
            }
        },
        containerColor = Background
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues)) {
            SetupNavGraph(navController = navController, factory = factory)
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(72.dp)
                .align(Alignment.BottomCenter),
            color = Surface.copy(alpha = 0.9f),
            shape = RoundedCornerShape(32.dp),
            tonalElevation = 8.dp
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NavItem(Screen.Home, currentRoute == Screen.Home.route) { 
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                NavItem(Screen.Plans, currentRoute == Screen.Plans.route) { 
                    navController.navigate(Screen.Plans.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                
                Spacer(modifier = Modifier.width(64.dp))
                
                NavItem(Screen.Social, currentRoute == Screen.Social.route) { 
                    navController.navigate(Screen.Social.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
                NavItem(Screen.Profile, currentRoute == Screen.Profile.route) { 
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Home.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
        
        FloatingActionButton(
            onClick = { navController.navigate(Screen.Session.route) },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .offset(y = (-24).dp)
                .size(72.dp),
            containerColor = BrightLime,
            contentColor = Background,
            shape = CircleShape
        ) {
            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(36.dp))
        }
    }
}

@Composable
fun NavItem(screen: Screen, isSelected: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                imageVector = screen.icon!!,
                contentDescription = screen.label,
                tint = if (isSelected) BrightLime else OnSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
            if (isSelected) {
                Box(modifier = Modifier.size(4.dp).background(BrightLime, CircleShape))
            }
        }
    }
}
