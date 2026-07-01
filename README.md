<div align="center">

<!-- ═══════════════════════ ANIMATED HEADER ═══════════════════════ -->
<img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=6,12,20,24&height=220&section=header&text=🧠%20BrainQuest&fontSize=72&fontColor=ffffff&animation=twinkling&fontAlignY=32&desc=⚡%20The%20Ultimate%20Trivia%20Battle%20Arena%20⚡&descSize=20&descAlignY=55" width="100%"/>

<!-- ═══════════════════════ TYPING ANIMATION ═══════════════════════ -->
<a href="https://github.com/dashashutosh9090/BrainQuest">
  <img src="https://readme-typing-svg.demolab.com?font=Fira+Code&weight=600&size=24&pause=1000&color=00F7FF&center=true&vCenter=true&width=700&lines=%3E+INITIALIZING+NEURAL+INTERFACE...;%3E+24+KNOWLEDGE+SECTORS+DETECTED;%3E+LEADERBOARD+UPLINK+ESTABLISHED;%3E+CHALLENGE+YOUR+BRAIN.+CLIMB+THE+RANKS.;%3E+ALL+SYSTEMS+GO+%E2%9C%93" alt="Typing Animation"/>
</a>

<br/><br/>

<!-- ═══════════════════════ BADGE WALL ═══════════════════════ -->
<img src="https://img.shields.io/badge/Kotlin-2.2.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" alt="Kotlin"/>
<img src="https://img.shields.io/badge/Jetpack%20Compose-BOM%202025.07-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" alt="Jetpack Compose"/>
<img src="https://img.shields.io/badge/Firebase-Auth%20%2B%20Firestore-FFCA28?style=for-the-badge&logo=firebase&logoColor=black" alt="Firebase"/>
<br/>
<img src="https://img.shields.io/badge/Material%203-Expressive-757575?style=for-the-badge&logo=materialdesign&logoColor=white" alt="Material 3"/>
<img src="https://img.shields.io/badge/Retrofit-3.0.0-48B983?style=for-the-badge&logo=square&logoColor=white" alt="Retrofit"/>
<img src="https://img.shields.io/badge/Min%20SDK-24-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Min SDK"/>
<img src="https://img.shields.io/badge/Target%20SDK-36-3DDC84?style=for-the-badge&logo=android&logoColor=white" alt="Target SDK"/>

<br/><br/>

<img src="https://img.shields.io/github/stars/dashashutosh9090/BrainQuest?style=for-the-badge&logo=github&color=8A2BE2&logoColor=white" alt="Stars"/>
<img src="https://img.shields.io/github/forks/dashashutosh9090/BrainQuest?style=for-the-badge&logo=github&color=00CED1&logoColor=white" alt="Forks"/>
<img src="https://img.shields.io/github/last-commit/dashashutosh9090/BrainQuest?style=for-the-badge&logo=git&color=FF6B6B&logoColor=white" alt="Last Commit"/>

</div>

<!-- ═══════════════════════ ANIMATED DIVIDER ═══════════════════════ -->
<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="100%" alt="divider"/>

## 🛰️ Mission Briefing

```text
 ╔══════════════════════════════════════════════════════════╗
 ║  > BOOTING BRAINQUEST v1.0 .......................       ║
 ║  > NEURAL LINK ..................... ✓ ONLINE            ║
 ║  > FIREBASE UPLINK ................. ✓ CONNECTED         ║
 ║  > QUESTION DATABASE ............... ✓ 24 SECTORS LOADED ║
 ║  > DIFFICULTY MATRIX ............... ✓ EASY→MEDIUM→HARD  ║
 ║  > LEADERBOARD SATELLITE ........... ✓ IN ORBIT          ║
 ║                                                          ║
 ║        ALL SYSTEMS GO. WELCOME, CHALLENGER. 🚀           ║
 ╚══════════════════════════════════════════════════════════╝
```

**BrainQuest** is a sleek, modern Android trivia app that turns knowledge into competition. Pick your battlefield from **24 categories** — from 🧬 Science to 🎮 Video Games to 🏛️ Mythology — tune the difficulty, and battle through questions streamed live from the [Open Trivia Database](https://opentdb.com/). Every score is beamed to a **global Firestore leaderboard**, so the fight for Rank #1 never ends.

<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="100%" alt="divider"/>

## ⚡ Feature Matrix

<table>
<tr>
<td width="50%">

### 🔐 Secure Access Terminal
Email/password authentication powered by **Firebase Auth** — sign up, log in, and your identity travels with you.

### 🎛️ Quiz Configurator
Dial in your mission parameters:
- 🗂️ **24 categories** + random mode
- 🌡️ **3 difficulty levels** (Easy / Medium / Hard)
- ❓ **Question types** — Multiple Choice or True/False
- 🔢 **1–50 questions** per run

### 🧠 Live Question Engine
Questions fetched in real time via **Retrofit + Gson** from Open Trivia DB, with graceful error handling for lost signals.

</td>
<td width="50%">

### 🏆 Global Leaderboard
Three ranking dimensions, all synced through **Cloud Firestore**:
- 💯 Total Score
- 📊 Average Score
- 🥇 Best Score

### 📡 Command Center (Home)
Your personal HQ — total quizzes flown, global rank, and a feed of your recent missions with timestamps.

### 👤 Pilot Profile
Track your identity and stats across sessions.

### 🌌 Fluid UI
100% **Jetpack Compose** + **Material 3** with edge-to-edge rendering and smooth navigation transitions.

</td>
</tr>
</table>

<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="100%" alt="divider"/>

## 🚀 Navigation Star Map

```mermaid
%%{init: {'theme':'dark', 'themeVariables': {'primaryColor':'#1a1b27','primaryTextColor':'#00f7ff','primaryBorderColor':'#7F52FF','lineColor':'#8A2BE2','fontSize':'16px'}}}%%
flowchart LR
    A(["🛸 Splash"]) --> B{"🔑 Authenticated?"}
    B -- "No" --> C["🔐 Login"]
    C <-.-> D["📝 Signup"]
    B -- "Yes" --> E["🏠 Home Base"]
    C --> E
    D --> E
    E --> F["🎛️ Quiz Setup"]
    F --> G["🧠 Quiz Arena"]
    G --> H["📊 Results"]
    H -- "Retry Mission" --> F
    H -- "Return to Base" --> E
    E --> I["👤 Profile"]
    E --> J["🏆 Leaderboard"]
```

## 🧬 System Architecture

```mermaid
%%{init: {'theme':'dark', 'themeVariables': {'primaryColor':'#1a1b27','primaryTextColor':'#00f7ff','primaryBorderColor':'#7F52FF','lineColor':'#8A2BE2'}}}%%
flowchart TB
    subgraph UI ["🎨 Compose UI Layer"]
        SCREENS["9 Screens · Material 3 · Navigation Compose"]
    end
    subgraph VM ["🧠 ViewModel Layer"]
        QVM["QuizViewModel · StateFlow · Coroutines"]
    end
    subgraph DATA ["📡 Data Layer"]
        RETRO["Retrofit + Gson"]
        FB["Firebase Auth + Firestore"]
    end
    OTDB[("🌐 Open Trivia DB API")]
    CLOUD[("☁️ Google Cloud")]

    SCREENS -- "events" --> QVM
    QVM -- "UiState (StateFlow)" --> SCREENS
    QVM --> RETRO --> OTDB
    SCREENS --> FB --> CLOUD
```

<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="100%" alt="divider"/>

## 🛠️ Tech Arsenal

<div align="center">

<img src="https://skillicons.dev/icons?i=kotlin,androidstudio,firebase,gradle,git,github&theme=dark" alt="Tech Stack"/>

| System | Technology | Version |
|:------:|:-----------|:-------:|
| 🚀 **Language** | Kotlin | `2.2.0` |
| 🎨 **UI Toolkit** | Jetpack Compose (Material 3) | `BOM 2025.07.00` |
| 🧭 **Navigation** | Navigation Compose | `2.9.2` |
| 🔥 **Backend** | Firebase Auth · Firestore · Analytics | `BOM 34.0.0` |
| 📡 **Networking** | Retrofit + Gson Converter | `3.0.0` |
| 🏗️ **Build System** | Gradle (Kotlin DSL) + AGP | `8.11.1` |
| 🧪 **Testing** | JUnit · Espresso · Compose UI Test | — |

</div>

## 📂 Project Coordinates

```text
🛸 BrainQuest/
├── 📱 app/src/main/java/com/axu/brainquest/
│   ├── 🚀 MainActivity.kt            → Launch pad + NavHost
│   └── 🎨 ui/
│       ├── 🛸 SplashScreen.kt        → Boot sequence
│       ├── 🔐 LoginScreen.kt         → Access terminal
│       ├── 📝 SignupScreen.kt        → New pilot registration
│       ├── 🏠 HomeScreen.kt          → Command center + stats
│       ├── 🎛️ QuizSetupScreen.kt     → Mission configurator
│       ├── 🧠 QuizScreen.kt          → The arena
│       ├── 📊 ResultScreen.kt        → Mission debrief
│       ├── 👤 ProfileScreen.kt       → Pilot dossier
│       ├── 🏆 LeaderboardScreen.kt   → Hall of legends
│       ├── ⚙️ QuizViewModel.kt       → State engine + OpenTDB API
│       ├── 🧩 components/            → Shared UI components
│       └── 🌈 theme/                 → Colors · Typography · Theme
└── 📦 *.apk                          → Ready-to-fly builds
```

<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="100%" alt="divider"/>

## 🧑‍🚀 Launch Sequence (Getting Started)

> **Pre-flight checklist:** Android Studio (latest), JDK 11+, Android SDK 36, and a device/emulator on API 24+.

```bash
# 1️⃣  Clone the mothership
git clone https://github.com/dashashutosh9090/BrainQuest.git
cd BrainQuest

# 2️⃣  Open in Android Studio and let Gradle sync

# 3️⃣  Ignite engines
./gradlew assembleDebug

# 4️⃣  Deploy to your device
./gradlew installDebug
```

> 🔥 **Firebase note:** the project ships with a `google-services.json`. To point at **your own** Firebase project, create one in the [Firebase Console](https://console.firebase.google.com/), enable **Email/Password Auth** + **Cloud Firestore**, and drop your `google-services.json` into `app/`.

### 📦 Instant Deploy

No build tools? Grab a pre-built APK straight from the repo:

<div align="center">

[![Download APK](https://img.shields.io/badge/⬇️%20Download-BrainQuest--release.apk-00F7FF?style=for-the-badge&logo=android&logoColor=black)](https://github.com/dashashutosh9090/BrainQuest/raw/master/BrainQuest-release.apk)

</div>

## 📸 Visual Transmission

<details>
<summary>🖼️ <b>Click to expand screenshots</b> <i>(incoming transmission...)</i></summary>
<br/>

> 📡 Screenshots are being beamed down from orbit — add yours to a `screenshots/` folder and reference them here!

<!--
<p align="center">
  <img src="screenshots/home.png" width="24%"/>
  <img src="screenshots/quiz.png" width="24%"/>
  <img src="screenshots/result.png" width="24%"/>
  <img src="screenshots/leaderboard.png" width="24%"/>
</p>
-->

</details>

<img src="https://user-images.githubusercontent.com/74038190/212284100-561aa473-3905-4a80-b561-0d28506553ee.gif" width="100%" alt="divider"/>

## 🤝 Join the Crew

Contributions, ideas, and bug reports are always welcome aboard!

```text
1. 🍴 Fork the repo          →  your own parallel universe
2. 🌿 git checkout -b feat/warp-drive
3. 💾 git commit -m "feat: add warp drive"
4. 🚀 git push origin feat/warp-drive
5. 🛰️ Open a Pull Request    →  dock with the mothership
```

## 👨‍🚀 Commander

<div align="center">

**Ashutosh Dash**

[![GitHub](https://img.shields.io/badge/GitHub-dashashutosh9090-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/dashashutosh9090)

<br/>

*⭐ If BrainQuest fired up your neurons, drop a star — it fuels the mission! ⭐*

<br/>

<img src="https://readme-typing-svg.demolab.com?font=Fira+Code&size=18&pause=1000&color=8A2BE2&center=true&vCenter=true&width=500&lines=%3E+END+OF+TRANSMISSION;%3E+SEE+YOU+ON+THE+LEADERBOARD%2C+CHALLENGER+🏆" alt="footer typing"/>

<!-- ═══════════════════════ ANIMATED FOOTER ═══════════════════════ -->
<img src="https://capsule-render.vercel.app/api?type=waving&color=gradient&customColorList=6,12,20,24&height=140&section=footer" width="100%"/>

</div>
