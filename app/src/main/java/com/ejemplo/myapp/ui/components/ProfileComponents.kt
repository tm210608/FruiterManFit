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
fun ProgressChart(
    modifier: Modifier = Modifier,
    data: List<Double> = emptyList()
) {
    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height
        
        if (data.size < 2) {
            // Draw placeholder or empty state
            val path = Path().apply {
                moveTo(0f, height * 0.8f)
                cubicTo(width * 0.2f, height * 0.8f, width * 0.4f, height * 0.2f, width * 0.6f, height * 0.4f)
                cubicTo(width * 0.8f, height * 0.6f, width * 0.9f, 0f, width, height * 0.2f)
            }
            drawPath(
                path = path,
                color = BrightLime.copy(alpha = 0.3f),
                style = Stroke(width = 4.dp.toPx())
            )
            return@Canvas
        }

        val maxVal = (data.maxOrNull() ?: 1.0).coerceAtLeast(1.0)
        val path = Path()
        
        data.forEachIndexed { index, value ->
            val x = index * (width / (data.size - 1))
            val y = height - (value.toFloat() / maxVal.toFloat() * height * 0.8f) - (height * 0.1f)
            
            if (index == 0) {
                path.moveTo(x, y)
            } else {
                val prevX = (index - 1) * (width / (data.size - 1))
                val prevY = height - (data[index - 1].toFloat() / maxVal.toFloat() * height * 0.8f) - (height * 0.1f)
                
                path.cubicTo(
                    prevX + (x - prevX) / 2, prevY,
                    prevX + (x - prevX) / 2, y,
                    x, y
                )
            }
        }

        drawPath(
            path = path,
            color = BrightLime,
            style = Stroke(width = 4.dp.toPx())
        )
    }
}
