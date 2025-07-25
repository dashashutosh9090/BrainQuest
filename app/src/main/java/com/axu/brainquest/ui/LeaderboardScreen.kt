package com.axu.brainquest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.axu.brainquest.ui.components.BrainQuestCard
import com.axu.brainquest.ui.components.ErrorMessage
import com.axu.brainquest.ui.components.LoadingScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class LeaderboardEntry(
    val uid: String = "",
    val name: String = "",
    val email: String = "",
    val totalQuizzes: Int = 0,
    val averageScore: Double = 0.0,
    val bestScore: Int = 0,
    val totalScore: Int = 0
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaderboardScreen(navController: NavController) {
    var loading by remember { mutableStateOf(true) }
    var leaderboardData by remember { mutableStateOf<List<LeaderboardEntry>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var selectedTab by remember { mutableIntStateOf(0) }
    
    val currentUser = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()
    
    // Load leaderboard data
    LaunchedEffect(selectedTab) {
        try {
            loading = true
            error = null
            
            // Get all users
            val usersQuery = db.collection("users").get().await()
            val leaderboardEntries = mutableListOf<LeaderboardEntry>()
            
            for (userDoc in usersQuery.documents) {
                val userData = userDoc.data ?: continue
                val userId = userDoc.id
                
                // Get user's quiz scores
                val scoresQuery = db.collection("users")
                    .document(userId)
                    .collection("scores")
                    .get()
                    .await()
                
                val scores = scoresQuery.documents.map { doc ->
                    doc.getLong("score")?.toInt() ?: 0
                }
                
                // Add all users, even those with no scores
                val totalQuizzes = scores.size
                val totalScore = scores.sum()
                val averageScore = if (scores.isNotEmpty()) scores.average() else 0.0
                val bestScore = scores.maxOrNull() ?: 0
                
                leaderboardEntries.add(
                    LeaderboardEntry(
                        uid = userId,
                        name = userData["name"] as? String ?: "Anonymous",
                        email = userData["email"] as? String ?: "",
                        totalQuizzes = totalQuizzes,
                        averageScore = averageScore,
                        bestScore = bestScore,
                        totalScore = totalScore
                    )
                )
            }
            
            // Sort based on selected tab
            leaderboardData = when (selectedTab) {
                0 -> leaderboardEntries.sortedByDescending { it.totalScore } // Total Score
                1 -> leaderboardEntries.sortedByDescending { it.averageScore } // Average Score
                else -> leaderboardEntries.sortedByDescending { it.bestScore } // Best Score
            }
            
            loading = false
            
        } catch (e: Exception) {
            error = e.localizedMessage ?: "Failed to load leaderboard"
            loading = false
        }
    }
    
    Column(modifier = Modifier.fillMaxSize()) {
        // Top App Bar
        TopAppBar(
            title = { 
                Text(
                    text = "Leaderboard",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )
        
        if (loading) {
            LoadingScreen(message = "Loading leaderboard...")
            return
        }
        
        if (error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                ErrorMessage(
                    message = error!!,
                    onRetry = {
                        loading = true
                        error = null
                    }
                )
            }
            return
        }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                            MaterialTheme.colorScheme.background
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp)
            ) {
                // Tab Selection
                BrainQuestCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = Color.Transparent,
                        contentColor = MaterialTheme.colorScheme.primary
                    ) {
                        Tab(
                            selected = selectedTab == 0,
                            onClick = { selectedTab = 0 },
                            text = {
                                Text(
                                    "Total Score",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        )
                        Tab(
                            selected = selectedTab == 1,
                            onClick = { selectedTab = 1 },
                            text = {
                                Text(
                                    "Average",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        )
                        Tab(
                            selected = selectedTab == 2,
                            onClick = { selectedTab = 2 },
                            text = {
                                Text(
                                    "Best Score",
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                
                if (leaderboardData.isEmpty()) {
                    BrainQuestCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.EmojiEvents,
                                contentDescription = null,
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "No data available",
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center
                            )
                            Text(
                                text = "Take some quizzes to see rankings!",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(leaderboardData) { index, entry ->
                            LeaderboardItem(
                                rank = index + 1,
                                entry = entry,
                                isCurrentUser = entry.uid == currentUser?.uid,
                                selectedTab = selectedTab
                            )
                        }
                        
                        // Add some bottom padding
                        item { Spacer(modifier = Modifier.height(24.dp)) }
                    }
                }
            }
        }
    }
}

@Composable
fun LeaderboardItem(
    rank: Int,
    entry: LeaderboardEntry,
    isCurrentUser: Boolean,
    selectedTab: Int
) {
    val rankColor = when (rank) {
        1 -> Color(0xFFFFD700) // Gold
        2 -> Color(0xFFC0C0C0) // Silver
        3 -> Color(0xFFCD7F32) // Bronze
        else -> MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    }
    
    val scoreValue = when (selectedTab) {
        0 -> "${entry.totalScore}"
        1 -> "${entry.averageScore.toInt()}%"
        else -> "${entry.bestScore}"
    }
    
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentUser) {
                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
            } else {
                MaterialTheme.colorScheme.surface
            }
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isCurrentUser) 8.dp else 4.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Rank
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (rank <= 3) rankColor.copy(alpha = 0.2f)
                        else MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (rank <= 3) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = null,
                        tint = rankColor,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text(
                        text = "$rank",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // User Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // User Info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.name + if (isCurrentUser) " (You)" else "",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${entry.totalQuizzes} quizzes completed",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
            
            // Score
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = scoreValue,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = when (rank) {
                        1 -> Color(0xFFFFD700)
                        2 -> Color(0xFFC0C0C0)
                        3 -> Color(0xFFCD7F32)
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
                Text(
                    text = when (selectedTab) {
                        0 -> "points"
                        1 -> "average"
                        else -> "best"
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}
