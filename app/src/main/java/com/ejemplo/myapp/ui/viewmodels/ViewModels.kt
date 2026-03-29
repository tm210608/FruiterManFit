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
            initialValue = UserStats(1, "Fresh Fruit", 0, "0", 0)
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
            initialValue = UserStats(1, "Fresh Fruit", 0, "0", 0)
        )
}

@HiltViewModel
class ExerciseLibraryViewModel @Inject constructor(
    private val getExercisesUseCase: GetExercisesUseCase,
    private val repository: FitnessRepository
) : ViewModel() {
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedFilter = MutableStateFlow("All")
    val selectedFilter: StateFlow<String> = _selectedFilter

    val exercises: StateFlow<List<Exercise>> = combine(
        getExercisesUseCase(),
        _searchQuery,
        _selectedFilter
    ) { list, query, filter ->
        list.filter { 
            (filter == "All" || it.category == filter) &&
            (query.isEmpty() || it.name.contains(query, ignoreCase = true))
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

    init {
        viewModelScope.launch {
            repository.refreshExercises()
        }
    }
}

@HiltViewModel
class WorkoutSessionViewModel @Inject constructor(
    private val repository: FitnessRepository
) : ViewModel() {
    private val _activeExercises = MutableStateFlow<List<ActiveExercise>>(
        listOf(
            ActiveExercise(
                "1", "Bench Press", "Chest", BrightBlue,
                listOf(SessionSet(1, "80", "10", false))
            )
        )
    )
    val activeExercises: StateFlow<List<ActiveExercise>> = _activeExercises

    private val _startTime = System.currentTimeMillis()

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

    fun finishWorkout(onComplete: () -> Unit) {
        viewModelScope.launch {
            val duration = System.currentTimeMillis() - _startTime
            // Cálculo básico de calorías: 250 cal por sesión por ahora.
            // Más adelante podemos basarlo en el volumen total (sets * reps * weight)
            repository.saveWorkoutSession(
                title = "Morning Muscle Burn 🔥",
                durationMillis = duration,
                calories = 250,
                activeExercises = _activeExercises.value
            )
            onComplete()
        }
    }
}
