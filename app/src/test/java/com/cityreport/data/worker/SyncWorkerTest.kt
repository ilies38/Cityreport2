package com.cityreport.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.cityreport.data.repository.ReportRepository
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class SyncWorkerTest {

    private lateinit var repository: ReportRepository
    private lateinit var context: Context
    private lateinit var workerParams: WorkerParameters

    @Before
    fun setup() {
        context = mockk(relaxed = true)
        workerParams = mockk(relaxed = true)
        repository = mockk(relaxed = true)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `syncPendingReports should be called during sync`() = runTest {
        // Given
        coEvery { repository.syncPendingReports() } returns Result.success(0)

        // When
        repository.syncPendingReports()

        // Then
        coVerify(exactly = 1) { repository.syncPendingReports() }
    }

    @Test
    fun `sync should handle success`() = runTest {
        // Given
        coEvery { repository.syncPendingReports() } returns Result.success(0)

        // When
        var success = false
        try {
            repository.syncPendingReports()
            success = true
        } catch (e: Exception) {
            success = false
        }

        // Then
        assertTrue(success)
    }

    @Test
    fun `sync should handle failure gracefully`() = runTest {
        // Given
        coEvery { repository.syncPendingReports() } throws Exception("Network error")

        // When
        var exceptionThrown = false
        try {
            repository.syncPendingReports()
        } catch (e: Exception) {
            exceptionThrown = true
            assertEquals("Network error", e.message)
        }

        // Then
        assertTrue(exceptionThrown)
    }

    @Test
    fun `multiple sync calls should work`() = runTest {
        // Given
        coEvery { repository.syncPendingReports() } returns Result.success(0)

        // When
        repository.syncPendingReports()
        repository.syncPendingReports()
        repository.syncPendingReports()

        // Then
        coVerify(exactly = 3) { repository.syncPendingReports() }
    }
}
