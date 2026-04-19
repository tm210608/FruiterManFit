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

    val allMigrations = arrayOf(
        MIGRATION_5_6,
        MIGRATION_6_7
    )
}
