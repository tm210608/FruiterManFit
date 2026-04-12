package com.ejemplo.myapp.ui.screens

import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.SubcomposeAsyncImage
import coil.decode.GifDecoder
import coil.decode.ImageDecoderDecoder
import coil.request.ImageRequest
import com.ejemplo.myapp.ui.theme.*
import com.ejemplo.myapp.ui.viewmodels.ExerciseLibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseLibraryScreen(
    onExerciseClick: (String) -> Unit = {},
    viewModel: ExerciseLibraryViewModel
) {
    val exercises by viewModel.exercises.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val myApiKey = "e039faa7c4msh8c3b03cd185d65dp10ef44jsnc97b003b7753"

    // IMPORTANTE: Optimizamos el cargador de GIFs para que no se recree
    val context = LocalContext.current
    val imageLoader = remember {
        ImageLoader.Builder(context)
            .components {
                if (SDK_INT >= 28) {
                    add(ImageDecoderDecoder.Factory())
                } else {
                    add(GifDecoder.Factory())
                }
            }
            .crossfade(true)
            .build()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 24.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp, bottom = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "EXPLORE\nLIBRARY",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 34.sp
                )
                // Contador para saber si hay datos en la DB
                Text(
                    text = "${exercises.size} Exercises Loaded",
                    color = BrightLime,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            
            IconButton(
                onClick = { 
                    Log.d("FruiterMan", "Refreshing exercises...")
                    viewModel.refreshExercises(myApiKey) 
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(Surface, RoundedCornerShape(12.dp))
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = BrightLime, strokeWidth = 2.dp)
                } else {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = OnSurface)
                }
            }
        }

        if (isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                color = BrightLime,
                trackColor = Surface
            )
        }
        
        Surface(
            modifier = Modifier.fillMaxWidth().height(56.dp),
            color = Surface,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.05f))
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                placeholder = { Text("Search exercises...", color = OnSurfaceVariant, fontSize = 14.sp) },
                modifier = Modifier.fillMaxSize(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = BrightLime
                ),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = OnSurfaceVariant) },
                singleLine = true
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        val filters = listOf("All", "waist", "chest", "back", "cardio", "upper arms", "lower arms", "upper legs", "lower legs", "shoulders")
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filters) { filter ->
                val isSelected = filter == selectedFilter
                Surface(
                    color = if (isSelected) BrightLime else Surface,
                    shape = RoundedCornerShape(20.dp),
                    modifier = Modifier
                        .height(40.dp)
                        .clickable { viewModel.onFilterSelected(filter) }
                ) {
                    Box(modifier = Modifier.padding(horizontal = 20.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = filter.uppercase(),
                            color = if (isSelected) Background else OnSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // El grid de ejercicios optimizado con LazyColumn
        if (exercises.isEmpty() && !isLoading) {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                Text("No exercises found. Tap the Refresh button above.", color = OnSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
            }
        } else {
            val chunkedExercises = remember(exercises) { exercises.chunked(2) }
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 120.dp)
            ) {
                items(chunkedExercises) { pair ->
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        pair.forEach { exercise ->
                            SmallExerciseCard(
                                modifier = Modifier.weight(1f), 
                                title = exercise.name, 
                                category = exercise.bodyPart, 
                                gifUrl = exercise.gifUrl,
                                imageLoader = imageLoader,
                                color = exercise.accentColor ?: BrightBlue,
                                onClick = { onExerciseClick(exercise.id) }
                            )
                        }
                        if (pair.size == 1) Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun SmallExerciseCard(
    modifier: Modifier = Modifier,
    title: String,
    category: String,
    gifUrl: String,
    imageLoader: ImageLoader,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(230.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                if (gifUrl.isNotEmpty()) {
                    SubcomposeAsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(gifUrl)
                            .crossfade(true)
                            .build(),
                        imageLoader = imageLoader,
                        contentDescription = title,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Fit,
                        loading = {
                            Box(
                                modifier = Modifier.fillMaxSize().background(Color.LightGray.copy(alpha = 0.1f)),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = BrightLime.copy(alpha = 0.5f),
                                    strokeWidth = 2.dp
                                )
                            }
                        },
                        error = {
                            Box(
                                modifier = Modifier.fillMaxSize().background(Color.LightGray.copy(alpha = 0.05f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.BrokenImage,
                                    contentDescription = "Error loading image",
                                    tint = OnSurfaceVariant.copy(alpha = 0.3f),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    )
                } else {
                    Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = Background.copy(alpha = 0.2f), modifier = Modifier.size(48.dp))
                }
            }
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = title, 
                    fontWeight = FontWeight.Black, 
                    fontSize = 13.sp, 
                    lineHeight = 15.sp,
                    maxLines = 2,
                    color = OnSurface
                )
                Text(
                    text = category.uppercase(), 
                    color = color, 
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
