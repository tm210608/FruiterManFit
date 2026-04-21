package com.ejemplo.myapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ejemplo.myapp.data.local.converters.StringListConverter
import com.ejemplo.myapp.data.local.dao.FitnessDao
import com.ejemplo.myapp.data.local.entities.*
import com.ejemplo.myapp.data.local.DatabaseConfig
import com.ejemplo.myapp.data.local.MigrationProvider

@Database(
    entities = [
        ExerciseEntity::class,
        WorkoutSessionEntity::class,
        SessionExerciseEntity::class,
        ExerciseSetEntity::class,
        UserEntity::class,
        FruitChallengeEntity::class
    ],
    version = DatabaseConfig.DATABASE_VERSION,
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
                    DatabaseConfig.DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .addMigrations(*MigrationProvider.allMigrations)
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
