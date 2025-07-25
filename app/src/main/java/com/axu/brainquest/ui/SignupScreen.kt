package com.axu.brainquest.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import com.axu.brainquest.ui.components.BrainQuestTextField
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignupScreen(navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var loading by remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()

    fun validateInputs(): Boolean {
        return when {
            name.isBlank() -> {
                error = "Name is required"
                false
            }
            name.length < 2 -> {
                error = "Name must be at least 2 characters"
                false
            }
            email.isBlank() -> {
                error = "Email is required"
                false
            }
            !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                error = "Please enter a valid email address"
                false
            }
            password.isBlank() -> {
                error = "Password is required"
                false
            }
            password.length < 6 -> {
                error = "Password must be at least 6 characters"
                false
            }
            confirmPassword != password -> {
                error = "Passwords do not match"
                false
            }
            else -> {
                error = null
                true
            }
        }
    }

    fun signUp() {
        if (!validateInputs()) return
        
        loading = true
        error = null
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    if (user != null) {
                        // Update user profile with display name
                        val profileUpdates = userProfileChangeRequest {
                            displayName = name
                        }
                        user.updateProfile(profileUpdates)
                        
                        // Save to Firestore
                        CoroutineScope(Dispatchers.IO).launch {
                            try {
                                val db = FirebaseFirestore.getInstance()
                                val userData = mapOf(
                                    "uid" to user.uid,
                                    "email" to user.email,
                                    "name" to name,
                                    "createdAt" to com.google.firebase.Timestamp.now()
                                )
                                db.collection("users").document(user.uid).set(userData)
                                    .addOnSuccessListener {
                                        loading = false
                                        navController.navigate(Screen.Home.route) {
                                            popUpTo(Screen.Signup.route) { inclusive = true }
                                        }
                                    }
                                    .addOnFailureListener { e ->
                                        loading = false
                                        error = e.localizedMessage ?: "Failed to save user info"
                                    }
                            } catch (e: Exception) {
                                loading = false
                                error = e.localizedMessage ?: "Failed to save user info"
                            }
                        }
                    }
                } else {
                    loading = false
                    error = task.exception?.localizedMessage ?: "Signup failed"
                }
            }
    }

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
        // Top App Bar
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent
            )
        )
        
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
                .padding(top = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // App Icon
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(
                        brush = Brush.linearGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary,
                                MaterialTheme.colorScheme.secondary
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Rounded.Psychology,
                    contentDescription = "BrainQuest Logo",
                    modifier = Modifier.size(40.dp),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Title Section
            Text(
                text = "Create Account",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            
            Text(
                text = "Join BrainQuest and start your learning journey",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
            )

            // Signup Form Card
            BrainQuestCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Sign Up",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                BrainQuestTextField(
                    value = name,
                    onValueChange = { 
                        name = it
                        error = null
                    },
                    label = "Full Name",
                    leadingIcon = Icons.Default.Person,
                    isError = error != null && (name.isBlank() || name.length < 2),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                BrainQuestTextField(
                    value = email,
                    onValueChange = { 
                        email = it
                        error = null
                    },
                    label = "Email Address",
                    leadingIcon = Icons.Default.Email,
                    isError = error != null && (email.isBlank() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                BrainQuestTextField(
                    value = password,
                    onValueChange = { 
                        password = it
                        error = null
                    },
                    label = "Password",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    isError = error != null && (password.isBlank() || password.length < 6),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                BrainQuestTextField(
                    value = confirmPassword,
                    onValueChange = { 
                        confirmPassword = it
                        error = null
                    },
                    label = "Confirm Password",
                    leadingIcon = Icons.Default.Lock,
                    isPassword = true,
                    isError = error != null && confirmPassword != password,
                    modifier = Modifier.fillMaxWidth()
                )
                
                if (error != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = error!!,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                Spacer(modifier = Modifier.height(24.dp))
                
                BrainQuestButton(
                    onClick = { signUp() },
                    text = "Create Account",
                    icon = Icons.Default.PersonAdd,
                    isLoading = loading,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                BrainQuestOutlinedButton(
                    onClick = { navController.navigate(Screen.Login.route) },
                    text = "Already have an account? Sign In",
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "By creating an account, you agree to our Terms of Service and Privacy Policy",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }
    }
}
