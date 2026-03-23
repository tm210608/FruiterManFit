package com.ejemplo.myapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ejemplo.myapp.data.repository.FitnessRepository

class FitnessViewModelFactory(private val repository: FitnessRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(DashboardViewModel::class.java) -> DashboardViewModel(repository) as T
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> ProfileViewModel(repository) as T
            modelClass.isAssignableFrom(ExerciseLibraryViewModel::class.java) -> ExerciseLibraryViewModel(repository) as T
            modelClass.isAssignableFrom(WorkoutSessionViewModel::class.java) -> WorkoutSessionViewModel(repository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
