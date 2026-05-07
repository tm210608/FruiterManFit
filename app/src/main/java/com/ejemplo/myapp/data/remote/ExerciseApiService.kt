package com.ejemplo.myapp.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

interface ExerciseApiService {
    // WorkoutX API endpoint - devuelve todos los ejercicios con GIFs
    @GET("exercises")
    suspend fun getFullDataset(): List<ExerciseDto>

    // Filtrar por parte del cuerpo (opcional)
    @GET("exercises/bodyPart/{bodyPart}")
    suspend fun getExercisesByBodyPart(@retrofit2.http.Path("bodyPart") bodyPart: String): List<ExerciseDto>

    // Funciones legacy
    suspend fun getAllExercises(): List<ExerciseDto> = getFullDataset()
}

data class ExerciseDto(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("bodyPart") val bodyPart: String?,
    @SerializedName("equipment") val equipment: String?,
    @SerializedName("gifUrl") val gifUrl: String?,
    @SerializedName("target") val target: String?,
    @SerializedName("secondaryMuscles") val secondaryMuscles: List<String>?,
    @SerializedName("instructions") val instructions: Any?,
    @SerializedName("images") val images: List<String>?,
    @SerializedName("description") val description: String?,
    @SerializedName("difficulty") val difficulty: String?,
    @SerializedName("category") val category: String?
)
