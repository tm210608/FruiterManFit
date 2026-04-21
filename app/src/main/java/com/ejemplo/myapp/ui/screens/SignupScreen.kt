package com.ejemplo.myapp.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejemplo.myapp.ui.components.*
import com.ejemplo.myapp.ui.theme.*

import androidx.hilt.navigation.compose.hiltViewModel
import com.ejemplo.myapp.ui.viewmodels.UserViewModel

@Composable
fun SignupScreen(
    onSignupSuccess: () -> Unit,
    onBack: () -> Unit,
    viewModel: UserViewModel = hiltViewModel()
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BrightPink)
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
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = BrightLime)
                    }
                    Text(
                        text = "Join the Tree", 
                        fontSize = 24.sp, 
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                var name by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                AppTextField(label = "FULL NAME", placeholder = "John Appleseed", value = name, onValueChange = { name = it })
                Spacer(modifier = Modifier.height(16.dp))
                AppTextField(label = "EMAIL", placeholder = "fruity@man.com", value = email, onValueChange = { email = it })
                Spacer(modifier = Modifier.height(16.dp))
                AppTextField(label = "PASSWORD", placeholder = "••••••••", value = password, onValueChange = { password = it }, isPassword = true)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                AppButton(
                    text = "PLANT MY SEED", 
                    onClick = {
                        viewModel.register(name, email, password) { success ->
                            if (success) onSignupSuccess()
                        }
                    },
                    containerColor = BrightPink
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "By signing up, you agree to become 100% natural fruit.",
                    fontSize = 10.sp,
                    color = OnSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
