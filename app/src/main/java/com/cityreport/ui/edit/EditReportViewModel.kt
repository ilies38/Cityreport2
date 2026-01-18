package com.cityreport.ui.edit

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
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
import javax.inject.Inject

/**
 * Etat UI pour l'ecran d'edition de rapport
 */
data class EditReportUiState(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val category: ReportCategory = ReportCategory.OTHER,
    val location: LatLng? = null,
    val photoUrl: String? = null,
    val newPhotoUri: Uri? = null,
    val timestamp: Long = 0L,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val error: String? = null,
    val isSaved: Boolean = false
)

@HiltViewModel
class EditReportViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val reportId: String = checkNotNull(savedStateHandle["reportId"])

    private val _uiState = MutableStateFlow(EditReportUiState())
    val uiState: StateFlow<EditReportUiState> = _uiState.asStateFlow()

    init {
        loadReport()
    }

    private fun loadReport() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val report = reportRepository.getReportById(reportId)
            if (report != null) {
                _uiState.update {
                    it.copy(
                        id = report.id,
                        title = report.title,
                        description = report.description,
                        category = report.category,
                        location = LatLng(report.latitude, report.longitude),
                        photoUrl = report.photoUrl,
                        timestamp = report.timestamp,
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "Rapport non trouve"
                    )
                }
            }
        }
    }

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
        _uiState.update { it.copy(newPhotoUri = uri) }
    }

    fun clearPhoto() {
        _uiState.update { it.copy(newPhotoUri = null, photoUrl = null) }
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
            _uiState.update { it.copy(isSaving = true, error = null) }

            try {
                // Upload de la nouvelle photo si presente
                var photoUrl: String? = state.photoUrl
                if (state.newPhotoUri != null) {
                    val uploadResult = reportRepository.uploadImage(state.newPhotoUri, state.id)
                    if (uploadResult.isSuccess) {
                        photoUrl = uploadResult.getOrNull()
                    } else {
                        // Si l'upload echoue, utiliser l'URI locale
                        photoUrl = state.newPhotoUri.toString()
                    }
                }

                // Mise a jour du rapport
                val report = Report(
                    id = state.id,
                    title = state.title,
                    description = state.description,
                    category = state.category,
                    latitude = state.location.latitude,
                    longitude = state.location.longitude,
                    photoUrl = photoUrl,
                    timestamp = state.timestamp,
                    syncStatus = SyncStatus.PENDING
                )

                reportRepository.updateReport(report)
                _uiState.update { it.copy(isSaving = false, isSaved = true) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isSaving = false,
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
