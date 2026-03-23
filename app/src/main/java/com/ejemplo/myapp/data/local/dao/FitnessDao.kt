package com.ejemplo.myapp.data.local.dao

import androidx.room.*
import com.ejemplo.myapp.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface FitnessDao {
    @Query("SELECT * FROM exercises")
    fun getAllExercises(): Flow<List<ExerciseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExercises(exercises: List<ExerciseEntity>)

    @Query("SELECT * FROM workout_sessions ORDER BY startTime DESC")
    fun getAllSessions(): Flow<List<WorkoutSessionEntity>>

    @Insert
    suspend fun insertSession(session: WorkoutSessionEntity): Long

    @Transaction
    @Query("SELECT * FROM session_exercises WHERE sessionId = :sessionId")
    fun getSessionExercisesWithSets(sessionId: Long): Flow<List<SessionExerciseWithSets>>

    @Insert
    suspend fun insertSessionExercise(sessionExercise: SessionExerciseEntity): Long

    @Insert
    suspend fun insertSet(set: ExerciseSetEntity)

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
