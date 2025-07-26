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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.axu.brainquest.ui.components.BrainQuestButton
import com.axu.brainquest.ui.components.BrainQuestCard
import com.axu.brainquest.ui.components.BrainQuestOutlinedButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizSetupScreen(navController: NavController, quizViewModel: QuizViewModel = viewModel()) {
    val uiState by quizViewModel.uiState.collectAsState()

    // Preserve selections across recompositions
    var selectedCategory by rememberSaveable { mutableStateOf(QuizCategory.ANY) }
    var selectedDifficulty by rememberSaveable { mutableStateOf(QuizDifficulty.ANY) }
    var selectedType by rememberSaveable { mutableStateOf(QuizType.ANY) }
    var questionCount by rememberSaveable { mutableIntStateOf(10) }
    
    var categoryExpanded by remember { mutableStateOf(false) }
    var difficultyExpanded by remember { mutableStateOf(false) }
    var typeExpanded by remember { mutableStateOf(false) }

    // Navigate to quiz when questions are loaded successfully
    LaunchedEffect(uiState.questions.size, uiState.loading) {
        if (uiState.questions.isNotEmpty() && !uiState.loading) {
            navController.navigate("quiz") {
                popUpTo("quiz_setup") { inclusive = true }
            }
        }
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
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            // Header with back button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Quiz Setup",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Customize your quiz experience",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                modifier = Modifier.padding(bottom = 32.dp)
            )

            // Question Count Section
            BrainQuestCard {
                Text(
                    text = "Number of Questions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf(5, 10, 15, 20).forEach { count ->
                        FilterChip(
                            onClick = { questionCount = count },
                            label = { Text("$count") },
                            selected = questionCount == count,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Category Section
            BrainQuestCard {
                Text(
                    text = "Category",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                ExposedDropdownMenuBox(
                    expanded = categoryExpanded,
                    onExpandedChange = { categoryExpanded = !categoryExpanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedCategory.displayName,
                        onValueChange = { },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = categoryExpanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = categoryExpanded,
                        onDismissRequest = { categoryExpanded = false }
                    ) {
                        QuizCategory.entries.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.displayName) },
                                onClick = {
                                    selectedCategory = category
                                    categoryExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Difficulty Section
            BrainQuestCard {
                Text(
                    text = "Difficulty",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                ExposedDropdownMenuBox(
                    expanded = difficultyExpanded,
                    onExpandedChange = { difficultyExpanded = !difficultyExpanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedDifficulty.displayName,
                        onValueChange = { },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = difficultyExpanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = difficultyExpanded,
                        onDismissRequest = { difficultyExpanded = false }
                    ) {
                        QuizDifficulty.entries.forEach { difficulty ->
                            DropdownMenuItem(
                                text = { Text(difficulty.displayName) },
                                onClick = {
                                    selectedDifficulty = difficulty
                                    difficultyExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            // Type Section
            BrainQuestCard {
                Text(
                    text = "Question Type",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                ExposedDropdownMenuBox(
                    expanded = typeExpanded,
                    onExpandedChange = { typeExpanded = !typeExpanded }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = selectedType.displayName,
                        onValueChange = { },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(
                                expanded = typeExpanded
                            )
                        },
                        colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = typeExpanded,
                        onDismissRequest = { typeExpanded = false }
                    ) {
                        QuizType.entries.forEach { type ->
                            DropdownMenuItem(
                                text = { Text(type.displayName) },
                                onClick = {
                                    selectedType = type
                                    typeExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))

            // Start Quiz Button
            BrainQuestButton(
                onClick = {
                    quizViewModel.resetQuiz() // Reset previous quiz state
                    val config = QuizConfig(
                        amount = questionCount,
                        category = selectedCategory,
                        difficulty = selectedDifficulty,
                        type = selectedType
                    )
                    quizViewModel.fetchQuestions(config)
                    // TODO: Navigation will be handled by observing uiState.questions
                },
                text = if (uiState.loading) "Loading Quiz..." else "Start Quiz",
                icon = Icons.Default.PlayArrow,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.loading
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Quick Start Option
            BrainQuestOutlinedButton(
                onClick = {
                    quizViewModel.resetQuiz()
                    val config = QuizConfig(
                        amount = 10,
                        category = selectedCategory,
                        difficulty = selectedDifficulty,
                        type = selectedType
                    )
                    quizViewModel.fetchQuestions(config)
                    // TODO: Navigation will be handled by observing uiState.questions
                },
                text = "Start with Current Settings",
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
