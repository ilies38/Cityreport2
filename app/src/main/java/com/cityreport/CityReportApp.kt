package com.cityreport

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import com.cityreport.util.LocaleHelper
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

/**
 * Classe Application principale avec Hilt
 * Optimisee pour un demarrage rapide
 */
@HiltAndroidApp
class CityReportApp : Application(), Configuration.Provider, ImageLoaderFactory {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        // Appliquer la langue depuis SharedPreferences (lecture synchrone rapide)
        // Pas de runBlocking ni DataStore ici pour eviter le lag
        val languageCode = LocaleHelper.getPersistedLocale(this)
        LocaleHelper.setLocale(languageCode)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    /**
     * Configuration optimisee de Coil pour le chargement d'images
     */
    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            // Cache memoire (25% de la memoire disponible)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.25)
                    .build()
            }
            // Cache disque (50 MB)
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizeBytes(50 * 1024 * 1024) // 50 MB
                    .build()
            }
            // Politiques de cache agressives
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .networkCachePolicy(CachePolicy.ENABLED)
            // Crossfade pour des transitions fluides
            .crossfade(true)
            .crossfade(150)
            // Respecter les en-tetes de cache HTTP
            .respectCacheHeaders(true)
            .build()
    }
}
