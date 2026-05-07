package com.ejemplo.myapp.ui.screens

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
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.res.stringResource
import com.ejemplo.myapp.R
import com.ejemplo.myapp.ui.theme.*
import com.ejemplo.myapp.ui.utils.ExerciseTranslator
import com.ejemplo.myapp.ui.utils.translateCategory
import com.ejemplo.myapp.ui.viewmodels.ExerciseLibraryViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseLibraryScreen(
    onExerciseClick: (String) -> Unit = {},
    onAddExercise: (String) -> Unit = {},
    viewModel: ExerciseLibraryViewModel
) {
    val exercises by viewModel.exercises.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    var selectedExercise by remember { mutableStateOf<com.ejemplo.myapp.data.models.Exercise?>(null) }
    val sheetState = rememberModalBottomSheetState()
    var showSheet by remember { mutableStateOf(false) }

    val myApiKey = "e039faa7c4msh8c3b03cd185d65dp10ef44jsnc97b003b7753"
    
    // USAR EL ImageLoader GLOBAL que soporta GIFs
    val context = LocalContext.current
    val imageLoader = (context.applicationContext as? coil.ImageLoaderFactory)?.newImageLoader() 
        ?: coil.ImageLoader.Builder(context).build()

    if (showSheet && selectedExercise != null) {
        ModalBottomSheet(
            onDismissRequest = { showSheet = false },
            sheetState = sheetState,
            containerColor = Surface,
            dragHandle = { BottomSheetDefaults.DragHandle(color = OnSurfaceVariant.copy(alpha = 0.4f)) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(bottom = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    modifier = Modifier.size(200.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    if (selectedExercise!!.gifUrl.isNotEmpty()) {
                        SubcomposeAsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(selectedExercise!!.gifUrl.replace("http://", "https://"))
                                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36")
                                .crossfade(true)
                                .build(),
                            imageLoader = imageLoader,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                            loading = {
                                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                    CircularProgressIndicator(color = selectedExercise!!.accentColor ?: BrightLime)
                                }
                            },
                            error = {
                                Log.e("FruiterMan", "Error cargando GIF: ${selectedExercise!!.gifUrl}")
                                ExercisePlaceholder(selectedExercise!!.bodyPart, selectedExercise!!.equipment, selectedExercise!!.accentColor ?: BrightBlue)
                            }
                        )
                    } else {
                        ExercisePlaceholder(selectedExercise!!.bodyPart, selectedExercise!!.equipment, selectedExercise!!.accentColor ?: BrightBlue)
                    }
                }
// ... resto del ModalBottomSheet
                
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = ExerciseTranslator.translateExerciseName(selectedExercise!!.name).uppercase(),
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Text(translateCategory(selectedExercise!!.bodyPart).uppercase(), color = BrightLime, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                
                Spacer(modifier = Modifier.height(32.dp))
                
                Button(
                    onClick = {
                        showSheet = false
                        onExerciseClick(selectedExercise!!.id)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Surface),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = BrightBlue)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(stringResource(R.string.library_action_view_instructions), color = OnSurface, fontWeight = FontWeight.Bold)
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                Button(
                    onClick = {
                        showSheet = false
                        onAddExercise(selectedExercise!!.id)
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BrightLime),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Background)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(stringResource(R.string.library_action_add_to_workout), color = Background, fontWeight = FontWeight.Black)
                }
            }
        }
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
                    text = stringResource(R.string.library_title),
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Black,
                    lineHeight = 34.sp
                )
                Text(
                    text = stringResource(R.string.library_subtitle, exercises.size),
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
                placeholder = { Text("Buscar ejercicios...", color = OnSurfaceVariant, fontSize = 14.sp) },
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
        
        val filters = listOf(
            stringResource(R.string.library_filter_all),
            stringResource(R.string.category_waist),
            stringResource(R.string.category_chest),
            stringResource(R.string.category_back),
            stringResource(R.string.category_cardio),
            stringResource(R.string.category_upper_arms),
            stringResource(R.string.category_lower_arms),
            stringResource(R.string.category_upper_legs),
            stringResource(R.string.category_lower_legs),
            stringResource(R.string.category_shoulders)
        )
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
                            text = (if(filter == "Todo") "TODO" else filter).uppercase(),
                            color = if (isSelected) Background else OnSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (exercises.isEmpty() && !isLoading) {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                Text("No se encontraron ejercicios. Pulsa el botón de actualizar arriba.", color = OnSurfaceVariant, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
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
                                title = ExerciseTranslator.translateExerciseName(exercise.name),
                                category = exercise.bodyPart, 
                                gifUrl = exercise.gifUrl,
                                color = exercise.accentColor ?: BrightBlue,
                                onClick = { 
                                    selectedExercise = exercise
                                    showSheet = true
                                }
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
fun ExercisePlaceholder(category: String, equipment: String, color: Color) {
    val icon = when {
        equipment.contains("dumbbell") -> Icons.Default.FitnessCenter
        equipment.contains("barbell") -> Icons.Default.FitnessCenter
        equipment.contains("cable") -> Icons.Default.SyncAlt
        equipment.contains("bench") -> Icons.Default.Chair
        equipment.contains("body weight") -> Icons.Default.DirectionsRun
        else -> Icons.Default.FitnessCenter
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            icon,
            contentDescription = null,
            tint = color.copy(alpha = 0.2f),
            modifier = Modifier.size(64.dp)
        )
        Text(
            translateCategory(category).uppercase(),
            color = color.copy(alpha = 0.4f),
            fontWeight = FontWeight.Black,
            fontSize = 14.sp
        )
        Text(
            equipment.uppercase(),
            color = color.copy(alpha = 0.2f),
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp
        )
    }
}

@Composable
fun SmallExerciseCard(
    modifier: Modifier = Modifier,
    title: String,
    category: String,
    equipment: String = "body weight",
    gifUrl: String,
    color: Color,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    // Obtener el loader de la aplicación que ya tiene configurado el GifDecoder
    val imageLoader = (context.applicationContext as? coil.ImageLoaderFactory)?.newImageLoader() 
        ?: coil.ImageLoader.Builder(context).build()

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
                            .data(gifUrl.replace("http://", "https://"))
                            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/119.0.0.0 Safari/537.36")
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
                                    color = color.copy(alpha = 0.5f),
                                    strokeWidth = 2.dp
                                )
                            }
                        },
                        error = {
                            ExercisePlaceholder(category, equipment, color)
                        }
                    )
                } else {
                    ExercisePlaceholder(category, equipment, color)
                }
            }
// ... resto del Column
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
                    text = translateCategory(category).uppercase(),
                    color = color, 
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
            }
        }
    }
}
