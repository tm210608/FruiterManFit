package com.ejemplo.myapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejemplo.myapp.ui.theme.*

@Composable
fun ProfileStat(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Black, color = OnSurface)
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariant, letterSpacing = 1.sp)
    }
}

@Composable
fun RecapDivider() {
    Box(
        modifier = Modifier
            .height(30.dp)
            .width(1.dp)
            .background(Color.White.copy(alpha = 0.1f))
    )
}

@Composable
fun ProgressChart(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        val path = Path().apply {
            moveTo(0f, height * 0.8f)
            cubicTo(width * 0.2f, height * 0.8f, width * 0.4f, height * 0.2f, width * 0.6f, height * 0.4f)
            cubicTo(width * 0.8f, height * 0.6f, width * 0.9f, 0f, width, height * 0.2f)
        }
        drawPath(
            path = path,
            color = BrightLime,
            style = Stroke(width = 4.dp.toPx())
        )
    }
}
