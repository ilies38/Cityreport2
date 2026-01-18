package com.cityreport.data.repository

import android.net.Uri
import com.cityreport.data.local.ReportDao
import com.cityreport.data.local.ReportEntity
import com.cityreport.data.remote.FirebaseDataSource
import com.cityreport.domain.model.Report
import com.cityreport.domain.model.ReportCategory
import com.cityreport.domain.model.SyncStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository pour gerer les rapports (local + remote)
 */
@Singleton
class ReportRepository @Inject constructor(
    private val reportDao: ReportDao,
    private val firebaseDataSource: FirebaseDataSource
) {

    /**
     * Recupere tous les rapports (Flow reactif)
     */
    fun getAllReports(): Flow<List<Report>> {
        return reportDao.getAllReports().map { entities ->
            entities.map { it.toReport() }
        }
    }

    /**
     * Recupere un rapport par son ID
     */
    suspend fun getReportById(id: String): Report? {
        return reportDao.getReportById(id)?.toReport()
    }

    /**
     * Recupere les rapports par categorie
     */
    fun getReportsByCategory(category: ReportCategory): Flow<List<Report>> {
        return reportDao.getReportsByCategory(category.name).map { entities ->
            entities.map { it.toReport() }
        }
    }

    /**
     * Cree un nouveau rapport (local d'abord, puis sync)
     */
    suspend fun createReport(report: Report) {
        // Sauvegarde locale avec statut PENDING
        val pendingReport = report.copy(syncStatus = SyncStatus.PENDING)
        reportDao.insertReport(ReportEntity.fromReport(pendingReport))
    }

    /**
     * Met a jour un rapport existant
     */
    suspend fun updateReport(report: Report) {
        // Sauvegarde locale avec statut PENDING pour re-synchronisation
        val pendingReport = report.copy(syncStatus = SyncStatus.PENDING)
        reportDao.updateReport(ReportEntity.fromReport(pendingReport))
    }

    /**
     * Supprime un rapport
     */
    suspend fun deleteReport(id: String) {
        reportDao.deleteReportById(id)
    }

    /**
     * Upload une image et retourne l'URL
     */
    suspend fun uploadImage(localUri: Uri, reportId: String): Result<String> {
        return firebaseDataSource.uploadImage(localUri, reportId)
    }

    /**
     * Synchronise les rapports en attente vers Firebase
     */
    suspend fun syncPendingReports(): Result<Int> {
        val pendingReports = reportDao.getReportsBySyncStatus(SyncStatus.PENDING.name)
        var syncedCount = 0

        for (entity in pendingReports) {
            val report = entity.toReport()
            val result = firebaseDataSource.saveReport(report)

            if (result.isSuccess) {
                reportDao.updateSyncStatus(report.id, SyncStatus.SYNCED.name)
                syncedCount++
            } else {
                reportDao.updateSyncStatus(report.id, SyncStatus.FAILED.name)
            }
        }

        return Result.success(syncedCount)
    }

    /**
     * Recupere les rapports non synchronises
     */
    suspend fun getPendingReports(): List<Report> {
        return reportDao.getReportsBySyncStatus(SyncStatus.PENDING.name)
            .map { it.toReport() }
    }
}
