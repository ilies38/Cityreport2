package com.cityreport.ui.create

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cityreport.domain.model.ReportCategory
import com.cityreport.ui.theme.CityReportTheme
import com.google.android.gms.maps.model.LatLng
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CreateReportScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun createReportScreen_displaysTitle() {
        // Given
        val viewModel = mockk<CreateReportViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(CreateReportUiState())

        // When
        composeTestRule.setContent {
            CityReportTheme {
                CreateReportScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Créer un rapport").assertIsDisplayed()
    }

    @Test
    fun createReportScreen_displaysTitleField() {
        // Given
        val viewModel = mockk<CreateReportViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(CreateReportUiState())

        // When
        composeTestRule.setContent {
            CityReportTheme {
                CreateReportScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Titre *").assertIsDisplayed()
    }

    @Test
    fun createReportScreen_displaysDescriptionField() {
        // Given
        val viewModel = mockk<CreateReportViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(CreateReportUiState())

        // When
        composeTestRule.setContent {
            CityReportTheme {
                CreateReportScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Description *").assertIsDisplayed()
    }

    @Test
    fun createReportScreen_displaysCategoryDropdown() {
        // Given
        val viewModel = mockk<CreateReportViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(CreateReportUiState())

        // When
        composeTestRule.setContent {
            CityReportTheme {
                CreateReportScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Catégorie").assertIsDisplayed()
    }

    @Test
    fun createReportScreen_displaysPhotoSection() {
        // Given
        val viewModel = mockk<CreateReportViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(CreateReportUiState())

        // When
        composeTestRule.setContent {
            CityReportTheme {
                CreateReportScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Photo").assertIsDisplayed()
    }

    @Test
    fun createReportScreen_displaysLocationSection() {
        // Given
        val viewModel = mockk<CreateReportViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(CreateReportUiState())

        // When
        composeTestRule.setContent {
            CityReportTheme {
                CreateReportScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Localisation *").assertIsDisplayed()
    }

    @Test
    fun createReportScreen_displaysSaveButton() {
        // Given
        val viewModel = mockk<CreateReportViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(CreateReportUiState())

        // When
        composeTestRule.setContent {
            CityReportTheme {
                CreateReportScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Enregistrer le rapport").assertIsDisplayed()
    }

    @Test
    fun createReportScreen_displaysCameraButton() {
        // Given
        val viewModel = mockk<CreateReportViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(CreateReportUiState())

        // When
        composeTestRule.setContent {
            CityReportTheme {
                CreateReportScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Caméra").assertIsDisplayed()
    }

    @Test
    fun createReportScreen_displaysGalleryButton() {
        // Given
        val viewModel = mockk<CreateReportViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(CreateReportUiState())

        // When
        composeTestRule.setContent {
            CityReportTheme {
                CreateReportScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Galerie").assertIsDisplayed()
    }

    @Test
    fun createReportScreen_displaysGpsButton() {
        // Given
        val viewModel = mockk<CreateReportViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(CreateReportUiState())

        // When
        composeTestRule.setContent {
            CityReportTheme {
                CreateReportScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Utiliser ma position actuelle").assertIsDisplayed()
    }

    @Test
    fun createReportScreen_showsError_whenValidationFails() {
        // Given
        val viewModel = mockk<CreateReportViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(
            CreateReportUiState(error = "Le titre est obligatoire")
        )

        // When
        composeTestRule.setContent {
            CityReportTheme {
                CreateReportScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Le titre est obligatoire").assertIsDisplayed()
    }

    @Test
    fun createReportScreen_showsLoadingState() {
        // Given
        val viewModel = mockk<CreateReportViewModel>(relaxed = true)
        every { viewModel.uiState } returns MutableStateFlow(
            CreateReportUiState(isLoading = true)
        )

        // When
        composeTestRule.setContent {
            CityReportTheme {
                CreateReportScreen(
                    onNavigateBack = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Enregistrement...").assertIsDisplayed()
    }
}
