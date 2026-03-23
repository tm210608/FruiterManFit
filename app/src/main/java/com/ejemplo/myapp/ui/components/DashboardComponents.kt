package com.ejemplo.myapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejemplo.myapp.ui.theme.*

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
        colors = CardDefaults.cardColors(containerColor = containerColor)
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
                    text = label.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Black,
                    color = if (containerColor == BrightLime) Background.copy(alpha = 0.6f) else OnSurfaceVariant,
                    letterSpacing = 1.sp
                )
                icon?.let {
                    Icon(imageVector = it, contentDescription = null, tint = iconColor, modifier = Modifier.size(16.dp))
                }
            }
            
            Column {
                Text(
                    text = value,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = valueColor
                )
                bottomText?.let {
                    Text(
                        text = it,
                        fontSize = 10.sp,
                        color = if (containerColor == BrightLime) Background.copy(alpha = 0.6f) else OnSurfaceVariant,
                        fontWeight = FontWeight.Bold
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
            .height(200.dp)
            .clickable { onLaunch() },
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            // Simple decorative element
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(150.dp)
                    .offset(x = 40.dp, y = 40.dp)
                    .clip(RoundedCornerShape(75.dp))
                    .background(BrightBlue.copy(alpha = 0.1f))
            )
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Tag(text = level, containerColor = BrightBlue.copy(alpha = 0.1f), contentColor = BrightBlue)
                    Spacer(modifier = Modifier.width(8.dp))
                    Tag(text = duration)
                }
                
                Column {
                    Text(
                        text = title,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = OnSurface
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(BrightLime)
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Background, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("START NOW", color = Background, fontWeight = FontWeight.Black, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ActivityItem(title: String, time: String, icon: ImageVector, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Surface)
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
