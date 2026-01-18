package com.cityreport.data.remote

import android.net.Uri
import com.cityreport.domain.model.Report
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Source de donnees Firebase (Firestore + Storage)
 */
@Singleton
class FirebaseDataSource @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) {

    private val reportsCollection = firestore.collection("reports")

    /**
     * Sauvegarde un rapport dans Firestore
     */
    suspend fun saveReport(report: Report): Result<Unit> {
        return try {
            val data = mapOf(
                "id" to report.id,
                "title" to report.title,
                "description" to report.description,
                "category" to report.category.name,
                "latitude" to report.latitude,
                "longitude" to report.longitude,
                "photoUrl" to report.photoUrl,
                "timestamp" to report.timestamp
            )
            reportsCollection.document(report.id).set(data).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Upload une image dans Firebase Storage
     * @return URL de l'image uploadee
     */
    suspend fun uploadImage(localUri: Uri, reportId: String): Result<String> {
        return try {
            val ref = storage.reference.child("reports/$reportId.jpg")
            ref.putFile(localUri).await()
            val downloadUrl = ref.downloadUrl.await()
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Recupere tous les rapports depuis Firestore
     */
    suspend fun getAllReports(): Result<List<Map<String, Any>>> {
        return try {
            val snapshot = reportsCollection.get().await()
            val reports = snapshot.documents.mapNotNull { it.data }
            Result.success(reports)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
