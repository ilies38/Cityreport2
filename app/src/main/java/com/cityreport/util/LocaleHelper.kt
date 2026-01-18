package com.cityreport.util

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

/**
 * Helper pour gerer les changements de langue dans l'application
 */
object LocaleHelper {

    private const val PREFS_NAME = "settings"
    private const val KEY_LANGUAGE = "app_language"

    /**
     * Applique la langue specifiee a l'application
     * Utilise AppCompatDelegate pour les versions modernes d'Android
     */
    fun setLocale(languageCode: String) {
        val localeList = LocaleListCompat.forLanguageTags(languageCode)
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    /**
     * Applique la locale au contexte et retourne le nouveau contexte
     * A utiliser dans attachBaseContext
     */
    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = Configuration(context.resources.configuration)
        config.setLocale(locale)

        // Support RTL pour l'arabe
        if (languageCode == "ar") {
            config.setLayoutDirection(locale)
        }

        return context.createConfigurationContext(config)
    }

    /**
     * Recupere le code de langue actuel
     */
    fun getCurrentLanguageCode(): String {
        val locales = AppCompatDelegate.getApplicationLocales()
        return if (locales.isEmpty) {
            Locale.getDefault().language
        } else {
            locales[0]?.language ?: "fr"
        }
    }

    /**
     * Recupere la locale persistee depuis SharedPreferences
     * Utile pour lecture rapide au demarrage avant que DataStore soit charge
     */
    fun getPersistedLocale(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_LANGUAGE, "fr") ?: "fr"
    }

    /**
     * Persiste la locale dans SharedPreferences
     * Pour lecture rapide au demarrage
     */
    fun persistLocale(context: Context, languageCode: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_LANGUAGE, languageCode).apply()
    }

    /**
     * Applique la langue au contexte (pour les anciennes versions d'Android)
     * A utiliser dans attachBaseContext si necessaire
     */
    fun applyLocaleToContext(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val config = context.resources.configuration
        config.setLocale(locale)
        config.setLayoutDirection(locale)

        return context.createConfigurationContext(config)
    }
}
