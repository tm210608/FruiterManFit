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
import com.ejemplo.myapp.ui.theme.*
import com.ejemplo.myapp.ui.viewmodels.WorkoutSessionViewModel
import com.ejemplo.myapp.ui.components.AppButton

@Composable
fun WorkoutLogScreen(
    viewModel: WorkoutSessionViewModel,
    onFinish: () -> Unit,
    onAddExercise: () -> Unit
) {
    val activeExercises by viewModel.activeExercises.collectAsState()
    var restTimeLeft by remember { mutableLongStateOf(0L) }
    var isTimerActive by remember { mutableStateOf(false) }
    
    var showSummary by remember { mutableStateOf(false) }
    var summaryData by remember { mutableStateOf<Triple<Long, Int, Double>?>(null) }

    LaunchedEffect(isTimerActive) {
        if (isTimerActive) {
            while (restTimeLeft > 0) {
                kotlinx.coroutines.delay(1000)
                restTimeLeft -= 1000
            }
            isTimerActive = false
        }
    }

    if (showSummary && summaryData != null) {
        WorkoutSummaryDialog(
            durationMillis = summaryData!!.first,
            calories = summaryData!!.second,
            totalVolume = summaryData!!.third,
            onDismiss = {
                showSummary = false
                viewModel.resetSession() // Limpiar ejercicios activos
                onFinish() // Volver a Home
            }
        )
    }

    // Timer logic for Duration display
    var elapsedTime by remember { mutableLongStateOf(0L) }
    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        while (true) {
            elapsedTime = System.currentTimeMillis() - startTime
            kotlinx.coroutines.delay(1000)
        }
    }

    val seconds = (elapsedTime / 1000) % 60
    val minutes = (elapsedTime / (1000 * 60)) % 60
    val hours = (elapsedTime / (1000 * 60 * 60))
    val timeString = if (hours > 0) String.format("%d:%02d:%02d", hours, minutes, seconds) else String.format("%02d:%02d", minutes, seconds)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF08080A))
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
    ) {
        // ... (Header stays the same)
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
                Text("Sesión Activa", fontWeight = FontWeight.Black, fontSize = 20.sp)
                Text("Quema de Músculo Mañanera 🔥", color = BrightBlue, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(timeString, fontWeight = FontWeight.Black, fontSize = 24.sp, color = OnSurface)
                Text("DURACIÓN", fontSize = 9.sp, fontWeight = FontWeight.Black, color = OnSurfaceVariant)
            }
        }

        if (restTimeLeft > 0) {
            RestTimerComponent(
                timeLeftMillis = restTimeLeft,
                onCancel = { 
                    restTimeLeft = 0
                    isTimerActive = false
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        
        activeExercises.forEach { activeExercise ->
            ExerciseSessionCard(
                title = activeExercise.name,
                subtitle = activeExercise.subtitle,
                accentColor = activeExercise.accentColor,
                sets = activeExercise.sets,
                onAddSet = { viewModel.addSet(activeExercise.exerciseId) },
                onRemoveSet = { setNumber -> viewModel.removeSet(activeExercise.exerciseId, setNumber) },
                onUpdateSet = { setNumber, weight, reps, isDone ->
                    viewModel.updateSet(activeExercise.exerciseId, setNumber, weight, reps, isDone)
                    if (isDone) {
                        restTimeLeft = 60000L // 60 seconds rest
                        isTimerActive = true
                    }
                },
                onRemoveExercise = { viewModel.removeExercise(activeExercise.exerciseId) }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }
        
        if (activeExercises.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                Text(text = "¡Aún no hay ejercicios!", color = OnSurfaceVariant)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        
        AppButton(text = "+ AÑADIR EJERCICIO", onClick = onAddExercise, containerColor = Surface)
        Spacer(modifier = Modifier.height(16.dp))
        AppButton(
            text = "FINALIZAR ENTRENO", 
            onClick = {
                viewModel.finishWorkout(onComplete = { duration, calories, volume ->
                    summaryData = Triple(duration, calories, volume)
                    showSummary = true
                })
            }, 
            containerColor = BrightLime
        )

        Spacer(modifier = Modifier.height(120.dp))
    }
}

@Composable
fun WorkoutSummaryDialog(
    durationMillis: Long,
    calories: Int,
    totalVolume: Double,
    onDismiss: () -> Unit
) {
    val seconds = (durationMillis / 1000) % 60
    val minutes = (durationMillis / (1000 * 60)) % 60
    val hours = (durationMillis / (1000 * 60 * 60))

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Surface,
        shape = RoundedCornerShape(32.dp),
        modifier = Modifier.padding(16.dp),
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("¡Buen Trabajo!", fontSize = 24.sp, fontWeight = FontWeight.Black, color = BrightLime)
                Text("Entrenamiento Completado 🍏", fontSize = 14.sp, color = OnSurfaceVariant)
                Spacer(modifier = Modifier.height(8.dp))
                if (totalVolume > 0) {
                    Text(
                        "Has movido un total de ${totalVolume.toInt()} kg",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = BrightBlue,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        text = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryStat(
                    value = if (hours > 0) String.format("%d:%02d:%02d", hours, minutes, seconds) else String.format("%02d:%02d", minutes, seconds),
                    label = "TIEMPO",
                    icon = Icons.Default.Timer
                )
                SummaryStat(
                    value = String.format("%.0f", totalVolume),
                    label = "VOLUMEN (KG)",
                    icon = Icons.Default.FitnessCenter
                )
                SummaryStat(
                    value = calories.toString(),
                    label = "CALORÍAS",
                    icon = Icons.Default.LocalFireDepartment
                )
            }
        },
        confirmButton = {
            AppButton(
                text = "CONTINUAR",
                onClick = onDismiss,
                containerColor = BrightLime,
                modifier = Modifier.padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
            )
        }
    )
}

@Composable
fun SummaryStat(value: String, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = null, tint = OnSurfaceVariant, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(8.dp))
        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = OnSurface)
        Text(label, fontSize = 9.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariant, letterSpacing = 1.sp)
    }
}

@Composable
fun RestTimerComponent(timeLeftMillis: Long, onCancel: () -> Unit) {
    val seconds = (timeLeftMillis / 1000) % 60
    val minutes = (timeLeftMillis / (1000 * 60)) % 60
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = BrightBlue.copy(alpha = 0.1f)),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, BrightBlue.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Refresh, contentDescription = null, tint = BrightBlue)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("TIEMPO DE DESCANSO", fontSize = 10.sp, fontWeight = FontWeight.Black, color = BrightBlue)
                    Text(
                        text = String.format("%02d:%02d", minutes, seconds),
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Black,
                        color = OnSurface
                    )
                }
            }
            IconButton(onClick = onCancel) {
                Icon(Icons.Default.Close, contentDescription = "Cancel", tint = OnSurfaceVariant)
            }
        }
    }
}

@Composable
fun ExerciseSessionCard(
    title: String, 
    subtitle: String, 
    accentColor: Color, 
    sets: List<com.ejemplo.myapp.data.models.SessionSet>,
    onAddSet: () -> Unit,
    onRemoveSet: (Int) -> Unit,
    onUpdateSet: (Int, String, String, Boolean) -> Unit,
    onRemoveExercise: () -> Unit
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
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.Black, color = accentColor)
                    Text(text = subtitle, fontSize = 12.sp, color = OnSurfaceVariant)
                }
                var showMenu by remember { mutableStateOf(false) }
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(Icons.Default.MoreVert, contentDescription = null, tint = OnSurfaceVariant)
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(Surface)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Quitar Ejercicio", color = Color.Red) },
                            onClick = {
                                showMenu = false
                                onRemoveExercise()
                            },
                            leadingIcon = { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Header
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                HeaderText("SET", Modifier.width(40.dp))
                HeaderText("KG", Modifier.weight(1f))
                HeaderText("REPS", Modifier.weight(1f))
                HeaderText("HECHO", Modifier.width(60.dp))
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            sets.forEach { set ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.weight(1f)) {
                        EditableSetRow(set, accentColor) { weight, reps, isDone ->
                            onUpdateSet(set.number, weight, reps, isDone)
                        }
                    }
                    IconButton(
                        onClick = { onRemoveSet(set.number) },
                        modifier = Modifier.size(32.dp).padding(start = 4.dp)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Remove Set", tint = OnSurfaceVariant.copy(alpha = 0.5f), modifier = Modifier.size(16.dp))
                    }
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
                Text("+ AÑADIR SET", fontWeight = FontWeight.Black, color = OnSurface.copy(alpha = 0.6f), fontSize = 12.sp)
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
