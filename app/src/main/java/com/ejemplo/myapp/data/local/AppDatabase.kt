package com.ejemplo.myapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ejemplo.myapp.data.local.converters.StringListConverter
import com.ejemplo.myapp.data.local.dao.FitnessDao
import com.ejemplo.myapp.data.local.entities.*

@Database(
    entities = [
        ExerciseEntity::class,
        WorkoutSessionEntity::class,
        SessionExerciseEntity::class,
        ExerciseSetEntity::class
    ],
    version = 2, // Subimos a la versión 2
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
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
                )
                .fallbackToDestructiveMigration() // Esto evita que la app se cierre si cambias el esquema
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
