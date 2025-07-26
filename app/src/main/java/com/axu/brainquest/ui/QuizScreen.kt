package com.axu.brainquest.ui

import android.text.Html
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.axu.brainquest.Screen
import com.axu.brainquest.ui.components.BrainQuestButton
import com.axu.brainquest.ui.components.BrainQuestCard
import com.axu.brainquest.ui.components.ErrorMessage
import com.axu.brainquest.ui.components.LoadingScreen
import com.axu.brainquest.ui.components.QuizOptionButton
import com.axu.brainquest.ui.theme.ErrorRed
import com.axu.brainquest.ui.theme.SuccessGreen
import com.axu.brainquest.ui.theme.WarningOrange
import kotlinx.coroutines.launch

@Composable
fun QuizScreen(navController: NavController, quizViewModel: QuizViewModel = viewModel()) {
    val state by quizViewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    if (state.loading) {
        LoadingScreen(message = "Loading quiz questions...")
        return
    }
    
    if (state.error != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            ErrorMessage(
                message = state.error!!,
                onRetry = { quizViewModel.fetchQuestions() }
            )
        }
        return
    }
    
    if (state.questions.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            BrainQuestCard {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "No Quiz Selected",
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Please go back to set up your quiz",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    BrainQuestButton(
                        onClick = { navController.navigate(Screen.QuizSetup.route) },
                        text = "Set Up Quiz",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        return
    }
    
    val question = state.questions[state.currentIndex]
    val options = remember(question) {
        (question.incorrectAnswers + question.correctAnswer).shuffled()
    }
    val selected = state.userAnswers[state.currentIndex]
    val isLast = state.currentIndex == state.questions.lastIndex
    val progress = (state.currentIndex + 1).toFloat() / state.questions.size

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.surface
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
            // Progress Section
            BrainQuestCard {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Question ${state.currentIndex + 1}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${state.questions.size} Total",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
                
                Spacer(modifier = Modifier.height(12.dp))
                
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f),
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = "Score: ${state.score}/${state.questions.size}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            // Question Card
            BrainQuestCard {
                // Category and Difficulty
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = question.category,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    Surface(
                        color = when (question.difficulty.lowercase()) {
                            "easy" -> SuccessGreen.copy(alpha = 0.1f)
                            "medium" -> WarningOrange.copy(alpha = 0.1f)
                            "hard" -> ErrorRed.copy(alpha = 0.1f)
                            else -> MaterialTheme.colorScheme.surface
                        },
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = question.difficulty.replaceFirstChar { it.uppercase() },
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = when (question.difficulty.lowercase()) {
                                "easy" -> SuccessGreen
                                "medium" -> WarningOrange
                                "hard" -> ErrorRed
                                else -> MaterialTheme.colorScheme.onSurface
                            },
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(20.dp))
                
                Text(
                    text = Html.fromHtml(question.question, Html.FROM_HTML_MODE_LEGACY).toString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            // Options
            options.forEachIndexed { index, option ->
                val isSelected = selected == option
                val isCorrect = if (selected.isNotEmpty()) {
                    option == question.correctAnswer
                } else null
                
                QuizOptionButton(
                    text = Html.fromHtml(option, Html.FROM_HTML_MODE_LEGACY).toString(),
                    onClick = { quizViewModel.answerCurrentQuestion(option) },
                    modifier = Modifier.padding(bottom = 12.dp),
                    isSelected = isSelected,
                    isCorrect = isCorrect,
                    enabled = true // Allow answer changes
                )
            }
            
            // Next/Finish Button
            if (selected.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                
                BrainQuestButton(
                    onClick = {
                        if (isLast) {
                            scope.launch {
                                val category = if (state.questions.isNotEmpty()) state.questions[0].category else "General Knowledge"
                                saveQuizResultToFirestore(state.score, state.questions.size, category)
                                navController.navigate(Screen.Result.route) {
                                    popUpTo(Screen.Quiz.route) { inclusive = true }
                                }
                            }
                        } else {
                            quizViewModel.nextQuestion()
                        }
                    },
                    text = if (isLast) "Finish Quiz" else "Next Question",
                    icon = if (isLast) Icons.Default.CheckCircle else Icons.AutoMirrored.Filled.NavigateNext,
                    modifier = Modifier.fillMaxWidth()
                )
                
                // Show answer explanation (if available)
                if (selected == question.correctAnswer) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        color = SuccessGreen.copy(alpha = 0.1f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SuccessGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Correct! Well done! 🎉",
                                style = MaterialTheme.typography.bodyMedium,
                                color = SuccessGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                } else if (selected.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Surface(
                        color = ErrorRed.copy(alpha = 0.1f),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                text = "Incorrect. The correct answer is:",
                                style = MaterialTheme.typography.bodyMedium,
                                color = ErrorRed,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = Html.fromHtml(question.correctAnswer, Html.FROM_HTML_MODE_LEGACY).toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

fun saveQuizResultToFirestore(score: Int, totalQuestions: Int = 10, category: String = "General Knowledge") {
    val user = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser ?: return
    val db = com.google.firebase.firestore.FirebaseFirestore.getInstance()
    val data = mapOf(
        "score" to score,
        "totalQuestions" to totalQuestions,
        "category" to category,
        "timestamp" to com.google.firebase.Timestamp.now()
    )
    db.collection("users").document(user.uid)
        .collection("scores").add(data)
        .addOnFailureListener { e ->
            // Log error but don't crash
            e.printStackTrace()
        }
}
