package com.cityreport.ui.home

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.cityreport.domain.model.Report
import com.cityreport.domain.model.ReportCategory
import com.cityreport.domain.model.SyncStatus
import com.cityreport.ui.theme.CityReportTheme
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_displaysAppTitle() {
        // Given
        val viewModel = mockk<HomeViewModel>(relaxed = true)
        every { viewModel.reports } returns MutableStateFlow(emptyList())
        every { viewModel.isLoading } returns MutableStateFlow(false)
        every { viewModel.selectedCategory } returns MutableStateFlow(null)
        every { viewModel.searchQuery } returns MutableStateFlow("")

        // When
        composeTestRule.setContent {
            CityReportTheme {
                HomeScreen(
                    onNavigateToCreate = {},
                    onNavigateToDetails = {},
                    onNavigateToMap = {},
                    onNavigateToSettings = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("City Report").assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysEmptyState_whenNoReports() {
        // Given
        val viewModel = mockk<HomeViewModel>(relaxed = true)
        every { viewModel.reports } returns MutableStateFlow(emptyList())
        every { viewModel.isLoading } returns MutableStateFlow(false)
        every { viewModel.selectedCategory } returns MutableStateFlow(null)
        every { viewModel.searchQuery } returns MutableStateFlow("")

        // When
        composeTestRule.setContent {
            CityReportTheme {
                HomeScreen(
                    onNavigateToCreate = {},
                    onNavigateToDetails = {},
                    onNavigateToMap = {},
                    onNavigateToSettings = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Aucun rapport").assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysReportsList_whenReportsExist() {
        // Given
        val reports = listOf(
            Report(
                id = "1",
                title = "Test Report",
                description = "Description",
                category = ReportCategory.CLEANLINESS,
                latitude = 48.8566,
                longitude = 2.3522,
                syncStatus = SyncStatus.SYNCED
            )
        )
        val viewModel = mockk<HomeViewModel>(relaxed = true)
        every { viewModel.reports } returns MutableStateFlow(reports)
        every { viewModel.isLoading } returns MutableStateFlow(false)
        every { viewModel.selectedCategory } returns MutableStateFlow(null)
        every { viewModel.searchQuery } returns MutableStateFlow("")

        // When
        composeTestRule.setContent {
            CityReportTheme {
                HomeScreen(
                    onNavigateToCreate = {},
                    onNavigateToDetails = {},
                    onNavigateToMap = {},
                    onNavigateToSettings = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Test Report").assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysSyncBadge_forPendingReport() {
        // Given
        val reports = listOf(
            Report(
                id = "1",
                title = "Pending Report",
                description = "Description",
                category = ReportCategory.ROAD,
                latitude = 48.8566,
                longitude = 2.3522,
                syncStatus = SyncStatus.PENDING
            )
        )
        val viewModel = mockk<HomeViewModel>(relaxed = true)
        every { viewModel.reports } returns MutableStateFlow(reports)
        every { viewModel.isLoading } returns MutableStateFlow(false)
        every { viewModel.selectedCategory } returns MutableStateFlow(null)
        every { viewModel.searchQuery } returns MutableStateFlow("")

        // When
        composeTestRule.setContent {
            CityReportTheme {
                HomeScreen(
                    onNavigateToCreate = {},
                    onNavigateToDetails = {},
                    onNavigateToMap = {},
                    onNavigateToSettings = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("En attente").assertIsDisplayed()
    }

    @Test
    fun homeScreen_fabClick_triggersNavigationToCreate() {
        // Given
        val viewModel = mockk<HomeViewModel>(relaxed = true)
        every { viewModel.reports } returns MutableStateFlow(emptyList())
        every { viewModel.isLoading } returns MutableStateFlow(false)
        every { viewModel.selectedCategory } returns MutableStateFlow(null)
        every { viewModel.searchQuery } returns MutableStateFlow("")

        var navigatedToCreate = false

        // When
        composeTestRule.setContent {
            CityReportTheme {
                HomeScreen(
                    onNavigateToCreate = { navigatedToCreate = true },
                    onNavigateToDetails = {},
                    onNavigateToMap = {},
                    onNavigateToSettings = {},
                    viewModel = viewModel
                )
            }
        }

        // Clic sur FAB
        composeTestRule.onNodeWithContentDescription("Créer un rapport").performClick()

        // Then
        assert(navigatedToCreate)
    }

    @Test
    fun homeScreen_displaysSearchBar() {
        // Given
        val viewModel = mockk<HomeViewModel>(relaxed = true)
        every { viewModel.reports } returns MutableStateFlow(emptyList())
        every { viewModel.isLoading } returns MutableStateFlow(false)
        every { viewModel.selectedCategory } returns MutableStateFlow(null)
        every { viewModel.searchQuery } returns MutableStateFlow("")

        // When
        composeTestRule.setContent {
            CityReportTheme {
                HomeScreen(
                    onNavigateToCreate = {},
                    onNavigateToDetails = {},
                    onNavigateToMap = {},
                    onNavigateToSettings = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Rechercher un rapport...").assertIsDisplayed()
    }

    @Test
    fun homeScreen_displaysFilterChips() {
        // Given
        val viewModel = mockk<HomeViewModel>(relaxed = true)
        every { viewModel.reports } returns MutableStateFlow(emptyList())
        every { viewModel.isLoading } returns MutableStateFlow(false)
        every { viewModel.selectedCategory } returns MutableStateFlow(null)
        every { viewModel.searchQuery } returns MutableStateFlow("")

        // When
        composeTestRule.setContent {
            CityReportTheme {
                HomeScreen(
                    onNavigateToCreate = {},
                    onNavigateToDetails = {},
                    onNavigateToMap = {},
                    onNavigateToSettings = {},
                    viewModel = viewModel
                )
            }
        }

        // Then
        composeTestRule.onNodeWithText("Tous").assertIsDisplayed()
    }
}
