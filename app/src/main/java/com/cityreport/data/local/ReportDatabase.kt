package com.cityreport.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Base de donnees Room pour l'application
 */
@Database(
    entities = [ReportEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ReportDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
}
