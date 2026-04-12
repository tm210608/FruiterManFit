package com.ejemplo.myapp.ui.screens

import android.os.Build
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.ejemplo.myapp.data.models.Exercise
import com.ejemplo.myapp.data.repository.FitnessRepository
import com.ejemplo.myapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    exerciseId: String,
    repository: FitnessRepository,
    onBack: () -> Unit
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
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(ex.gifUrl)
                            .crossfade(true)
                            .build(),
                        imageLoader = imageLoader,
                        contentDescription = ex.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Target: ${ex.target}",
                        color = OnSurfaceVariant,
                        fontSize = 14.sp
                    )
                }

                Divider(modifier = Modifier.padding(vertical = 24.dp), color = Surface)

                // Instructions
                Text(
                    text = "INSTRUCTIONS",
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
                
                Spacer(modifier = Modifier.height(32.dp))
            }
        } ?: Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = BrightLime)
        }
    }
}
