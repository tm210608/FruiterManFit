package com.ejemplo.myapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface ExerciseApiService {
    @GET("exercises")
    suspend fun getAllExercises(
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") host: String = "exercisedb.p.rapidapi.com",
        @Query("limit") limit: Int = 500
    ): List<ExerciseDto>

    @GET("exercises/bodyPartList")
    suspend fun getBodyPartList(
        @Header("X-RapidAPI-Key") apiKey: String,
        @Header("X-RapidAPI-Host") host: String = "exercisedb.p.rapidapi.com"
    ): List<String>

    @GET("exercises/bodyPart/{bodyPart}")
    suspend fun getExercisesByBodyPart(
        @Header("X-RapidAPI-Key") apiKey: String,
        @Path("bodyPart") bodyPart: String,
        @Header("X-RapidAPI-Host") host: String = "exercisedb.p.rapidapi.com"
    ): List<ExerciseDto>
}

data class ExerciseDto(
    val id: String?,
    val name: String?,
    val bodyPart: String?,
    val equipment: String?,
    val gifUrl: String?,
    val target: String?,
    val secondaryMuscles: List<String>?,
    val instructions: List<String>?
)
