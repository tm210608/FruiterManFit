package com.ejemplo.myapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ejemplo.myapp.data.local.dao.FitnessDao
import com.ejemplo.myapp.data.local.entities.*

@Database(
    entities = [
        ExerciseEntity::class,
        WorkoutSessionEntity::class,
        SessionExerciseEntity::class,
        ExerciseSetEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fitnessDao(): FitnessDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fruiterman_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
