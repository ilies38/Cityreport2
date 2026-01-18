package com.cityreport.ui.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cityreport.data.repository.ReportRepository
import com.cityreport.util.LocaleHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

// Extension pour DataStore (garde pour compatibilite avec autres usages)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

// Enum pour les langues supportees
enum class AppLanguage(val code: String, val displayName: String) {
    FRENCH("fr", "Français"),
    ENGLISH("en", "English"),
    ARABIC("ar", "العربية")
}

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val reportRepository: ReportRepository
) : ViewModel() {

    // Lecture initiale depuis SharedPreferences (rapide, synchrone)
    private val _currentLanguage = MutableStateFlow(
        getLanguageFromCode(LocaleHelper.getPersistedLocale(context))
    )
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    private val _syncMessage = MutableStateFlow<String?>(null)
    val syncMessage: StateFlow<String?> = _syncMessage.asStateFlow()

    // Evenement pour redemarrer l'activite
    private val _shouldRestartActivity = MutableStateFlow(false)
    val shouldRestartActivity: StateFlow<Boolean> = _shouldRestartActivity.asStateFlow()

    private fun getLanguageFromCode(code: String): AppLanguage {
        return AppLanguage.entries.find { it.code == code } ?: AppLanguage.FRENCH
    }

    fun setLanguage(language: AppLanguage) {
        if (_currentLanguage.value == language) return // Eviter les changements inutiles

        viewModelScope.launch {
            // 1. Mettre a jour l'etat local
            _currentLanguage.value = language

            // 2. Sauvegarder dans SharedPreferences (seul stockage utilise maintenant)
            LocaleHelper.persistLocale(context, language.code)

            // 3. Appliquer la locale
            LocaleHelper.setLocale(context, language.code)

            // 4. Declencher le redemarrage de l'activite
            _shouldRestartActivity.value = true
        }
    }

    fun onActivityRestarted() {
        _shouldRestartActivity.value = false
    }

    fun syncNow() {
        if (_isSyncing.value) return // Eviter les doubles appels

        viewModelScope.launch {
            _isSyncing.value = true
            _syncMessage.value = null
            try {
                reportRepository.syncPendingReports()
                _syncMessage.value = "Synchronisation réussie"
            } catch (e: Exception) {
                _syncMessage.value = "Erreur: ${e.message}"
            } finally {
                _isSyncing.value = false
            }
        }
    }

    fun clearSyncMessage() {
        _syncMessage.value = null
    }
}
