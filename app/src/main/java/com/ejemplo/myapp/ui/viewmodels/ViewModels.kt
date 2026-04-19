package com.ejemplo.myapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ejemplo.myapp.data.models.*
import com.ejemplo.myapp.domain.usecase.GetExercisesUseCase
import com.ejemplo.myapp.data.repository.FitnessRepository
import com.ejemplo.myapp.ui.theme.BrightBlue
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: FitnessRepository
) : ViewModel() {
    val workout: StateFlow<Workout> = MutableStateFlow(repository.getTodaysWorkout())
    
    val stats: StateFlow<UserStats> = repository.getRealUserStats()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserStats(userName = "Fresh Fruit", level = 1, rank = "Fresh Fruit", streak = 0, calories = "0", goalReached = 0)
        )

    val challenges: StateFlow<List<FruitChallenge>> = repository.getFruitChallenges()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
}

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: FitnessRepository
) : ViewModel() {
    val stats: StateFlow<UserStats> = repository.getRealUserStats()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserStats(userName = "Fresh Fruit", level = 1, rank = "Fresh Fruit", streak = 0, calories = "0", goalReached = 0)
        )
}

@HiltViewModel
class ExerciseLibraryViewModel @Inject constructor(
    private val getExercisesUseCase: GetExercisesUseCase,
    private val repository: FitnessRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedFilter = MutableStateFlow("Todo")
    val selectedFilter: StateFlow<String> = _selectedFilter

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    val exercises: StateFlow<List<Exercise>> = combine(
        getExercisesUseCase(),
        _searchQuery,
        _selectedFilter
    ) { list, query, filter ->
        list.filter { exercise ->
            val matchesFilter = if (filter == "Todo" || filter == "All") {
                true
            } else {
                // Comparamos ignorando mayúsculas/minúsculas y espacios
                exercise.bodyPart.replace(" ", "").equals(filter.replace(" ", ""), ignoreCase = true)
            }
            
            val matchesQuery = query.isEmpty() || exercise.name.contains(query, ignoreCase = true)
            
            matchesFilter && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onSearchQueryChanged(newQuery: String) {
        _searchQuery.value = newQuery
    }

    fun onFilterSelected(filter: String) {
        _selectedFilter.value = filter
    }

    fun refreshExercises(apiKey: String) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.refreshExercises(apiKey)
            _isLoading.value = false
        }
    }
}

@HiltViewModel
class ExerciseDetailViewModel @Inject constructor(
    val repository: FitnessRepository
) : ViewModel()

@HiltViewModel
class WorkoutSessionViewModel @Inject constructor(
    private val repository: FitnessRepository
) : ViewModel() {
    private val _activeExercises = MutableStateFlow<List<ActiveExercise>>(emptyList())
    val activeExercises: StateFlow<List<ActiveExercise>> = _activeExercises

    private val _startTime = System.currentTimeMillis()

    fun addExerciseById(exerciseId: String) {
        viewModelScope.launch {
            val allExercises = repository.getExercises().first()
            val exercise = allExercises.find { it.id == exerciseId }
            
            exercise?.let {
                val newActiveExercise = ActiveExercise(
                    exerciseId = it.id,
                    name = it.name,
                    subtitle = it.bodyPart,
                    accentColor = it.accentColor ?: BrightBlue,
                    sets = listOf(SessionSet(1, "0", "0", false)),
                    gifUrl = it.gifUrl
                )
                _activeExercises.value = _activeExercises.value + newActiveExercise
            }
        }
    }

    fun addSet(exerciseId: String) {
        val currentList = _activeExercises.value.toMutableList()
        val index = currentList.indexOfFirst { it.exerciseId == exerciseId }
        if (index != -1) {
            val exercise = currentList[index]
            val nextSetNumber = exercise.sets.size + 1
            val newSets = exercise.sets + SessionSet(nextSetNumber, "0", "0", false)
            currentList[index] = exercise.copy(sets = newSets)
            _activeExercises.value = currentList
        }
    }

    fun updateSet(exerciseId: String, setNumber: Int, weight: String, reps: String, isDone: Boolean) {
        val currentList = _activeExercises.value.toMutableList()
        val exIndex = currentList.indexOfFirst { it.exerciseId == exerciseId }
        if (exIndex != -1) {
            val exercise = currentList[exIndex]
            val setIndex = exercise.sets.indexOfFirst { it.number == setNumber }
            if (setIndex != -1) {
                val newSets = exercise.sets.toMutableList()
                newSets[setIndex] = SessionSet(setNumber, weight, reps, isDone)
                currentList[exIndex] = exercise.copy(sets = newSets)
                _activeExercises.value = currentList
            }
        }
    }

    fun removeExercise(exerciseId: String) {
        _activeExercises.value = _activeExercises.value.filterNot { it.exerciseId == exerciseId }
    }

    fun removeSet(exerciseId: String, setNumber: Int) {
        val currentList = _activeExercises.value.toMutableList()
        val index = currentList.indexOfFirst { it.exerciseId == exerciseId }
        if (index != -1) {
            val exercise = currentList[index]
            val newSets = exercise.sets.filterNot { it.number == setNumber }
                .mapIndexed { i, set -> set.copy(number = i + 1) } // Renumber sets
            currentList[index] = exercise.copy(sets = newSets)
            _activeExercises.value = currentList
        }
    }

    fun finishWorkout(onComplete: (Long, Int, Double) -> Unit) {
        if (_activeExercises.value.isEmpty()) {
            onComplete(0L, 0, 0.0)
            return
        }
        viewModelScope.launch {
            val duration = System.currentTimeMillis() - _startTime
            
            // Cálculo dinámico de volumen y calorías
            var totalVolume = 0.0
            _activeExercises.value.forEach { ex ->
                totalVolume += ex.sets.filter { it.isDone }.sumOf { 
                    (it.weight.toDoubleOrNull() ?: 0.0) * (it.reps.toIntOrNull() ?: 0) 
                }
            }
            
            val calories = (totalVolume * 0.05).toInt().coerceAtLeast(50)

            repository.saveWorkoutSession(
                title = "Custom Workout",
                durationMillis = duration,
                calories = calories,
                activeExercises = _activeExercises.value
            )
            onComplete(duration, calories, totalVolume)
        }
    }

    fun resetSession() {
        _activeExercises.value = emptyList()
    }
}
