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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejemplo.myapp.ui.components.*
import com.ejemplo.myapp.ui.theme.*

@Composable
fun SettingsScreen(onBack: () -> Unit, onLogout: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.White.copy(alpha = 0.05f))
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = BrightLime, modifier = Modifier.size(20.dp))
            }
            Text(
                text = "Settings",
                fontSize = 24.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.weight(1f).padding(start = 16.dp)
            )
        }
        
        // Profile Organic Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(BorderStroke(1.dp, BrightLime.copy(alpha = 0.2f)), RoundedCornerShape(32.dp)),
            colors = CardDefaults.cardColors(containerColor = Surface),
            shape = RoundedCornerShape(32.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                // Avatar
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.05f))
                            .border(4.dp, BrightLime, CircleShape)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(modifier = Modifier.fillMaxSize(), shape = CircleShape, color = Color.Gray.copy(alpha = 0.3f)) {
                            Box(contentAlignment = Alignment.Center) {
                                Text("FM", fontSize = 24.sp, fontWeight = FontWeight.Black)
                            }
                        }
                    }
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(36.dp).clip(CircleShape).background(BrightLime)
                    ) {
                        Icon(Icons.Default.Edit, contentDescription = null, tint = Background, modifier = Modifier.size(18.dp))
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Fruit Master John", fontWeight = FontWeight.Black, fontSize = 20.sp)
                Text(text = "Member since May 2024", color = OnSurfaceVariant, fontSize = 12.sp)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                SettingsField("Full Name", "John Appleseed")
                Spacer(modifier = Modifier.height(16.dp))
                SettingsField("Bio", "Fueling my workouts with nature's candy! 🍎🍓", isTextArea = true)
                Spacer(modifier = Modifier.height(16.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SettingsField("Weight (kg)", "78", modifier = Modifier.weight(1f))
                    SettingsField("Height (cm)", "182", modifier = Modifier.weight(1f))
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Divider(color = Color.White.copy(alpha = 0.05f))
                SettingsToggle("Notifications", Icons.Default.Notifications, true)
                SettingsToggle("Dark Mode", Icons.Default.DarkMode, true)
                
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.SettingsAccessibility, contentDescription = null, tint = BrightLime, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text("Unit System", fontWeight = FontWeight.Black, fontSize = 14.sp)
                    }
                    Surface(
                        color = Color.White.copy(alpha = 0.05f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(modifier = Modifier.padding(2.dp)) {
                            Text(
                                text = "Metric",
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(BrightLime)
                                    .padding(horizontal = 12.dp, vertical = 6.dp),
                                color = Background,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "Imperial",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                color = OnSurfaceVariant,
                                fontWeight = FontWeight.Black,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        AppButton("Save Changes", {}, containerColor = BrightLime)
        
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            SecondarySettingsButton("Help", Icons.Default.Help, modifier = Modifier.weight(1f))
            SecondarySettingsButton("Terms", Icons.Default.Gavel, modifier = Modifier.weight(1f))
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().height(56.dp).background(Color.Red.copy(alpha = 0.1f), RoundedCornerShape(16.dp)),
            colors = ButtonDefaults.textButtonColors(contentColor = Color.Red)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Logout, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Log Out", fontWeight = FontWeight.Black)
            }
        }
        
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun SettingsField(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    isTextArea: Boolean = false
) {
    Column(modifier = modifier) {
        Text(text = label, color = BrightLime, fontSize = 11.sp, fontWeight = FontWeight.Black, modifier = Modifier.padding(start = 4.dp, bottom = 8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().height(if (isTextArea) 100.dp else 56.dp),
            color = Color.White.copy(alpha = 0.05f),
            shape = RoundedCornerShape(16.dp)
        ) {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = if (isTextArea) 16.dp else 0.dp), contentAlignment = if (isTextArea) Alignment.TopStart else Alignment.CenterStart) {
                Text(text = value, fontSize = 14.sp)
            }
        }
    }
}

@Composable
fun SettingsToggle(label: String, icon: ImageVector, checked: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = BrightLime, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = label, fontWeight = FontWeight.Black, fontSize = 14.sp)
        }
        Switch(
            checked = checked,
            onCheckedChange = {},
            colors = SwitchDefaults.colors(
                checkedThumbColor = Background,
                checkedTrackColor = BrightLime,
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.DarkGray
            )
        )
    }
}

@Composable
fun SecondarySettingsButton(label: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.height(56.dp),
        color = Surface,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = OnSurfaceVariant, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = label, fontWeight = FontWeight.Black, fontSize = 14.sp)
        }
    }
}
