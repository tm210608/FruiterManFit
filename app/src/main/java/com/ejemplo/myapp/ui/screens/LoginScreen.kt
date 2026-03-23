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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejemplo.myapp.ui.components.*
import com.ejemplo.myapp.ui.theme.*

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onSignupClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrightBlue)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(containerColor = Background)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo Section
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(BrightLime)
                        .border(4.dp, Color.White, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Restaurant, contentDescription = null, tint = BrightBlue, modifier = Modifier.size(48.dp))
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "FruiterMan Fit", fontSize = 32.sp, fontWeight = FontWeight.Black, color = OnSurface, fontStyle = androidx.compose.ui.text.font.FontStyle.Italic)
                Text(text = "Get Juicy. Get Fit.", color = OnSurfaceVariant, fontSize = 14.sp)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = "Welcome back", 
                    fontSize = 24.sp, 
                    fontWeight = FontWeight.Bold, 
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Fields (Mocked)
                AppTextField(label = "EMAIL ADDRESS", placeholder = "fruity@man.com")
                Spacer(modifier = Modifier.height(16.dp))
                AppTextField(label = "PASSWORD", placeholder = "••••••••", isPassword = true)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                AppButton(
                    text = "GO!", 
                    onClick = onLoginSuccess,
                    containerColor = BrightLime
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                TextButton(onClick = onSignupClick) {
                    Row {
                        Text("New fruit on the tree? ", color = OnSurfaceVariant)
                        Text("Sign up", fontWeight = FontWeight.Black, color = BrightBlue)
                    }
                }
            }
        }
    }
}

@Composable
fun AppTextField(label: String, placeholder: String, isPassword: Boolean = false) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BrightLime, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            color = Color.White.copy(alpha = 0.05f),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
        ) {
            Box(modifier = Modifier.padding(horizontal = 16.dp), contentAlignment = Alignment.CenterStart) {
                Text(text = placeholder, color = OnSurfaceVariant)
            }
        }
    }
}
