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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.axu.brainquest.Screen
import com.axu.brainquest.ui.components.BrainQuestButton
import com.axu.brainquest.ui.components.BrainQuestCard
import com.axu.brainquest.ui.components.BrainQuestOutlinedButton
import com.axu.brainquest.ui.components.ErrorMessage
import com.axu.brainquest.ui.components.LoadingScreen
import com.axu.brainquest.ui.theme.ErrorRed
import com.axu.brainquest.ui.theme.SuccessGreen
import com.axu.brainquest.ui.theme.WarningOrange
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

data class UserProfile(
    val name: String = "",
    val email: String = "",
    val uid: String = "",
    val totalQuizzes: Int = 0,
    val averageScore: Double = 0.0,
    val bestScore: Int = 0,
    val joinDate: String = ""
)

data class QuizResult(
    val score: Int = 0,
    val totalQuestions: Int = 10,
    val timestamp: com.google.firebase.Timestamp? = null,
    val category: String = "General Knowledge"
)

@Composable
fun ProfileScreen(navController: NavController) {
    var loading by remember { mutableStateOf(true) }
    var logoutLoading by remember { mutableStateOf(false) }
    var userProfile by remember { mutableStateOf<UserProfile?>(null) }
    var recentQuizzes by remember { mutableStateOf<List<QuizResult>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    
    val currentUser = FirebaseAuth.getInstance().currentUser
    val db = FirebaseFirestore.getInstance()
    
    // Load user data
    LaunchedEffect(currentUser) {
        if (currentUser != null) {
            try {
                loading = true
                error = null
                
                // Get user profile
                val userDoc = db.collection("users").document(currentUser.uid).get().await()
                val userData = userDoc.data
                
                // Get user's quiz scores
                val scoresQuery = db.collection("users")
                    .document(currentUser.uid)
                    .collection("scores")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .limit(10)
                    .get()
                    .await()
                
                val quizResults = scoresQuery.documents.map { doc ->
                    QuizResult(
                        score = doc.getLong("score")?.toInt() ?: 0,
                        totalQuestions = doc.getLong("totalQuestions")?.toInt() ?: 10,
                        timestamp = doc.getTimestamp("timestamp"),
                        category = doc.getString("category") ?: "General Knowledge"
                    )
                }
                
                // Calculate statistics
                val totalQuizzes = quizResults.size
                val averageScore = if (totalQuizzes > 0) {
                    quizResults.map { it.score.toDouble() / it.totalQuestions * 100 }.average()
                } else 0.0
                val bestScore = quizResults.maxOfOrNull { it.score } ?: 0
                
                userProfile = UserProfile(
                    name = userData?.get("name") as? String ?: currentUser.displayName ?: "User",
                    email = currentUser.email ?: "No email",
                    uid = currentUser.uid,
                    totalQuizzes = totalQuizzes,
                    averageScore = averageScore,
                    bestScore = bestScore,
                    joinDate = "Member since ${java.text.SimpleDateFormat("MMM yyyy", java.util.Locale.getDefault()).format(currentUser.metadata?.creationTimestamp?.let { java.util.Date(it) } ?: java.util.Date())}"
                )
                
                recentQuizzes = quizResults
                loading = false
                
            } catch (e: Exception) {
                error = e.localizedMessage ?: "Failed to load profile"
                loading = false
            }
        } else {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Profile.route) { inclusive = true }
            }
        }
    }
    
    if (loading) {
        LoadingScreen(message = "Loading profile...")
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
    
    val profile = userProfile ?: return
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.colorScheme.background
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Profile Header
            BrainQuestCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Profile Avatar
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(60.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = profile.name,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = profile.email,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    
                    Text(
                        text = profile.joinDate,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Statistics
            BrainQuestCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Quiz Statistics",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${profile.totalQuizzes}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Quizzes",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${profile.averageScore.toInt()}%",
                            style = MaterialTheme.typography.headlineMedium,
                            color = SuccessGreen,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Average",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                    
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "${profile.bestScore}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Best Score",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Recent Activity
            if (recentQuizzes.isNotEmpty()) {
                BrainQuestCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Recent Activity",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    recentQuizzes.take(5).forEachIndexed { index, quiz ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                                        shape = CircleShape
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Quiz,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            
                            Spacer(modifier = Modifier.width(12.dp))
                            
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = quiz.category,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                val timeAgo = quiz.timestamp?.let {
                                    val diff = System.currentTimeMillis() - it.toDate().time
                                    when {
                                        diff < 3600000 -> "${diff / 60000} minutes ago"
                                        diff < 86400000 -> "${diff / 3600000} hours ago"
                                        else -> "${diff / 86400000} days ago"
                                    }
                                } ?: "Recently"
                                
                                Text(
                                    text = "Score: ${quiz.score}/${quiz.totalQuestions} • $timeAgo",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                                )
                            }
                            
                            val percentage = (quiz.score.toFloat() / quiz.totalQuestions * 100).toInt()
                            Surface(
                                color = when {
                                    percentage >= 80 -> SuccessGreen.copy(alpha = 0.1f)
                                    percentage >= 60 -> WarningOrange.copy(alpha = 0.1f)
                                    else -> ErrorRed.copy(alpha = 0.1f)
                                },
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                            ) {
                                Text(
                                    text = "$percentage%",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = when {
                                        percentage >= 80 -> SuccessGreen
                                        percentage >= 60 -> WarningOrange
                                        else -> ErrorRed
                                    },
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        
                        if (index < recentQuizzes.size - 1 && index < 4) {
                            HorizontalDivider(
                                modifier = Modifier.padding(vertical = 4.dp),
                                thickness = DividerDefaults.Thickness, color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BrainQuestOutlinedButton(
                    onClick = { navController.navigate(Screen.Home.route) },
                    text = "Home",
                    icon = Icons.Default.Home,
                    modifier = Modifier.weight(1f)
                )
                
                BrainQuestOutlinedButton(
                    onClick = { 
                        // Navigate to leaderboard - we'll implement this next
                        // For now, just show a message
                    },
                    text = "Leaderboard",
                    icon = Icons.AutoMirrored.Filled.TrendingUp,
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Logout Button
            BrainQuestButton(
                onClick = {
                    logoutLoading = true
                    FirebaseAuth.getInstance().signOut()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                    logoutLoading = false
                },
                text = "Sign Out",
                icon = Icons.AutoMirrored.Filled.ExitToApp,
                isLoading = logoutLoading,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
