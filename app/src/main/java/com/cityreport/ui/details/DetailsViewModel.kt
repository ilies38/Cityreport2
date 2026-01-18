package com.cityreport.ui.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cityreport.data.repository.ReportRepository
import com.cityreport.domain.model.Report
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val reportRepository: ReportRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    // Recuperation de l'ID du rapport depuis les arguments de navigation
    private val reportId: String = checkNotNull(savedStateHandle["reportId"])

    private val _report = MutableStateFlow<Report?>(null)
    val report: StateFlow<Report?> = _report.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isRetrying = MutableStateFlow(false)
    val isRetrying: StateFlow<Boolean> = _isRetrying.asStateFlow()

    private val _isDeleted = MutableStateFlow(false)
    val isDeleted: StateFlow<Boolean> = _isDeleted.asStateFlow()

    init {
        loadReport()
    }

    private fun loadReport() {
        viewModelScope.launch {
            _isLoading.value = true
            val report = reportRepository.getReportById(reportId)
            _report.value = report
            _isLoading.value = false
        }
    }

    fun retrySync() {
        viewModelScope.launch {
            _isRetrying.value = true
            reportRepository.syncPendingReports()
            // Recharger le rapport pour voir le nouveau statut
            loadReport()
            _isRetrying.value = false
        }
    }

    fun deleteReport() {
        viewModelScope.launch {
            reportRepository.deleteReport(reportId)
            _isDeleted.value = true
        }
    }
}
