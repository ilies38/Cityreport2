package com.cityreport.domain.model

/**
 * Modele de donnees pour un signalement
 */
data class Report(
    val id: String,
    val title: String,
    val description: String,
    val category: ReportCategory,
    val latitude: Double,
    val longitude: Double,
    val photoUrl: String?,
    val timestamp: Long,
    val syncStatus: SyncStatus
)
