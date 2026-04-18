package com.ejemplo.myapp.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejemplo.myapp.ui.theme.*

@Composable
fun MainHeader(title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = title,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                color = BrightBlue,
                letterSpacing = (-1).sp
            )
            Text(
                text = subtitle,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = OnSurfaceVariant,
                letterSpacing = 2.sp
            )
        }
        
        // Organic Avatar Shape
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(RoundedCornerShape(bottomStart = 16.dp, topEnd = 16.dp, topStart = 6.dp, bottomEnd = 6.dp))
                .background(BrightLime)
                .padding(2.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(bottomStart = 14.dp, topEnd = 14.dp, topStart = 4.dp, bottomEnd = 4.dp))
                    .background(Background),
                contentAlignment = Alignment.Center
            ) {
                Text("FM", color = BrightLime, fontWeight = FontWeight.Black)
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, action: String = "", onActionClicked: () -> Unit = {}) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Black,
            color = OnSurface,
            letterSpacing = (-0.5).sp
        )
        if (action.isNotEmpty()) {
            TextButton(onClick = onActionClicked, contentPadding = PaddingValues(0.dp)) {
                Text(
                    text = action.uppercase(),
                    color = BrightBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun Tag(text: String, containerColor: Color = Color.White.copy(alpha = 0.1f), contentColor: Color = OnSurface) {
    Surface(
        color = containerColor,
        shape = CircleShape
    ) {
        Text(
            text = text.uppercase(),
            color = contentColor,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun AppButton(
    text: String, 
    onClick: () -> Unit, 
    modifier: Modifier = Modifier,
    containerColor: Color = BrightBlue
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        colors = ButtonDefaults.buttonColors(containerColor = containerColor),
        shape = RoundedCornerShape(16.dp)
    ) {
        Text(
            text = text.uppercase(),
            color = if (containerColor == BrightLime) Background else OnSurface,
            fontWeight = FontWeight.Black,
            letterSpacing = (-0.5).sp
        )
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
