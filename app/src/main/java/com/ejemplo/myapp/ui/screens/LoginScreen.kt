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
import androidx.compose.ui.res.stringResource
import com.ejemplo.myapp.R
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.ejemplo.myapp.ui.components.*
import com.ejemplo.myapp.ui.theme.*

import androidx.hilt.navigation.compose.hiltViewModel
import com.ejemplo.myapp.ui.viewmodels.UserViewModel

@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    onSignupClick: () -> Unit,
    viewModel: UserViewModel = hiltViewModel()
) {
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
                Text(text = stringResource(R.string.auth_slogan), color = OnSurfaceVariant, fontSize = 14.sp)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Text(
                    text = stringResource(R.string.login_welcome), 
                    fontSize = 24.sp, 
                    fontWeight = FontWeight.Bold, 
                    modifier = Modifier.align(Alignment.Start)
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Fields
                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }

                AppTextField(label = stringResource(R.string.login_email_label), placeholder = "fruity@man.com", value = email, onValueChange = { email = it })
                Spacer(modifier = Modifier.height(16.dp))
                AppTextField(label = stringResource(R.string.login_password_label), placeholder = "••••••••", value = password, onValueChange = { password = it }, isPassword = true)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                AppButton(
                    text = stringResource(R.string.login_button), 
                    onClick = {
                        viewModel.login(email, password) { success ->
                            if (success) onLoginSuccess()
                        }
                    },
                    containerColor = BrightLime
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                TextButton(onClick = onSignupClick) {
                    Row {
                        Text(stringResource(R.string.login_signup_prompt), color = OnSurfaceVariant)
                        Text(stringResource(R.string.login_signup_action), fontWeight = FontWeight.Black, color = BrightBlue)
                    }
                }
            }
        }
    }
}

@Composable
fun AppTextField(
    label: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BrightLime, letterSpacing = 1.sp)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text(text = placeholder, color = OnSurfaceVariant) },
            singleLine = true,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = {
                if (isPassword) {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            contentDescription = if (passwordVisible) "Hide password" else "Show password",
                            tint = OnSurfaceVariant
                        )
                    }
                }
            },
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White.copy(alpha = 0.05f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.05f),
                focusedBorderColor = BrightLime,
                unfocusedBorderColor = Color.White.copy(alpha = 0.1f)
            )
        )
    }
}
