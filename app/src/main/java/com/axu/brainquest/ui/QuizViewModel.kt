package com.axu.brainquest.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import com.google.gson.annotations.SerializedName

// --- Data Models ---
data class QuizQuestion(
    @SerializedName("question") val question: String,
    @SerializedName("correct_answer") val correctAnswer: String,
    @SerializedName("incorrect_answers") val incorrectAnswers: List<String>,
    @SerializedName("type") val type: String,
    @SerializedName("category") val category: String,
    @SerializedName("difficulty") val difficulty: String
)

data class QuizResponse(
    @SerializedName("results") val results: List<QuizQuestion>
)

// --- Quiz Configuration ---
enum class QuizCategory(val id: Int, val displayName: String) {
    ANY(0, "Any Category"),
    GENERAL_KNOWLEDGE(9, "General Knowledge"),
    BOOKS(10, "Entertainment: Books"),
    FILM(11, "Entertainment: Film"),
    MUSIC(12, "Entertainment: Music"),
    MUSICALS_THEATRES(13, "Entertainment: Musicals & Theatres"),
    TELEVISION(14, "Entertainment: Television"),
    VIDEO_GAMES(15, "Entertainment: Video Games"),
    BOARD_GAMES(16, "Entertainment: Board Games"),
    SCIENCE_NATURE(17, "Science & Nature"),
    COMPUTERS(18, "Science: Computers"),
    MATHEMATICS(19, "Science: Mathematics"),
    MYTHOLOGY(20, "Mythology"),
    SPORTS(21, "Sports"),
    GEOGRAPHY(22, "Geography"),
    HISTORY(23, "History"),
    POLITICS(24, "Politics"),
    ART(25, "Art"),
    CELEBRITIES(26, "Celebrities"),
    ANIMALS(27, "Animals"),
    VEHICLES(28, "Vehicles"),
    COMICS(29, "Entertainment: Comics"),
    GADGETS(30, "Science: Gadgets"),
    ANIME_MANGA(31, "Entertainment: Japanese Anime & Manga"),
    CARTOON_ANIMATIONS(32, "Entertainment: Cartoon & Animations")
}

enum class QuizDifficulty(val value: String, val displayName: String) {
    ANY("", "Any Difficulty"),
    EASY("easy", "Easy"),
    MEDIUM("medium", "Medium"),
    HARD("hard", "Hard")
}

enum class QuizType(val value: String, val displayName: String) {
    ANY("", "Any Type"),
    MULTIPLE("multiple", "Multiple Choice"),
    BOOLEAN("boolean", "True / False")
}

data class QuizConfig(
    val amount: Int = 10,
    val category: QuizCategory = QuizCategory.ANY,
    val difficulty: QuizDifficulty = QuizDifficulty.ANY,
    val type: QuizType = QuizType.ANY
)

// --- Retrofit API ---
interface OpenTDBApi {
    @GET("api.php")
    suspend fun getQuestions(
        @Query("amount") amount: Int,
        @Query("category") category: Int?,
        @Query("difficulty") difficulty: String?,
        @Query("type") type: String?
    ): QuizResponse
}

object RetrofitInstance {
    val api: OpenTDBApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://opentdb.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenTDBApi::class.java)
    }
}

// --- ViewModel State ---
data class QuizUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val questions: List<QuizQuestion> = emptyList(),
    val currentIndex: Int = 0,
    val userAnswers: List<String> = emptyList(),
    val score: Int = 0
)

class QuizViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(QuizUiState(loading = true))
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        fetchQuestions()
    }

    fun fetchQuestions(config: QuizConfig = QuizConfig()) {
        _uiState.value = QuizUiState(loading = true)
        viewModelScope.launch {
            try {
                // Explicitly handle each parameter according to OpenTDB API requirements
                val response = RetrofitInstance.api.getQuestions(
                    amount = config.amount.coerceIn(1, 50), // Ensure amount is within valid range
                    category = if (config.category == QuizCategory.ANY) null else config.category.id,
                    difficulty = if (config.difficulty == QuizDifficulty.ANY) null else config.difficulty.value,
                    type = if (config.type == QuizType.ANY) null else config.type.value
                )
                _uiState.value = QuizUiState(
                    loading = false,
                    questions = response.results,
                    userAnswers = List(response.results.size) { "" }
                )
            } catch (e: Exception) {
                _uiState.value = QuizUiState(loading = false, error = e.localizedMessage)
            }
        }
    }

    fun answerCurrentQuestion(answer: String) {
        val state = _uiState.value
        val updatedAnswers = state.userAnswers.toMutableList()
        updatedAnswers[state.currentIndex] = answer
        
        // Recalculate the entire score by counting correct answers
        val correctCount = state.questions.zip(updatedAnswers) { question, userAnswer ->
            if (userAnswer == question.correctAnswer) 1 else 0
        }.sum()
        
        _uiState.value = state.copy(
            userAnswers = updatedAnswers,
            score = correctCount
        )
    }

    fun nextQuestion() {
        val state = _uiState.value
        if (state.currentIndex < state.questions.size - 1) {
            _uiState.value = state.copy(currentIndex = state.currentIndex + 1)
        }
    }

    fun resetQuiz() {
        fetchQuestions()
    }

}