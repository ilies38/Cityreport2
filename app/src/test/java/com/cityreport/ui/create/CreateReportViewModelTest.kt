package com.cityreport.ui.create

import android.net.Uri
import com.cityreport.data.repository.ReportRepository
import com.cityreport.domain.model.ReportCategory
import com.google.android.gms.maps.model.LatLng
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

@OptIn(ExperimentalCoroutinesApi::class)
class CreateReportViewModelTest {

    private lateinit var viewModel: CreateReportViewModel
    private lateinit var repository: ReportRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = mockk(relaxed = true)
        viewModel = CreateReportViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `initial state should have empty fields`() {
        // Then
        val state = viewModel.uiState.value
        assertEquals("", state.title)
        assertEquals("", state.description)
        assertEquals(ReportCategory.OTHER, state.category)
        assertNull(state.location)
        assertNull(state.photoUri)
        assertFalse(state.isLoading)
        assertNull(state.error)
        assertFalse(state.isSaved)
    }

    @Test
    fun `updateTitle should update state`() {
        // When
        viewModel.updateTitle("New Title")

        // Then
        assertEquals("New Title", viewModel.uiState.value.title)
    }

    @Test
    fun `updateDescription should update state`() {
        // When
        viewModel.updateDescription("New Description")

        // Then
        assertEquals("New Description", viewModel.uiState.value.description)
    }

    @Test
    fun `updateCategory should update state`() {
        // When
        viewModel.updateCategory(ReportCategory.ROAD)

        // Then
        assertEquals(ReportCategory.ROAD, viewModel.uiState.value.category)
    }

    @Test
    fun `updateLocation should update state`() {
        // When
        val location = LatLng(48.8566, 2.3522)
        viewModel.updateLocation(location)

        // Then
        assertEquals(location, viewModel.uiState.value.location)
    }

    @Test
    fun `updatePhoto should update state`() {
        // When
        val uri = mockk<Uri>()
        viewModel.updatePhoto(uri)

        // Then
        assertEquals(uri, viewModel.uiState.value.photoUri)
    }

    @Test
    fun `clearPhoto should set photoUri to null`() {
        // Given
        val uri = mockk<Uri>()
        viewModel.updatePhoto(uri)

        // When
        viewModel.clearPhoto()

        // Then
        assertNull(viewModel.uiState.value.photoUri)
    }

    @Test
    fun `saveReport with empty title should show error`() {
        // Given
        viewModel.updateTitle("")
        viewModel.updateDescription("Description")
        viewModel.updateLocation(LatLng(48.8566, 2.3522))

        // When
        viewModel.saveReport()

        // Then
        assertNotNull(viewModel.uiState.value.error)
        assertFalse(viewModel.uiState.value.isSaved)
    }

    @Test
    fun `saveReport with empty description should show error`() {
        // Given
        viewModel.updateTitle("Title")
        viewModel.updateDescription("")
        viewModel.updateLocation(LatLng(48.8566, 2.3522))

        // When
        viewModel.saveReport()

        // Then
        assertNotNull(viewModel.uiState.value.error)
        assertFalse(viewModel.uiState.value.isSaved)
    }

    @Test
    fun `saveReport with null location should show error`() {
        // Given
        viewModel.updateTitle("Title")
        viewModel.updateDescription("Description")
        // location reste null

        // When
        viewModel.saveReport()

        // Then
        assertNotNull(viewModel.uiState.value.error)
        assertFalse(viewModel.uiState.value.isSaved)
    }

    @Test
    fun `saveReport with valid data should create report`() = runTest {
        // Given
        viewModel.updateTitle("Valid Title")
        viewModel.updateDescription("Valid Description")
        viewModel.updateCategory(ReportCategory.CLEANLINESS)
        viewModel.updateLocation(LatLng(48.8566, 2.3522))

        coEvery { repository.createReport(any()) } just Runs

        // When
        viewModel.saveReport()
        advanceUntilIdle()

        // Then
        coVerify { repository.createReport(any()) }
        assertTrue(viewModel.uiState.value.isSaved)
        assertNull(viewModel.uiState.value.error)
    }

    @Test
    fun `clearError should clear error state`() {
        // Given
        viewModel.updateTitle("")
        viewModel.saveReport() // Genere une erreur

        // When
        viewModel.clearError()

        // Then
        assertNull(viewModel.uiState.value.error)
    }
}
