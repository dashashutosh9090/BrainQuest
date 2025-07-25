package com.axu.brainquest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.rounded.EmojiEvents
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.axu.brainquest.Screen
import com.axu.brainquest.ui.components.*
import com.axu.brainquest.ui.theme.*
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ResultScreen(navController: NavController, quizViewModel: QuizViewModel = viewModel()) {
    val state by quizViewModel.uiState.collectAsState()

    val score = state.score
    val totalQuestions = state.questions.size.takeIf { it > 0 } ?: 10

    // Moved percentage calculation here
    val percentage = (score.toFloat() / totalQuestions * 100).toInt()

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
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            
            // Achievement Icon based on performance
            val achievementIcon = when {
                percentage >= 90 -> Icons.Rounded.EmojiEvents
                percentage >= 70 -> Icons.Rounded.Psychology
                else -> Icons.Rounded.Psychology
            }
            
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(
                        brush = Brush.radialGradient(
                            colors = when {
                                percentage >= 90 -> listOf(
                                    androidx.compose.ui.graphics.Color(0xFFFFD700),
                                    androidx.compose.ui.graphics.Color(0xFFFFA500)
                                )
                                percentage >= 70 -> listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                                else -> listOf(
                                    MaterialTheme.colorScheme.outline,
                                    MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
                                )
                            }
                        ),
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = achievementIcon,
                    contentDescription = "Achievement",
                    modifier = Modifier.size(40.dp),
                    tint = androidx.compose.ui.graphics.Color.White
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Quiz Complete!",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Great job on completing the quiz",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            ScoreCard(
                score = score,
                totalQuestions = totalQuestions,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Performance Analysis
            BrainQuestCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Performance Analysis",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$score",
                            style = MaterialTheme.typography.headlineMedium,
                            color = SuccessGreen,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Correct",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "${totalQuestions - score}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = ErrorRed,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Incorrect",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$percentage%",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Accuracy",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            BrainQuestButton(
                onClick = {
                    quizViewModel.resetQuiz()
                    navController.navigate(Screen.Quiz.route) {
                        popUpTo(Screen.Result.route) { inclusive = true }
                    }
                },
                text = "Take Another Quiz",
                icon = Icons.Default.PlayArrow,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

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
                        // Share functionality placeholder
                        navController.navigate(Screen.Profile.route)
                    },
                    text = "Share",
                    icon = Icons.Default.Share,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Motivational message (now has access to percentage)
            BrainQuestCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                val motivationalMessage = when {
                    percentage >= 90 -> "Outstanding! You're a quiz master! 🏆"
                    percentage >= 80 -> "Excellent work! Keep it up! 🌟"
                    percentage >= 70 -> "Great job! You're doing well! 👍"
                    percentage >= 60 -> "Good effort! Practice makes perfect! 📚"
                    else -> "Don't give up! Every expert was once a beginner! 💪"
                }

                Text(
                    text = motivationalMessage,
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
