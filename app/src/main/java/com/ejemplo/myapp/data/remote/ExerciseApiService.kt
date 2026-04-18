package com.ejemplo.myapp.data.remote

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET

interface ExerciseApiService {
    // Apuntamos al archivo JSON que contiene los 1327 ejercicios con el esquema original de ExerciseDB
    @GET("exercises.json")
    suspend fun getFullDataset(): List<ExerciseDto>

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
