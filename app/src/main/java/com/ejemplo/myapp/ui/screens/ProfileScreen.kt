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
import androidx.compose.ui.res.stringResource
import com.ejemplo.myapp.R
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
                Text(
                    text = stringResource(R.string.profile_greeting, stats.userName),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.profile_level_rank, stats.level, stats.rank),
                    color = OnSurfaceVariant,
                    fontSize = 16.sp
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                   ProfileStat("${stats.streak}", stringResource(R.string.profile_stat_streak))
                   RecapDivider()
                   ProfileStat(stats.calories, stringResource(R.string.profile_stat_calories))
                   RecapDivider()
                   ProfileStat(
                       if(stats.totalVolume > 1000) "${String.format("%.1f", stats.totalVolume/1000)}k" else "${stats.totalVolume.toInt()}",
                       stringResource(R.string.profile_stat_volume)
                   )
                   RecapDivider()
                   ProfileStat("${stats.goalReached}%", stringResource(R.string.profile_stat_goal))
                }
            }
        }
        
        Column(modifier = Modifier.padding(horizontal = 24.dp)) {
            SectionHeader(
                stringResource(R.string.dashboard_stat_weekly_label),
                stringResource(R.string.profile_action_history),
                onActionClicked = onHistoryClick
            )
            // ... Rest of the UI remains focused on the state
            Card(
                modifier = Modifier.fillMaxWidth().padding(bottom = 32.dp),
                colors = CardDefaults.cardColors(containerColor = Surface),
                shape = RoundedCornerShape(28.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = stringResource(R.string.profile_advice_default),
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
            BadgesSection(stats.badges)
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun BadgesSection(badges: List<com.ejemplo.myapp.data.models.Badge>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        SectionHeader(
            stringResource(R.string.profile_badge_title),
            stringResource(R.string.profile_badge_description)
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            badges.forEach { badge ->
                BadgeItem(badge, modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun BadgeItem(badge: com.ejemplo.myapp.data.models.Badge, modifier: Modifier = Modifier) {
    val alpha = if (badge.isUnlocked) 1f else 0.2f
    val color = if (badge.isUnlocked) BrightLime else OnSurfaceVariant
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(color.copy(alpha = 0.1f), CircleShape)
                .border(BorderStroke(1.dp, color.copy(alpha = 0.3f * alpha)), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = badge.icon as androidx.compose.ui.graphics.vector.ImageVector,
                contentDescription = null,
                tint = color.copy(alpha = alpha),
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = badge.title,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = if (badge.isUnlocked) OnSurface else OnSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview(showBackground = true)
@Composable
fun PreviewProfileScreen() {
    FruiterManTheme {
        ProfileScreen(onSettingsClick = {}, onHistoryClick = {})
    }
}
