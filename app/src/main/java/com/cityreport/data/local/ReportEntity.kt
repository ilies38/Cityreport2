package com.cityreport.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.cityreport.domain.model.Report
import com.cityreport.domain.model.ReportCategory
import com.cityreport.domain.model.SyncStatus

/**
 * Entite Room pour stocker les rapports localement
 */
@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val category: String,
    val latitude: Double,
    val longitude: Double,
    val photoUrl: String?,
    val timestamp: Long,
    val syncStatus: String
) {
    // Conversion vers le modele domain
    fun toReport(): Report {
        return Report(
            id = id,
            title = title,
            description = description,
            category = ReportCategory.valueOf(category),
            latitude = latitude,
            longitude = longitude,
            photoUrl = photoUrl,
            timestamp = timestamp,
            syncStatus = SyncStatus.valueOf(syncStatus)
        )
    }

    companion object {
        // Conversion depuis le modele domain
        fun fromReport(report: Report): ReportEntity {
            return ReportEntity(
                id = report.id,
                title = report.title,
                description = report.description,
                category = report.category.name,
                latitude = report.latitude,
                longitude = report.longitude,
                photoUrl = report.photoUrl,
                timestamp = report.timestamp,
                syncStatus = report.syncStatus.name
            )
        }
    }
}
