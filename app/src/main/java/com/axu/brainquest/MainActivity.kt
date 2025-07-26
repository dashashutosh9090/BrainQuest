package com.axu.brainquest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.axu.brainquest.ui.theme.BrainQuestTheme
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.axu.brainquest.ui.SplashScreen
import com.axu.brainquest.ui.LoginScreen
import com.axu.brainquest.ui.SignupScreen
import com.axu.brainquest.ui.HomeScreen
import com.axu.brainquest.ui.QuizSetupScreen
import com.axu.brainquest.ui.QuizScreen
import com.axu.brainquest.ui.ResultScreen
import com.axu.brainquest.ui.ProfileScreen
import com.axu.brainquest.ui.LeaderboardScreen

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Login : Screen("login")
    object Signup : Screen("signup")
    object Home : Screen("home")
    object QuizSetup : Screen("quiz_setup")
    object Quiz : Screen("quiz")
    object Result : Screen("result")
    object Profile : Screen("profile")
    object Leaderboard : Screen("leaderboard")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BrainQuestTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavHost(navController = navController, modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    val quizViewModel: com.axu.brainquest.ui.QuizViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier
    ) {
        composable(Screen.Splash.route) { SplashScreen(navController) }
        composable(Screen.Login.route) { LoginScreen(navController) }
        composable(Screen.Signup.route) { SignupScreen(navController) }
        composable(Screen.Home.route) { HomeScreen(navController) }
        composable(Screen.QuizSetup.route) { QuizSetupScreen(navController, quizViewModel) }
        composable(Screen.Quiz.route) { QuizScreen(navController, quizViewModel) }
        composable(Screen.Result.route) { ResultScreen(navController, quizViewModel) }
        composable(Screen.Profile.route) { ProfileScreen(navController) }
        composable(Screen.Leaderboard.route) { LeaderboardScreen(navController) }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BrainQuestTheme {
        Greeting("Android")
    }
}