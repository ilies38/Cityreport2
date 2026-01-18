package com.cityreport.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.room.Room
import com.cityreport.data.local.ReportDao
import com.cityreport.data.local.ReportDatabase
import com.cityreport.ui.settings.dataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Module Hilt pour les dependances de l'application
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideReportDatabase(
        @ApplicationContext context: Context
    ): ReportDatabase {
        return Room.databaseBuilder(
            context,
            ReportDatabase::class.java,
            "cityreport_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideReportDao(database: ReportDatabase): ReportDao {
        return database.reportDao()
    }

    @Provides
    @Singleton
    fun provideDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> {
        return context.dataStore
    }
}
