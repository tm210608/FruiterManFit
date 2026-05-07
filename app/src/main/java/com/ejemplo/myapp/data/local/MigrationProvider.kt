package com.ejemplo.myapp.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object MigrationProvider {

    // Migración de la versión 5 a la 6: Añadir la tabla de retos
    val MIGRATION_5_6 = object : Migration(5, 6) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS `fruit_challenges` (
                    `id` TEXT NOT NULL, 
                    `title` TEXT NOT NULL, 
                    `description` TEXT NOT NULL, 
                    `iconType` TEXT NOT NULL, 
                    `progress` REAL NOT NULL, 
                    `target` REAL NOT NULL, 
                    `isCompleted` INTEGER NOT NULL, 
                    `category` TEXT NOT NULL, 
                    PRIMARY KEY(`id`)
                )
            """.trimIndent())
        }
    }

    // Migración de la versión 6 a la 7: Añadir email y password a la tabla users
    val MIGRATION_6_7 = object : Migration(6, 7) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE `users` ADD COLUMN `email` TEXT NOT NULL DEFAULT ''")
            db.execSQL("ALTER TABLE `users` ADD COLUMN `password` TEXT NOT NULL DEFAULT ''")
        }
    }

    // Migración de la versión 8 a la 9: Añadir campos de sync, timestamps y userUuid/passwordHash
    val MIGRATION_8_9 = object : Migration(8, 9) {
        override fun migrate(db: SupportSQLiteDatabase) {
            // workout_sessions
            db.execSQL("ALTER TABLE `workout_sessions` ADD COLUMN `remoteId` TEXT DEFAULT NULL")
            db.execSQL("ALTER TABLE `workout_sessions` ADD COLUMN `syncStatus` TEXT NOT NULL DEFAULT 'SYNCED'")
            db.execSQL("ALTER TABLE `workout_sessions` ADD COLUMN `createdAt` INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE `workout_sessions` ADD COLUMN `updatedAt` INTEGER NOT NULL DEFAULT 0")

            // session_exercises
            db.execSQL("ALTER TABLE `session_exercises` ADD COLUMN `remoteId` TEXT DEFAULT NULL")
            db.execSQL("ALTER TABLE `session_exercises` ADD COLUMN `syncStatus` TEXT NOT NULL DEFAULT 'SYNCED'")
            db.execSQL("ALTER TABLE `session_exercises` ADD COLUMN `createdAt` INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE `session_exercises` ADD COLUMN `updatedAt` INTEGER NOT NULL DEFAULT 0")

            // exercise_sets
            db.execSQL("ALTER TABLE `exercise_sets` ADD COLUMN `remoteId` TEXT DEFAULT NULL")
            db.execSQL("ALTER TABLE `exercise_sets` ADD COLUMN `syncStatus` TEXT NOT NULL DEFAULT 'SYNCED'")
            db.execSQL("ALTER TABLE `exercise_sets` ADD COLUMN `createdAt` INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE `exercise_sets` ADD COLUMN `updatedAt` INTEGER NOT NULL DEFAULT 0")

            // exercises
            db.execSQL("ALTER TABLE `exercises` ADD COLUMN `remoteId` TEXT DEFAULT NULL")
            db.execSQL("ALTER TABLE `exercises` ADD COLUMN `createdAt` INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE `exercises` ADD COLUMN `updatedAt` INTEGER NOT NULL DEFAULT 0")

            // users
            db.execSQL("ALTER TABLE `users` ADD COLUMN `userUuid` TEXT NOT NULL DEFAULT ''")
            db.execSQL("UPDATE `users` SET `userUuid` = lower(hex(randomblob(16)))")
            db.execSQL("ALTER TABLE `users` ADD COLUMN `passwordHash` TEXT NOT NULL DEFAULT ''")
            db.execSQL("ALTER TABLE `users` ADD COLUMN `remoteId` TEXT DEFAULT NULL")
            db.execSQL("ALTER TABLE `users` ADD COLUMN `createdAt` INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE `users` ADD COLUMN `updatedAt` INTEGER NOT NULL DEFAULT 0")

            // fruit_challenges
            db.execSQL("ALTER TABLE `fruit_challenges` ADD COLUMN `remoteId` TEXT DEFAULT NULL")
            db.execSQL("ALTER TABLE `fruit_challenges` ADD COLUMN `syncStatus` TEXT NOT NULL DEFAULT 'SYNCED'")
            db.execSQL("ALTER TABLE `fruit_challenges` ADD COLUMN `createdAt` INTEGER NOT NULL DEFAULT 0")
            db.execSQL("ALTER TABLE `fruit_challenges` ADD COLUMN `updatedAt` INTEGER NOT NULL DEFAULT 0")
        }
    }

    val allMigrations = arrayOf(
        MIGRATION_5_6,
        MIGRATION_6_7,
        MIGRATION_8_9
    )
}
