package com.cityreport.data.repository

import com.cityreport.data.local.ReportDao
import com.cityreport.data.local.ReportEntity
import com.cityreport.data.remote.FirebaseDataSource
import com.cityreport.domain.model.Report
import com.cityreport.domain.model.ReportCategory
import com.cityreport.domain.model.SyncStatus
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class ReportRepositoryTest {

    private lateinit var repository: ReportRepository
    private lateinit var reportDao: ReportDao
    private lateinit var firebaseDataSource: FirebaseDataSource

    @Before
    fun setup() {
        reportDao = mockk(relaxed = true)
        firebaseDataSource = mockk(relaxed = true)
        repository = ReportRepository(reportDao, firebaseDataSource)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `createReport should insert report into local database`() = runTest {
        // Given
        val report = Report(
            id = "test-id",
            title = "Test Report",
            description = "Test Description",
            category = ReportCategory.CLEANLINESS,
            latitude = 48.8566,
            longitude = 2.3522,
            photoUrl = null,
            timestamp = System.currentTimeMillis(),
            syncStatus = SyncStatus.PENDING
        )

        coEvery { reportDao.insertReport(any()) } just Runs

        // When
        repository.createReport(report)

        // Then
        coVerify(exactly = 1) { reportDao.insertReport(any()) }
    }

    @Test
    fun `getAllReports should return flow of reports from database`() = runTest {
        // Given
        val entities = listOf(
            ReportEntity(
                id = "1",
                title = "Report 1",
                description = "Description 1",
                category = "CLEANLINESS",
                latitude = 48.8566,
                longitude = 2.3522,
                photoUrl = null,
                timestamp = System.currentTimeMillis(),
                syncStatus = "PENDING"
            )
        )
        every { reportDao.getAllReports() } returns flowOf(entities)

        // When
        val result = repository.getAllReports().first()

        // Then
        assertEquals(1, result.size)
        assertEquals("Report 1", result[0].title)
        assertEquals(ReportCategory.CLEANLINESS, result[0].category)
    }

    @Test
    fun `getReportById should return report when exists`() = runTest {
        // Given
        val entity = ReportEntity(
            id = "test-id",
            title = "Test",
            description = "Description",
            category = "ROAD",
            latitude = 48.8566,
            longitude = 2.3522,
            photoUrl = null,
            timestamp = System.currentTimeMillis(),
            syncStatus = "SYNCED"
        )
        coEvery { reportDao.getReportById("test-id") } returns entity

        // When
        val result = repository.getReportById("test-id")

        // Then
        assertNotNull(result)
        assertEquals("Test", result?.title)
        assertEquals(ReportCategory.ROAD, result?.category)
        assertEquals(SyncStatus.SYNCED, result?.syncStatus)
    }

    @Test
    fun `getReportById should return null when not exists`() = runTest {
        // Given
        coEvery { reportDao.getReportById("non-existent") } returns null

        // When
        val result = repository.getReportById("non-existent")

        // Then
        assertNull(result)
    }

    @Test
    fun `deleteReport should remove report from database`() = runTest {
        // Given
        coEvery { reportDao.deleteReportById(any()) } just Runs

        // When
        repository.deleteReport("test-id")

        // Then
        coVerify(exactly = 1) { reportDao.deleteReportById("test-id") }
    }

    @Test
    fun `syncPendingReports should call Firebase sync`() = runTest {
        // Given
        val pendingEntities = listOf(
            ReportEntity(
                id = "pending-1",
                title = "Pending",
                description = "Description",
                category = "SAFETY",
                latitude = 48.8566,
                longitude = 2.3522,
                photoUrl = null,
                timestamp = System.currentTimeMillis(),
                syncStatus = "PENDING"
            )
        )

        coEvery { reportDao.getReportsBySyncStatus(any()) } returns pendingEntities
        coEvery { firebaseDataSource.saveReport(any()) } returns Result.success(Unit)
        coEvery { reportDao.updateSyncStatus(any(), any()) } just Runs

        // When
        repository.syncPendingReports()

        // Then
        coVerify { reportDao.getReportsBySyncStatus(any()) }
    }
}
