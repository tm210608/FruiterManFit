package com.ejemplo.myapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ejemplo.myapp.ui.components.MainHeader
import com.ejemplo.myapp.ui.theme.*

@Composable
fun SocialScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(horizontal = 24.dp)
    ) {
        MainHeader(title = "COMMUNITY", subtitle = "Fruity Feed")
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(listOf(
                SocialPost("Alex Berry", "Just smashed my morning citrus shred! 🍋", "2h ago", BrightBlue),
                SocialPost("Kiwi Ken", "New PR on Bench Press: 100kg! 🥝", "5h ago", BrightLime),
                SocialPost("Mango Mary", "Who's up for a group run tomorrow? 🥭", "8h ago", BrightPink)
            )) { post ->
                PostCard(post)
            }
        }
    }
}

data class SocialPost(val user: String, val content: String, val time: String, val accentColor: Color)

@Composable
fun PostCard(post: SocialPost) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Surface),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(post.accentColor),
                    contentAlignment = Alignment.Center
                ) {
                    Text(post.user.first().toString(), fontWeight = FontWeight.Black, color = Background)
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(post.user, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Text(post.time, fontSize = 12.sp, color = OnSurfaceVariant)
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = post.content, fontSize = 15.sp, color = OnSurface)
            
            Spacer(modifier = Modifier.height(20.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = OnSurfaceVariant, modifier = Modifier.size(20.dp))
                Icon(Icons.Default.Message, contentDescription = null, tint = OnSurfaceVariant, modifier = Modifier.size(20.dp))
                Icon(Icons.Default.Share, contentDescription = null, tint = OnSurfaceVariant, modifier = Modifier.size(20.dp))
            }
        }
    }
}
