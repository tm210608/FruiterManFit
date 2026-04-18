package com.ejemplo.myapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejemplo.myapp.data.local.entities.WorkoutSessionEntity
import com.ejemplo.myapp.data.repository.FitnessRepository
import com.ejemplo.myapp.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    repository: FitnessRepository,
    onBack: () -> Unit
) {
    val sessions by repository.getAllSessions().collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout History", fontWeight = FontWeight.Black) },
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
        if (sessions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No sessions recorded yet", color = OnSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(sessions) { session ->
                    HistoryItem(session)
                }
            }
        }
    }
}

@Composable
fun HistoryItem(session: WorkoutSessionEntity) {
    val dateStr = remember(session.startTime) {
        SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(Date(session.startTime))
    }
    val durationStr = remember(session.duration) {
        val mins = session.duration / 60000
        "${mins}m"
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Surface)
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(BrightBlue.copy(alpha = 0.1f), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = BrightBlue)
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(text = session.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = dateStr, color = OnSurfaceVariant, fontSize = 12.sp)
            }
            
            Column(horizontalAlignment = Alignment.End) {
                Text(text = "${session.totalCalories} kcal", fontWeight = FontWeight.Black, fontSize = 16.sp, color = BrightLime)
                Text(text = durationStr, color = OnSurfaceVariant, fontSize = 12.sp)
            }
        }
    }
}
