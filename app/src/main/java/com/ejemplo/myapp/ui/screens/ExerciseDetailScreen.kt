package com.ejemplo.myapp.ui.screens

import android.os.Build
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.ejemplo.myapp.data.models.Exercise
import com.ejemplo.myapp.data.repository.FitnessRepository
import androidx.compose.ui.res.stringResource
import com.ejemplo.myapp.R
import com.ejemplo.myapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    exerciseId: String,
    repository: FitnessRepository,
    onBack: () -> Unit,
    onStartExercise: (String) -> Unit
) {
    var exercise by remember { mutableStateOf<Exercise?>(null) }
    
    LaunchedEffect(exerciseId) {
        exercise = repository.getExerciseById(exerciseId)
    }

    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (Build.VERSION.SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .build()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Exercise Details", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background,
                    titleContentColor = OnSurface,
                    navigationIconContentColor = OnSurface
                )
            )
        },
        containerColor = Background
    ) { padding ->
        exercise?.let { ex ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // GIF Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    if (ex.gifUrl.isNotEmpty()) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(ex.gifUrl)
                                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36")
                                .crossfade(true)
                                .build(),
                            imageLoader = imageLoader,
                            contentDescription = ex.name,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                            onLoading = { Log.d("FruiterMan", "Cargando GIF detalle: ${ex.gifUrl}") },
                            onError = { error ->
                                Log.e("FruiterMan", "Error cargando GIF detalle: ${error.result.throwable.message}")
                            }
                        )
                    } else {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    Icons.Default.FitnessCenter,
                                    contentDescription = null,
                                    tint = (ex.accentColor ?: BrightLime).copy(alpha = 0.2f),
                                    modifier = Modifier.size(120.dp)
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    ex.bodyPart.uppercase(),
                                    color = (ex.accentColor ?: BrightLime),
                                    fontWeight = FontWeight.Black,
                                    fontSize = 24.sp,
                                    letterSpacing = 2.sp
                                )
                                Text(
                                    "No Preview Available",
                                    color = OnSurfaceVariant.copy(alpha = 0.4f),
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 12.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Dificultad y Categoría (Nuevos campos)
                if (ex.difficulty.isNotEmpty() || ex.category.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (ex.difficulty.isNotEmpty()) {
                            DifficultyBadge(ex.difficulty)
                        }
                        if (ex.category.isNotEmpty()) {
                            CategoryBadge(ex.category)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Title & Category
                Text(
                    text = ex.name.uppercase(),
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Black,
                    color = OnSurface,
                    lineHeight = 32.sp
                )
                
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        color = ex.accentColor ?: BrightBlue,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = ex.bodyPart.uppercase(),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Background
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Surface(
                        color = Surface,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, OnSurfaceVariant.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = ex.equipment.uppercase(),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = OnSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (ex.description.isNotEmpty()) {
                    Text(
                        text = ex.description,
                        fontSize = 14.sp,
                        color = OnSurfaceVariant,
                        lineHeight = 20.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                Text(
                    text = stringResource(R.string.detail_target, ex.target.uppercase()),
                    color = BrightLime,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )

                Divider(modifier = Modifier.padding(vertical = 24.dp), color = Surface)

                // Instructions
                Text(
                    text = stringResource(R.string.detail_instructions_title),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = BrightLime
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                ex.instructions.forEachIndexed { index, step ->
                    Row(modifier = Modifier.padding(bottom = 16.dp)) {
                        Surface(
                            modifier = Modifier.size(24.dp),
                            color = Surface,
                            shape = CircleShape
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = (index + 1).toString(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = OnSurface
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = step,
                            fontSize = 15.sp,
                            color = OnSurface.copy(alpha = 0.8f),
                            lineHeight = 22.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(80.dp)) // Espacio para el botón flotante
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = BrightLime)
            Text(
                text = stringResource(R.string.detail_loading),
                modifier = Modifier.padding(top = 80.dp),
                color = OnSurfaceVariant
            )
        }
    }

    // Botón flotante para añadir al entrenamiento
    exercise?.let { ex ->
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = { onStartExercise(ex.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BrightLime),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Background)
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.detail_action_start),
                    color = Background,
                    fontWeight = FontWeight.Black
                )
            }
        }
    }
}

@Composable
fun DifficultyBadge(difficulty: String) {
    val color = when(difficulty.lowercase()) {
        "beginner" -> Color(0xFF4CAF50)
        "intermediate" -> Color(0xFFFF9800)
        "expert", "advanced" -> Color(0xFFF44336)
        else -> BrightLime
    }
    
    Surface(
        color = color.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Text(
            text = difficulty.uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
    }
}

@Composable
fun CategoryBadge(category: String) {
    Surface(
        color = BrightBlue.copy(alpha = 0.1f),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, BrightBlue.copy(alpha = 0.5f))
    ) {
        Text(
            text = category.uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = BrightBlue
        )
    }
}
