package com.ejemplo.myapp.data.local.dao

import androidx.room.*
import com.ejemplo.myapp.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FitnessDao {
    @Query("SELECT COUNT(*) FROM exercises")
    suspend fun getExerciseCount(): Int

    @Query("SELECT * FROM exercises")
    fun getAllExercises(): Flow<List<ExerciseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    @Query("DELETE FROM exercises")
    suspend fun clearExercises()

    @Query("SELECT * FROM exercises WHERE id = :exerciseId LIMIT 1")
    suspend fun getExerciseById(exerciseId: String): ExerciseEntity?

    @Transaction
    @Query("SELECT * FROM workout_sessions ORDER BY startTime DESC")
    fun getFullSessions(): Flow<List<FullWorkoutSession>>

    @Insert
    suspend fun insertSession(session: WorkoutSessionEntity): Long

    @Transaction
    @Query("SELECT * FROM session_exercises WHERE sessionId = :sessionId")
    fun getSessionExercisesWithSets(sessionId: Long): Flow<List<SessionExerciseWithSets>>

    @Insert
    suspend fun insertSessionExercise(sessionExercise: SessionExerciseEntity): Long

    @Insert
    suspend fun insertSet(set: ExerciseSetEntity)

    @Query("SELECT * FROM exercise_sets")
    fun getAllSets(): Flow<List<ExerciseSetEntity>>

    @Update
    suspend fun updateSet(set: ExerciseSetEntity)

    @Delete
    suspend fun deleteSet(set: ExerciseSetEntity)
}

data class SessionExerciseWithSets(
    @Embedded val sessionExercise: SessionExerciseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "sessionExerciseId"
    )
    val sets: List<ExerciseSetEntity>
)

data class FullWorkoutSession(
    @Embedded val session: WorkoutSessionEntity,
    @Relation(
        entity = SessionExerciseEntity::class,
        parentColumn = "id",
        entityColumn = "sessionId"
    )
    val exercises: List<SessionExerciseWithSets>
)
