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

    // User Profile
    @Query("SELECT * FROM users WHERE id = 1")
    fun getUser(): Flow<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    // Fruit Challenges
    @Query("SELECT * FROM fruit_challenges")
    fun getAllChallenges(): Flow<List<FruitChallengeEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChallenges(challenges: List<FruitChallengeEntity>)

    @Query("UPDATE fruit_challenges SET progress = :progress, isCompleted = :isCompleted WHERE id = :id")
    suspend fun updateChallengeProgress(id: String, progress: Float, isCompleted: Boolean)
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
