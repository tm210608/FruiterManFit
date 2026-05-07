# 🍏 FruiterMan Fit - Análisis de Arquitectura y Diseño

> Fecha de análisis: 3 Mayo 2026  
> Versión del proyecto: 1.0

---

## 📋 Índice

1. [Visión General](#visión-general)
2. [Stack Tecnológico](#stack-tecnológico)
3. [Arquitectura General](#arquitectura-general)
4. [Capa de Datos (Data Layer)](#capa-de-datos-data-layer)
5. [Capa de Dominio (Domain Layer)](#capa-de-dominio-domain-layer)
6. [Capa de Presentación (Presentation Layer)](#capa-de-presentación-presentation-layer)
7. [Inyección de Dependencias (DI)](#inyección-de-dependencias-di)
8. [Navegación](#navegación)
9. [Gestión de Estado](#gestión-de-estado)
10. [Flujo de Datos](#flujo-de-datos)
11. [Patrones de Diseño Utilizados](#patrones-de-diseño-utilizados)
12. [Estructura de Carpetas](#estructura-de-carpetas)

---

## Visión General

**FruiterMan Fit** es una aplicación Android moderna de fitness construida con arquitectura **Clean Architecture** y **MVVM**. La app permite registrar entrenamientos, gestionar ejercicios, calcular estadísticas de usuario y sincronizar datos desde ExerciseDB (API externa).

---

## Stack Tecnológico

| Componente | Tecnología | Versión | Propósito |
|------------|------------|---------|-----------|
| **Lenguaje** | Kotlin | 1.9.24 | Lenguaje principal |
| **UI Framework** | Jetpack Compose | BOM 2023.08.00 | Interfaz declarativa |
| **Arquitectura** | MVVM + Clean Arch | - | Separación de responsabilidades |
| **Base de Datos** | Room | 2.6.1 | Persistencia local |
| **DI** | Hilt (Dagger) | 2.51 | Inyección de dependencias |
| **Networking** | Retrofit + OkHttp | 2.9.0 / 4.11.0 | Comunicación HTTP |
| **Imágenes** | Coil | 2.5.0 | Carga de imágenes/GIFs |
| **Navegación** | Navigation Compose | 2.7.5 | Navegación entre pantallas |
| **Animaciones** | Lottie | 6.7.1 | Animaciones vectoriales |

---

## Arquitectura General

La aplicación sigue una arquitectura **Clean Architecture** con tres capas principales:

```
┌─────────────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                           │
│  ┌───────────┐  ┌───────────┐  ┌───────────┐  ┌───────────┐     │
│  │   UI      │  │ ViewModels│  │ Navigation│  │  Theme    │     │
│  │ (Screens) │  │ (State)   │  │ (NavGraph)│  │ (Compose) │     │
│  └─────┬─────┘  └─────┬─────┘  └───────────┘  └───────────┘     │
│        │              │                                         │
│        └──────────────┘                                         │
│               │                                                  │
│        ┌──────┴──────┐                                          │
│        │   StateFlow │  ← Reactive UI Updates                   │
│        └──────┬──────┘                                          │
└───────────────┼─────────────────────────────────────────────────┘
                │
                ▼
┌─────────────────────────────────────────────────────────────────┐
│                    DOMAIN LAYER                                   │
│  ┌───────────┐  ┌───────────┐                                   │
│  │ Use Cases │  │  Models   │                                   │
│  │ (Logic)   │  │ (Domain)  │                                   │
│  └─────┬─────┘  └───────────┘                                   │
│        │                                                         │
│        └────────────────┐                                        │
│                         ▼                                        │
└─────────────────────────────────────────────────────────────────┘
                │
                ▼
┌─────────────────────────────────────────────────────────────────┐
│                      DATA LAYER                                 │
│  ┌───────────┐  ┌───────────┐  ┌───────────┐                   │
│  │ Repository│  │   DAO     │  │  Remote   │                   │
│  │ (Logic)   │  │ (Local)   │  │   API     │                   │
│  └─────┬─────┘  └─────┬─────┘  └─────┬─────┘                   │
│        │              │              │                          │
│        └──────────────┼──────────────┘                          │
│                       │                                          │
│        ┌──────────────┴──────────────┐                          │
│        │         Room DB            │                          │
│        │  (SQLite + Flow)           │                          │
│        └────────────────────────────┘                          │
└─────────────────────────────────────────────────────────────────┘
```

---

## Capa de Datos (Data Layer)

### Estructura

```
data/
├── local/              # Persistencia local
│   ├── dao/            # Data Access Objects (Room)
│   ├── entities/       # Entidades de base de datos
│   ├── converters/     # Type converters
│   ├── AppDatabase.kt  # Configuración Room
│   └── DatabaseConfig.kt
├── remote/             # API externa
│   └── ExerciseApiService.kt
├── repository/         # Repositorios
│   ├── FitnessRepository.kt
│   └── UserRepository.kt
└── models/             # Modelos de dominio
    └── Models.kt
```

### Componentes Clave

#### 1. Room Database (Local Storage)

**Ubicación:** `data/local/`

```kotlin
// AppDatabase.kt - Configuración principal
@Database(
    entities = [
        UserEntity::class,
        ExerciseEntity::class,
        WorkoutSessionEntity::class,
        SessionExerciseEntity::class,
        ExerciseSetEntity::class,
        FruitChallengeEntity::class
    ],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fitnessDao(): FitnessDao
}
```

**Entidades Principales:**

| Entidad | Descripción | Relaciones |
|---------|-------------|------------|
| `UserEntity` | Información del usuario | - |
| `ExerciseEntity` | Ejercicios (1300+ de ExerciseDB) | - |
| `WorkoutSessionEntity` | Sesión de entrenamiento | 1:N con SessionExercise |
| `SessionExerciseEntity` | Ejercicio en una sesión | N:1 con WorkoutSession, 1:N con ExerciseSet |
| `ExerciseSetEntity` | Serie de un ejercicio | N:1 con SessionExercise |
| `FruitChallengeEntity` | Retos/desafíos del usuario | - |

**DAO (FitnessDao):**

```kotlin
@Dao
interface FitnessDao {
    // User operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)
    
    @Query("SELECT * FROM users LIMIT 1")
    fun getUser(): Flow<UserEntity?>
    
    // Exercise operations
    @Query("SELECT * FROM exercises")
    fun getAllExercises(): Flow<List<ExerciseEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)
    
    // Session operations
    @Transaction
    @Query("SELECT * FROM workout_sessions ORDER BY startTime DESC")
    fun getFullSessions(): Flow<List<FullWorkoutSession>>
    
    // ... más operaciones
}
```

#### 2. API Remota (ExerciseDB)

**Ubicación:** `data/remote/ExerciseApiService.kt`

```kotlin
interface ExerciseApiService {
    @GET("exercises.json")
    suspend fun getFullDataset(): List<ExerciseDto>
}
```

**Fuente:** [free-exercise-db](https://github.com/yuhonas/free-exercise-db)  
**Total:** ~1300 ejercicios con GIFs animados

#### 3. Repositorios

**FitnessRepository** (`data/repository/FitnessRepository.kt`):

```kotlin
class FitnessRepository @Inject constructor(
    private val fitnessDao: FitnessDao,
    private val exerciseApiService: ExerciseApiService
) {
    // Sincronización con ExerciseDB
    suspend fun refreshExercises(apiKey: String) { ... }
    
    // Cálculo de estadísticas en tiempo real
    fun getRealUserStats(): Flow<UserStats> { ... }
    
    // Gestión de sesiones
    suspend fun saveWorkoutSession(...) { ... }
    
    // Gestión de retos
    fun getFruitChallenges(): Flow<List<FruitChallenge>> { ... }
}
```

**Responsabilidades del Repository:**
- Abstracción de la fuente de datos (local vs remota)
- Mapeo entre entidades y modelos de dominio
- Lógica de negocio relacionada con datos
- Exposición de `Flow` para reactividad

---

## Capa de Dominio (Domain Layer)

### Estructura

```
domain/
└── usecase/
    └── GetExercisesUseCase.kt
```

### Use Cases

```kotlin
class GetExercisesUseCase @Inject constructor(
    private val repository: FitnessRepository
) {
    operator fun invoke(): Flow<List<Exercise>> {
        return repository.getExercises()
    }
}
```

**Patrón:** Cada use case representa una operación de negocio única, encapsulando la lógica y facilitando testing.

**Modelos de Dominio** (`data/models/Models.kt`):

```kotlin
data class Exercise(
    val id: String,
    val name: String,
    val bodyPart: String,
    val equipment: String,
    val gifUrl: String,
    val target: String,
    val secondaryMuscles: List<String>,
    val instructions: List<String>,
    val accentColor: Color?,  // Color según grupo muscular
    val description: String,
    val difficulty: String,
    val category: String
)

data class UserStats(
    val userName: String,
    val level: Int,
    val rank: String,
    val streak: Int,           // Racha de días
    val calories: String,      // Formateado (ej: "1.5k")
    val goalReached: Int,      // Porcentaje
    val totalVolume: Double,   // Peso total levantado
    val weeklyVolume: List<Double>,
    val weeklySessionsCount: Int,
    val weeklyGoal: Int,
    val badges: List<Badge>
)

// ... más modelos
```

---

## Capa de Presentación (Presentation Layer)

### Estructura

```
ui/
├── screens/           # Pantallas principales
│   ├── DashboardScreen.kt
│   ├── WorkoutLogScreen.kt
│   ├── ExerciseLibraryScreen.kt
│   ├── ProfileScreen.kt
│   ├── HistoryScreen.kt
│   ├── LoginScreen.kt
│   ├── SignupScreen.kt
│   ├── SettingsScreen.kt
│   ├── ExerciseDetailScreen.kt
│   ├── SplashScreen.kt
│   ├── SocialScreen.kt
│   ├── NutritionLogScreen.kt
│   ├── MobilityScreen.kt
│   └── ...
├── viewmodels/        # Lógica de estado
│   ├── ViewModels.kt
│   ├── UserViewModel.kt
│   └── FitnessViewModelFactory.kt
├── components/        # Componentes reutilizables
│   ├── CommonComponents.kt
│   ├── DashboardComponents.kt
│   └── ProfileComponents.kt
├── theme/             # Diseño visual
│   ├── Color.kt
│   └── Theme.kt
└── utils/             # Utilidades UI
    ├── ExerciseTranslator.kt
    └── LocalizationUtils.kt
```

### ViewModels

#### DashboardViewModel

```kotlin
@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: FitnessRepository
) : ViewModel() {
    
    val stats: StateFlow<UserStats> = repository.getRealUserStats()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserStats(...)
        )
    
    val challenges: StateFlow<List<FruitChallenge>> = repository.getFruitChallenges()
        .stateIn(...)
}
```

#### WorkoutSessionViewModel

Gestiona el estado de una sesión de entrenamiento activa:

```kotlin
@HiltViewModel
class WorkoutSessionViewModel @Inject constructor(
    private val repository: FitnessRepository
) : ViewModel() {
    
    private val _activeExercises = MutableStateFlow<List<ActiveExercise>>(emptyList())
    val activeExercises: StateFlow<List<ActiveExercise>> = _activeExercises
    
    fun addExerciseById(exerciseId: String) { ... }
    fun addSet(exerciseId: String) { ... }
    fun updateSet(exerciseId: String, setNumber: Int, weight: String, reps: String, isDone: Boolean) { ... }
    fun finishWorkout(onComplete: (Long, Int, Double) -> Unit) { ... }
}
```

---

## Inyección de Dependencias (DI)

### Configuración Hilt

**Aplicación:** `FitnessApplication.kt`

```kotlin
@HiltAndroidApp
class FitnessApplication : Application(), ImageLoaderFactory {
    @Inject lateinit var okHttpClient: OkHttpClient
    
    override fun newImageLoader(): ImageLoader {
        // Configuración Coil con soporte GIF
    }
}
```

### Módulos Dagger/Hilt

#### DatabaseModule

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase { ... }
    
    @Provides
    fun provideFitnessDao(database: AppDatabase): FitnessDao { ... }
}
```

#### NetworkModule

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient { ... }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit { ... }
    
    @Provides
    @Singleton
    fun provideExerciseApiService(retrofit: Retrofit): ExerciseApiService { ... }
}
```

#### RepositoryModule

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideFitnessRepository(
        fitnessDao: FitnessDao,
        exerciseApiService: ExerciseApiService
    ): FitnessRepository { ... }
}
```

---

## Navegación

### NavGraph

**Archivo:** `navigation/NavGraph.kt`

```kotlin
@Composable
fun SetupNavGraph(navController: NavHostController) {
    val sessionViewModel: WorkoutSessionViewModel = hiltViewModel()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // Splash → Login o Home
        composable(Screen.Splash.route) { SplashScreen(...) }
        
        // Auth
        composable(Screen.Login.route) { LoginScreen(...) }
        composable(Screen.Signup.route) { SignupScreen(...) }
        
        // Main
        composable(Screen.Home.route) { DashboardScreen(...) }
        composable(Screen.Session.route) { WorkoutLogScreen(viewModel = sessionViewModel) }
        composable(Screen.Plans.route) { ExerciseLibraryScreen(...) }
        
        // Detail con argumentos
        composable(
            route = Screen.ExerciseDetail.route,
            arguments = listOf(navArgument("exerciseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val exerciseId = backStackEntry.arguments?.getString("exerciseId")
            ExerciseDetailScreen(exerciseId = exerciseId, ...)
        }
        
        // ... más rutas
    }
}
```

### Rutas Definidas

| Ruta | Pantalla | Descripción |
|------|----------|-------------|
| `splash` | SplashScreen | Pantalla de inicio |
| `login` | LoginScreen | Autenticación |
| `signup` | SignupScreen | Registro |
| `home` | DashboardScreen | Dashboard principal |
| `session` | WorkoutLogScreen | Sesión de entrenamiento activa |
| `plans` | ExerciseLibraryScreen | Librería de ejercicios |
| `exercise_detail/{id}` | ExerciseDetailScreen | Detalle de ejercicio |
| `profile` | ProfileScreen | Perfil de usuario |
| `history` | HistoryScreen | Historial de entrenamientos |
| `settings` | SettingsScreen | Configuración |
| `social` | SocialScreen | Funcionalidad social |
| `nutrition` | NutritionLogScreen | Registro nutricional |
| `mobility` | MobilityScreen | Ejercicios de movilidad |

---

## Gestión de Estado

### Flujo Reactivo con StateFlow

```
┌─────────────────────────────────────────────────────┐
│  Repository → Flow<T> → ViewModel → StateFlow<T>   │
│         ↓                              ↓            │
│    (Datos cambian)              (UI recompone)      │
└─────────────────────────────────────────────────────┘
```

**Ejemplo:**

```kotlin
// Repository expone Flow
fun getExercises(): Flow<List<Exercise>> = fitnessDao.getAllExercises()
    .map { entities -> entities.map { it.toDomain() } }

// ViewModel convierte a StateFlow
val exercises: StateFlow<List<Exercise>> = getExercisesUseCase()
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

// UI observa y reacciona
val exercises by viewModel.exercises.collectAsStateWithLifecycle()
```

### ViewModel Compartido

`WorkoutSessionViewModel` se comparte entre pantallas para mantener estado durante toda la sesión:

```kotlin
// En NavGraph.kt
val sessionViewModel: WorkoutSessionViewModel = hiltViewModel()

// Usado en múltiples rutas:
// - Plans: Añade ejercicios
// - ExerciseDetail: Añade ejercicio específico  
// - Session: Muestra y edita ejercicios activos
```

---

## Flujo de Datos

### 1. Sincronización ExerciseDB

```
┌─────────────┐    HTTP GET    ┌──────────────┐    Parse    ┌─────────────┐
│ ExerciseApi │ ─────────────→ │  ExerciseDto │ ──────────→ │  Entities   │
│  Service    │                │   (JSON)     │             │  (Room)     │
└─────────────┘                └──────────────┘             └──────┬──────┘
                                                                    │
                                                                    ▼
                                                              ┌─────────────┐
                                                              │   Flow<T>   │
                                                              └──────┬──────┘
                                                                     │
                                                                     ▼
                                                              ┌─────────────┐
                                                              │   ViewModel │
                                                              └──────┬──────┘
                                                                     │
                                                                     ▼
                                                              ┌─────────────┐
                                                              │     UI      │
                                                              └─────────────┘
```

### 2. Guardado de Sesión

```
┌─────────────┐     User Input     ┌──────────────────┐
│     UI      │ ─────────────────→ │ WorkoutSessionVM │
│  (Screens)  │                    │                  │
└─────────────┘                    └────────┬─────────┘
                                          │
                                          ▼
                                   ┌─────────────┐
                                   │  Repository │
                                   │ saveSession │
                                   └──────┬──────┘
                                          │
                                          ▼
                                   ┌─────────────┐
                                   │    DAO      │
                                   │   (Room)    │
                                   └──────┬──────┘
                                          │
                                          ▼
                                   ┌─────────────┐
                                   │   SQLite    │
                                   └─────────────┘
```

### 3. Cálculo de Estadísticas

```kotlin
// En FitnessRepository
fun getRealUserStats(): Flow<UserStats> = combine(
    fitnessDao.getFullSessions(),
    fitnessDao.getUser()
) { fullSessions, user ->
    val totalCalories = fullSessions.sumOf { it.session.totalCalories }
    val totalVolume = calculateTotalVolume(fullSessions)
    val streak = calculateStreak(fullSessions)
    val badges = calculateBadges(fullSessions.size)
    
    UserStats(
        userName = user?.name ?: "Fresh Fruit",
        level = (fullSessions.size / 5) + 1,
        rank = calculateRank(fullSessions.size),
        streak = streak,
        calories = formatCalories(totalCalories),
        totalVolume = totalVolume,
        ...
    )
}
```

---

## Patrones de Diseño Utilizados

### 1. **MVVM (Model-View-ViewModel)**

```
View (Compose) ←→ ViewModel (StateFlow) ←→ Model (Repository)
```

### 2. **Repository Pattern**

Unifica acceso a datos locales (Room) y remotos (API).

### 3. **Dependency Injection (DI)**

Hilt/Dagger proporciona dependencias automáticamente.

### 4. **Observer Pattern (StateFlow/Flow)**

UI se actualiza automáticamente cuando cambian los datos.

### 5. **Use Case Pattern**

Encapsula operaciones de negocio independientes.

### 6. **Singleton Pattern**

Database, Repository, API Service son singletons gestionados por Hilt.

### 7. **Factory Pattern**

`FitnessViewModelFactory` para creación manual de ViewModels (cuando se necesita pasar parámetros).

### 8. **Strategy Pattern**

Colores de acento según grupo muscular:

```kotlin
accentColorHex = when(bodyPart?.lowercase()) {
    "chest" -> "#FF4B4B"      // Rojo
    "back" -> "#4B7BFF"       // Azul
    "shoulders" -> "#FFB84B"  // Amarillo
    "upper arms", "lower arms" -> "#BC4BFF" // Morado
    "upper legs", "lower legs" -> "#4BFF81" // Verde
    "waist" -> "#FF4BEB"      // Rosa
    else -> "#00D4FF"         // Cyan default
}
```

---

## Estructura de Carpetas

```
FruiterMan Fit/
├── app/
│   └── src/main/java/com/ejemplo/myapp/
│       ├── FitnessApplication.kt          # Entry point + Hilt
│       ├── MainActivity.kt                # Activity principal
│       ├── data/
│       │   ├── local/
│       │   │   ├── AppDatabase.kt
│       │   │   ├── DatabaseConfig.kt
│       │   │   ├── MigrationProvider.kt
│       │   │   ├── converters/
│       │   │   │   └── StringListConverter.kt
│       │   │   ├── dao/
│       │   │   │   └── FitnessDao.kt
│       │   │   └── entities/
│       │   │       └── Entities.kt
│       │   ├── remote/
│       │   │   └── ExerciseApiService.kt
│       │   ├── repository/
│       │   │   ├── FitnessRepository.kt
│       │   │   └── UserRepository.kt
│       │   └── models/
│       │       └── Models.kt
│       ├── di/
│       │   ├── DatabaseModule.kt
│       │   ├── NetworkModule.kt
│       │   └── RepositoryModule.kt
│       ├── domain/
│       │   └── usecase/
│       │       └── GetExercisesUseCase.kt
│       ├── navigation/
│       │   ├── NavGraph.kt
│       │   └── Screen.kt
│       └── ui/
│           ├── components/
│           │   ├── CommonComponents.kt
│           │   ├── DashboardComponents.kt
│           │   └── ProfileComponents.kt
│           ├── screens/
│           │   ├── DashboardScreen.kt
│           │   ├── ExerciseLibraryScreen.kt
│           │   ├── ExerciseDetailScreen.kt
│           │   ├── WorkoutLogScreen.kt
│           │   ├── ProfileScreen.kt
│           │   ├── HistoryScreen.kt
│           │   ├── LoginScreen.kt
│           │   ├── SignupScreen.kt
│           │   ├── SettingsScreen.kt
│           │   ├── SplashScreen.kt
│           │   ├── SocialScreen.kt
│           │   ├── NutritionLogScreen.kt
│           │   └── MobilityScreen.kt
│           ├── theme/
│           │   ├── Color.kt
│           │   └── Theme.kt
│           ├── utils/
│           │   ├── ExerciseTranslator.kt
│           │   └── LocalizationUtils.kt
│           └── viewmodels/
│               ├── ViewModels.kt
│               ├── UserViewModel.kt
│               └── FitnessViewModelFactory.kt
├── docs/                                  # Documentación
├── gradle/
└── build.gradle.kts
```

---

## Características Destacadas

### ✅ **Arquitectura Limpia**
- Separación clara de responsabilidades
- Testabilidad mejorada
- Escalabilidad

### ✅ **Reactividad Total**
- StateFlow para UI
- Flow para datos
- Recomposición automática con Compose

### ✅ **Offline-First**
- Room para persistencia local
- Sincronización controlada con API
- Funcionamiento sin conexión

### ✅ **Inyección de Dependencias**
- Hilt para gestión de dependencias
- Código desacoplado
- Fácil testing con mocks

### ✅ **Imágenes Optimizadas**
- Coil para carga eficiente
- Soporte nativo para GIFs
- Cache automático

---

## Métricas del Proyecto

| Métrica | Valor |
|---------|-------|
| **Total archivos Kotlin** | 41 |
| **Pantallas** | 12 |
| **ViewModels** | 8 |
| **Entidades Room** | 6 |
| **Use Cases** | 1 |
| **Repositorios** | 2 |
| **Módulos DI** | 3 |
| **Ejercicios soportados** | 1300+ |

---

## Próximos Pasos Sugeridos

1. **Testing:** Añadir tests unitarios y de UI
2. **Worker:** Implementar WorkManager para sincronización en segundo plano
3. **DataStore:** Migrar configuraciones de usuario de Room a DataStore
4. **Gemini API:** Integrar asistente IA para recomendaciones
5. **Charts:** Añadir gráficos de progreso (MPAndroidChart o Compose Charts)

---

*Documento generado automáticamente para FruiterMan Fit*
