package com.ejemplo.myapp.ui.screens

import androidx.compose.foundation.*
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ejemplo.myapp.ui.components.*
import com.ejemplo.myapp.ui.theme.*
import com.ejemplo.myapp.ui.viewmodels.ProfileViewModel

@Composable
fun ProfileScreen(
    onSettingsClick: () -> Unit,
    onHistoryClick: () -> Unit,
    viewModel: ProfileViewModel = viewModel()
) {
    val stats by viewModel.stats.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {
        // Header with gradient overlay
        Box(modifier = Modifier.fillMaxWidth().height(300.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Brush.verticalGradient(listOf(BrightLime.copy(alpha = 0.15f), Color.Transparent)))
            )
            
            // Settings Button
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(24.dp)
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.05f))
            ) {
                Icon(Icons.Default.Settings, contentDescription = null, tint = BrightLime)
            }
            
            Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Avatar
                Box(
                    modifier = Modifier
                        .size(110.dp)
                        .clip(CircleShape)
                        .background(Brush.sweepGradient(listOf(BrightLime, BrightBlue, BrightLime)))
                        .padding(4.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = Surface,
                        shape = CircleShape
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("FM", fontSize = 24.sp, fontWeight = FontWeight.Black, color = BrightLime)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Hey, FruiterMan! 🍎", fontSize = 28.sp, fontWeight = FontWeight.Bold)
                Text(text = "Level ${stats.level} ${stats.rank}", color = OnSurfaceVariant, fontSize = 16.sp)
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                   ProfileStat("${stats.streak}", "STREAK")
                   RecapDivider()
                   ProfileStat(stats.calories, "CALORIES")
                   RecapDivider()
                   ProfileStat(if(stats.totalVolume > 1000) "${String.format("%.1f", stats.totalVolume/1000)}k" else "${stats.totalVolume.toInt()}", "VOLUME")
                   RecapDivider()
                   ProfileStat("${stats.goalReached}%", "GOAL")
                }
            }
        }
        
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            SectionHeader("Weekly Gains", "History", onActionClicked = onHistoryClick)
            // ... Rest of the UI remains focused on the state
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Surface),
                shape = RoundedCornerShape(32.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "You're crushing the cardio, but maybe eat more berries? 🍓",
                        color = OnSurfaceVariant,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                    ProgressChart(
                        modifier = Modifier.fillMaxWidth().height(140.dp),
                        data = stats.weeklyVolume
                    )
                }
            }
            // Badge section etc.
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}
