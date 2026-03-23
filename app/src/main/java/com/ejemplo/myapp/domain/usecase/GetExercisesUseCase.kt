package com.ejemplo.myapp.domain.usecase

import com.ejemplo.myapp.data.models.Exercise
import com.ejemplo.myapp.data.repository.FitnessRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetExercisesUseCase @Inject constructor(
    private val repository: FitnessRepository
) {
    operator fun invoke(): Flow<List<Exercise>> {
        return repository.getExercises()
    }
}
