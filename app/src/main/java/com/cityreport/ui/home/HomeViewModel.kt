package com.cityreport.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cityreport.data.repository.ReportRepository
import com.cityreport.domain.model.Report
import com.cityreport.domain.model.ReportCategory
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val reportRepository: ReportRepository
) : ViewModel() {

    // Etats UI
    private val _reports = MutableStateFlow<List<Report>>(emptyList())
    val reports: StateFlow<List<Report>> = _reports.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedCategory = MutableStateFlow<ReportCategory?>(null)
    val selectedCategory: StateFlow<ReportCategory?> = _selectedCategory.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        loadReports()
    }

    private fun loadReports() {
        viewModelScope.launch {
            _isLoading.value = true
            // Combine les rapports avec les filtres categorie et recherche
            // Debounce sur la recherche pour éviter les recalculs trop fréquents
            reportRepository.getAllReports()
                .combine(_selectedCategory) { reports, category ->
                    if (category != null) {
                        reports.filter { it.category == category }
                    } else {
                        reports
                    }
                }
                .combine(_searchQuery.debounce(300)) { reports, query ->
                    if (query.isNotBlank()) {
                        reports.filter {
                            it.title.contains(query, ignoreCase = true) ||
                            it.description.contains(query, ignoreCase = true)
                        }
                    } else {
                        reports
                    }
                }
                .collect { filteredReports ->
                    _reports.value = filteredReports
                    _isLoading.value = false
                }
        }
    }

    fun filterByCategory(category: ReportCategory?) {
        _selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearSearch() {
        _searchQuery.value = ""
    }
}
