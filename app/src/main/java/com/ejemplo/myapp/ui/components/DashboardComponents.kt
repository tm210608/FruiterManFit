package com.ejemplo.myapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejemplo.myapp.R
import com.ejemplo.myapp.data.models.FruitChallenge
import com.ejemplo.myapp.ui.theme.*

import androidx.compose.ui.tooling.preview.Preview
import com.ejemplo.myapp.ui.theme.FruiterManTheme

@Preview(showBackground = true)
@Composable
fun PreviewStatCard() {
    FruiterManTheme {
        StatCard(
            label = "Weekly",
            value = "5/5",
            bottomText = "Workouts",
            containerColor = Surface,
            valueColor = BrightBlue,
            icon = Icons.Default.FlashOn
        )
    }
}

@Composable
fun StatCard(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    bottomText: String?,
    containerColor: Color,
    valueColor: Color,
    icon: ImageVector?,
    iconColor: Color = OnSurfaceVariant
) {
    Card(
        modifier = modifier.height(140.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = label,
                    color = if (containerColor == BrightLime) Background.copy(alpha = 0.7f) else OnSurfaceVariant,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                icon?.let {
                    Icon(
                        imageVector = it,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
            
            Column {
                Text(
                    text = value,
                    color = valueColor,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 32.sp
                )
                bottomText?.let {
                    Text(
                        text = it,
                        color = if (containerColor == BrightLime) Background.copy(alpha = 0.7f) else OnSurfaceVariant,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun FeaturedWorkoutCard(
    title: String,
    duration: String,
    level: String,
    onLaunch: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable { onLaunch() },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // High contrast decorative element
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(180.dp)
                    .offset(x = 50.dp, y = 50.dp)
                    .clip(RoundedCornerShape(90.dp))
                    .background(
                        Brush.radialGradient(
                            colors = listOf(BrightBlue.copy(alpha = 0.3f), Color.Transparent)
                        )
                    )
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(28.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    InternalTag(
                        text = level.uppercase(),
                        containerColor = BrightBlue.copy(alpha = 0.15f),
                        contentColor = BrightBlue
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Timer,
                            contentDescription = null,
                            tint = OnSurfaceVariant,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = duration,
                            color = OnSurfaceVariant,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Column {
                    Text(
                        text = title,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = OnSurface,
                        lineHeight = 32.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        onClick = onLaunch,
                        shape = RoundedCornerShape(16.dp),
                        color = BrightLime,
                        modifier = Modifier.height(48.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.PlayArrow,
                                contentDescription = null,
                                tint = Background,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = stringResource(R.string.dashboard_action_start_now),
                                color = Background,
                                fontWeight = FontWeight.Black,
                                fontSize = 14.sp,
                                letterSpacing = 0.5.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VolumeChart(weeklyVolume: List<Double>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                text = stringResource(R.string.dashboard_chart_volume_title),
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp,
                color = OnSurfaceVariant
            )
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                val maxVolume = (weeklyVolume.maxOrNull() ?: 1.0).coerceAtLeast(1.0)
                weeklyVolume.forEach { volume ->
                    val heightFactor = (volume / maxVolume).toFloat().coerceIn(0.1f, 1f)
                    Box(
                        modifier = Modifier
                            .width(32.dp)
                            .fillMaxHeight(heightFactor)
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(BrightBlue, BrightBlue.copy(alpha = 0.3f))
                                )
                            )
                    )
                }
            }
        }
    }
}

@Composable
private fun InternalTag(text: String, containerColor: Color = Surface, contentColor: Color = OnSurfaceVariant) {
    Surface(
        color = containerColor,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.height(24.dp)
    ) {
        Box(modifier = Modifier.padding(horizontal = 8.dp), contentAlignment = Alignment.Center) {
            Text(text = text, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = contentColor)
        }
    }
}

@Composable
fun ActivityItem(title: String, time: String, icon: ImageVector, color: Color, onClick: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Surface)
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            Text(text = time, color = OnSurfaceVariant, fontSize = 12.sp)
        }
    }
}

@Composable
fun ChallengesSection(challenges: List<FruitChallenge>, onClaim: (String) -> Unit = {}) {
    Column(modifier = Modifier.fillMaxWidth()) {
        challenges.forEach { challenge ->
            ChallengeItem(challenge, onClaim)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun ChallengeItem(challenge: FruitChallenge, onClaim: (String) -> Unit = {}) {
    val icon = when(challenge.icon.toString()) {
        "APPLE" -> Icons.Default.Star 
        "BANANA" -> Icons.Default.FlashOn
        else -> Icons.Default.FitnessCenter
    }
    
    val color = if (challenge.isCompleted) BrightLime else BrightBlue

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(Surface)
            .border(
                BorderStroke(1.dp, if (challenge.isCompleted && !challenge.isClaimed) BrightLime.copy(alpha = 0.5f) else Color.Transparent),
                RoundedCornerShape(24.dp)
            )
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(if (challenge.isClaimed) OnSurfaceVariant.copy(alpha = 0.1f) else color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon, 
                contentDescription = null, 
                tint = if (challenge.isClaimed) OnSurfaceVariant else color, 
                modifier = Modifier.size(24.dp)
            )
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = challenge.title, 
                fontWeight = FontWeight.Black, 
                fontSize = 16.sp,
                color = if (challenge.isClaimed) OnSurfaceVariant else if (challenge.isCompleted) BrightLime else OnSurface
            )
            Text(
                text = challenge.description, 
                color = OnSurfaceVariant, 
                fontSize = 12.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            LinearProgressIndicator(
                progress = challenge.progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(6.dp)
                    .clip(CircleShape),
                color = if (challenge.isClaimed) OnSurfaceVariant else color,
                trackColor = color.copy(alpha = 0.1f)
            )
        }
        
        if (challenge.isCompleted && !challenge.isClaimed) {
            Spacer(modifier = Modifier.width(12.dp))
            Button(
                onClick = { onClaim(challenge.id) },
                colors = ButtonDefaults.buttonColors(containerColor = BrightLime),
                contentPadding = PaddingValues(horizontal = 12.dp),
                modifier = Modifier.height(32.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(stringResource(R.string.challenge_reward_claim), fontSize = 10.sp, fontWeight = FontWeight.Black, color = Background)
            }
        } else if (challenge.isClaimed) {
            Spacer(modifier = Modifier.width(12.dp))
            Icon(
                Icons.Default.CheckCircle, 
                contentDescription = null, 
                tint = OnSurfaceVariant,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
