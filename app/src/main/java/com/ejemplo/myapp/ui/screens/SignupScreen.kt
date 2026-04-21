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
                        text = stringResource(R.string.signup_title), 
                        fontSize = 24.sp, 
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(32.dp))
                
                var name by remember { mutableStateOf("") }
                var email by remember { mutableStateOf("") }
                var password by remember { mutableStateOf("") }
                var error by remember { mutableStateOf<String?>(null) }

                AppTextField(label = stringResource(R.string.signup_name_label), placeholder = "John Appleseed", value = name, onValueChange = { name = it })
                Spacer(modifier = Modifier.height(16.dp))
                AppTextField(label = stringResource(R.string.login_email_label), placeholder = "fruity@man.com", value = email, onValueChange = { email = it })
                Spacer(modifier = Modifier.height(16.dp))
                AppTextField(label = stringResource(R.string.login_password_label), placeholder = "••••••••", value = password, onValueChange = { password = it }, isPassword = true)
                
                error?.let {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = it, color = Color.Red, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(32.dp))
                
                AppButton(
                    text = stringResource(R.string.signup_button), 
                    onClick = {
                        if (name.isBlank() || email.isBlank() || password.length < 6) {
                            error = "Please fill all fields (Password min 6 chars)"
                        } else {
                            viewModel.register(name, email, password) { success ->
                                if (success) onSignupSuccess()
                                else error = "Error creating account"
                            }
                        }
                    },
                    containerColor = BrightPink
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = stringResource(R.string.signup_disclaimer),
                    fontSize = 10.sp,
                    color = OnSurfaceVariant,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }
    }
}
