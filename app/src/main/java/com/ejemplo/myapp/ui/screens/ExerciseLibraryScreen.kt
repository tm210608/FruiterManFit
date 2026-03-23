package com.ejemplo.myapp.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ejemplo.myapp.ui.theme.*
import com.ejemplo.myapp.ui.viewmodels.ExerciseLibraryViewModel
import com.ejemplo.myapp.ui.components.Tag

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseLibraryScreen(
    onExerciseClick: (String) -> Unit = {},
    viewModel: ExerciseLibraryViewModel = viewModel()
) {
    val exercises by viewModel.exercises.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedFilter by viewModel.selectedFilter.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 24.dp)
    ) {
        Text(
            text = "EXPLORE\nLIBRARY",
            fontSize = 36.sp,
            fontWeight = FontWeight.Black,
            lineHeight = 34.sp,
            modifier = Modifier.padding(top = 32.dp, bottom = 24.dp)
        )
        
        // Search Bar
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
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = BrightLime
                ),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = OnSurfaceVariant) },
                singleLine = true
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Filter Chips
        val filters = listOf("All", "Chest", "Legs", "Back", "Arms", "Abs")
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
                            text = filter,
                            color = if (isSelected) Background else OnSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Exercise Grid
        Column(
            modifier = Modifier.verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (exercises.isEmpty()) {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                    Text("No exercises found", color = OnSurfaceVariant)
                }
            }

            val chunkedExercises = exercises.chunked(2)
            chunkedExercises.forEach { pair ->
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    pair.forEach { exercise ->
                        SmallExerciseCard(
                            modifier = Modifier.weight(1f), 
                            title = exercise.name, 
                            level = exercise.level, 
                            color = exercise.accentColor ?: BrightBlue,
                            onClick = { onExerciseClick(exercise.id) }
                        )
                    }
                    if (pair.size == 1) Spacer(Modifier.weight(1f))
                }
            }
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
fun SmallExerciseCard(
    modifier: Modifier = Modifier,
    title: String,
    level: String,
    color: Color,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .height(200.dp)
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(32.dp)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp)
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.FitnessCenter, contentDescription = null, tint = color.copy(alpha = 0.5f), modifier = Modifier.size(48.dp))
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, fontWeight = FontWeight.Black, fontSize = 15.sp, lineHeight = 18.sp)
                Text(text = level, color = OnSurfaceVariant, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
