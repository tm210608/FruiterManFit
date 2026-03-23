package com.ejemplo.myapp.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ejemplo.myapp.ui.theme.*
import com.ejemplo.myapp.ui.viewmodels.WorkoutSessionViewModel
import com.ejemplo.myapp.ui.components.AppButton

@Composable
fun WorkoutLogScreen(
    viewModel: WorkoutSessionViewModel = viewModel(),
    onFinish: () -> Unit,
    onAddExercise: () -> Unit
) {
    val activeExercises by viewModel.activeExercises.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF08080A))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        // Session Header
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(BrightLime.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text("🍏", fontSize = 24.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Active Session", fontWeight = FontWeight.Black, fontSize = 20.sp)
                Text("Morning Muscle Burn 🔥", color = BrightBlue, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("00:42:15", fontWeight = FontWeight.Black, fontSize = 24.sp, color = OnSurface)
                Text("DURATION", fontSize = 9.sp, fontWeight = FontWeight.Black, color = OnSurfaceVariant)
            }
        }
        
        activeExercises.forEach { activeExercise ->
            ExerciseSessionCard(
                title = activeExercise.name,
                subtitle = activeExercise.subtitle,
                accentColor = activeExercise.accentColor,
                sets = activeExercise.sets,
                onAddSet = { viewModel.addSet(activeExercise.exerciseId) },
                onUpdateSet = { setNumber, weight, reps, isDone ->
                    viewModel.updateSet(activeExercise.exerciseId, setNumber, weight, reps, isDone)
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        
        if (activeExercises.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                Text(text = "No exercises yet!", color = OnSurfaceVariant)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        AppButton(text = "+ ADD EXERCISE", onClick = onAddExercise, containerColor = Surface)
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(text = "FINISH WORKOUT", onClick = onFinish, containerColor = BrightLime)

        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
fun ExerciseSessionCard(
    title: String, 
    subtitle: String, 
    accentColor: Color, 
    sets: List<com.ejemplo.myapp.data.models.SessionSet>,
    onAddSet: () -> Unit,
    onUpdateSet: (Int, String, String, Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(listOf(accentColor.copy(alpha = 0.5f), Color.Transparent), endX = 400f),
                shape = RoundedCornerShape(32.dp)
            ),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column {
                    Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Black, color = accentColor)
                    Text(text = subtitle, fontSize = 12.sp, color = OnSurfaceVariant)
                }
                IconButton(onClick = {}) {
                    Icon(Icons.Default.MoreVert, contentDescription = null, tint = OnSurfaceVariant)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Header
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                HeaderText("SET", Modifier.width(40.dp))
                HeaderText("KG", Modifier.weight(1f))
                HeaderText("REPS", Modifier.weight(1f))
                HeaderText("DONE", Modifier.width(60.dp))
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            sets.forEach { set ->
                EditableSetRow(set, accentColor) { weight, reps, isDone ->
                    onUpdateSet(set.number, weight, reps, isDone)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            OutlinedButton(
                onClick = onAddSet,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("+ ADD SET", fontWeight = FontWeight.Black, color = OnSurface.copy(alpha = 0.6f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun HeaderText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontSize = 9.sp,
        fontWeight = FontWeight.Black,
        color = OnSurfaceVariant,
        letterSpacing = 1.sp,
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

@Composable
fun EditableSetRow(
    set: com.ejemplo.myapp.data.models.SessionSet, 
    accentColor: Color,
    onChanged: (String, String, Boolean) -> Unit
) {
    val rowBg = if (set.isDone) Color.White.copy(alpha = 0.03f) else accentColor.copy(alpha = 0.05f)
    val border = if (set.isDone) null else BorderStroke(1.dp, accentColor.copy(alpha = 0.2f))
    
    Surface(
        color = rowBg,
        shape = RoundedCornerShape(12.dp),
        border = border,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Set Number
            Text(
                text = set.number.toString(), 
                modifier = Modifier.width(40.dp), 
                textAlign = TextAlign.Center, 
                fontWeight = FontWeight.Black, 
                color = accentColor
            )

            // Weight Field
            SetInputField(
                value = set.weight,
                onValueChange = { onChanged(it, set.reps, set.isDone) },
                modifier = Modifier.weight(1f)
            )

            // Reps Field
            SetInputField(
                value = set.reps,
                onValueChange = { onChanged(set.weight, it, set.isDone) },
                modifier = Modifier.weight(1f)
            )

            // Done Checkbox
            Box(modifier = Modifier.width(60.dp), contentAlignment = Alignment.Center) {
                Surface(
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onChanged(set.weight, set.reps, !set.isDone) },
                    color = if (set.isDone) BrightBlue else Color.Transparent,
                    shape = RoundedCornerShape(8.dp),
                    border = BorderStroke(2.dp, if (set.isDone) BrightBlue else accentColor.copy(alpha = 0.3f))
                ) {
                    if (set.isDone) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Background, modifier = Modifier.padding(4.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SetInputField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = Modifier) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier.padding(horizontal = 4.dp),
        textStyle = TextStyle(
            color = OnSurface,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        ),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        cursorBrush = SolidColor(BrightLime),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White.copy(alpha = 0.05f), RoundedCornerShape(8.dp))
                    .padding(vertical = 8.dp),
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        }
    )
}
