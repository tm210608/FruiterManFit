package com.ejemplo.myapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejemplo.myapp.R
import com.ejemplo.myapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionLogScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nutrition_title), fontWeight = FontWeight.Black, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, tint = BrightLime)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background)
            )
        },
        containerColor = Background,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Add meal dialog */ },
                containerColor = BrightLime,
                contentColor = Background,
                shape = CircleShape
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 24.dp)
        ) {
            // Summary Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                shape = RoundedCornerShape(32.dp),
                colors = CardDefaults.cardColors(containerColor = Surface)
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = stringResource(R.string.nutrition_target_calories, 2200),
                        color = OnSurfaceVariant,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Box(contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(
                            progress = 0.65f,
                            modifier = Modifier.size(160.dp),
                            strokeWidth = 12.dp,
                            color = BrightLime,
                            trackColor = Color.White.copy(alpha = 0.05f)
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "1430", fontSize = 36.sp, fontWeight = FontWeight.Black, color = OnSurface)
                            Text(text = "kcal", fontSize = 14.sp, color = OnSurfaceVariant)
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                        NutritionStat(stringResource(R.string.nutrition_consumed), "1430", BrightBlue)
                        NutritionStat(stringResource(R.string.nutrition_remaining), "770", BrightLime)
                    }
                }
            }

            Text(
                text = "MEALS",
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 2.sp,
                color = OnSurfaceVariant,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // Example List
            val meals = listOf(
                MealItem(stringResource(R.string.nutrition_meal_breakfast), "Apple & Oats", 350),
                MealItem(stringResource(R.string.nutrition_meal_lunch), "Grilled Chicken & Rice", 650),
                MealItem(stringResource(R.string.nutrition_meal_snack), "Greek Yogurt", 150),
                MealItem(stringResource(R.string.nutrition_meal_dinner), "Salmon Salad", 280)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(meals) { meal ->
                    MealRow(meal)
                }
                item { Spacer(modifier = Modifier.height(100.dp)) }
            }
        }
    }
}

@Composable
fun NutritionStat(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = OnSurfaceVariant)
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Black, color = color)
    }
}

@Composable
fun MealRow(meal: MealItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(Surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(BrightLime.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Restaurant, contentDescription = null, tint = BrightLime, modifier = Modifier.size(20.dp))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = meal.type.uppercase(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = BrightLime)
            Text(text = meal.name, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = OnSurface)
        }
        Text(text = "${meal.calories} kcal", fontWeight = FontWeight.Black, fontSize = 14.sp, color = OnSurface)
    }
}

data class MealItem(val type: String, val name: String, val calories: Int)
