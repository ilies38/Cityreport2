package com.cityreport.ui.create

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cityreport.data.repository.ReportRepository
import com.cityreport.domain.model.Report
import com.cityreport.domain.model.ReportCategory
import com.cityreport.domain.model.SyncStatus
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

/**
 * Etat UI pour l'ecran de creation de rapport
 */
data class CreateReportUiState(
    val title: String = "",
    val description: String = "",
    val category: ReportCategory = ReportCategory.OTHER,
    val location: LatLng? = null,
    val photoUri: Uri? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false
)

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReportUiState())
    val uiState: StateFlow<CreateReportUiState> = _uiState.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun updateDescription(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun updateCategory(category: ReportCategory) {
        _uiState.update { it.copy(category = category) }
    }

    fun updateLocation(latLng: LatLng) {
        _uiState.update { it.copy(location = latLng) }
    }

    fun updatePhoto(uri: Uri) {
        _uiState.update { it.copy(photoUri = uri) }
    }

    fun clearPhoto() {
        _uiState.update { it.copy(photoUri = null) }
    }

    fun saveReport() {
        val state = _uiState.value

        // Validation des champs obligatoires
        if (state.title.isBlank()) {
            _uiState.update { it.copy(error = "Le titre est obligatoire") }
            return
        }
        if (state.description.isBlank()) {
            _uiState.update { it.copy(error = "La description est obligatoire") }
            return
        }
        if (state.location == null) {
            _uiState.update { it.copy(error = "La localisation est obligatoire") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }

            try {
                val reportId = UUID.randomUUID().toString()

                // Upload de la photo si presente
                var photoUrl: String? = null
                if (state.photoUri != null) {
                    val uploadResult = reportRepository.uploadImage(state.photoUri, reportId)
                    if (uploadResult.isSuccess) {
                        photoUrl = uploadResult.getOrNull()
                    }
                    // Si l'upload echoue, on continue sans photo en ligne
                    // La photo locale sera synchronisee plus tard
                }

                // Creation du rapport
                val report = Report(
                    id = reportId,
                    title = state.title,
                    description = state.description,
                    category = state.category,
                    latitude = state.location.latitude,
                    longitude = state.location.longitude,
                    photoUrl = photoUrl ?: state.photoUri?.toString(),
                    timestamp = System.currentTimeMillis(),
                    syncStatus = SyncStatus.PENDING
                )

                reportRepository.createReport(report)
                _uiState.update { it.copy(isLoading = false, isSaved = true) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Erreur lors de l'enregistrement: ${e.message}"
                    )
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
