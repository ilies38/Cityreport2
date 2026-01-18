package com.cityreport.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.cityreport.ui.create.CreateReportScreen
import com.cityreport.ui.details.DetailsScreen
import com.cityreport.ui.edit.EditReportScreen
import com.cityreport.ui.home.HomeScreen
import com.cityreport.ui.map.MapScreen
import com.cityreport.ui.settings.SettingsScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object CreateReport : Screen("create_report")
    object ReportDetails : Screen("report_details/{reportId}") {
        fun createRoute(reportId: String) = "report_details/$reportId"
    }
    object EditReport : Screen("edit_report/{reportId}") {
        fun createRoute(reportId: String) = "edit_report/$reportId"
    }
    object Map : Screen("map")
    object Settings : Screen("settings")
}

// Duree des animations en ms
private const val ANIM_DURATION = 200

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Home.route
) {
    // Memoriser les callbacks de navigation pour eviter les recompositions
    val navigateToCreate = remember(navController) {
        { navController.navigate(Screen.CreateReport.route) }
    }
    val navigateToMap = remember(navController) {
        { navController.navigate(Screen.Map.route) }
    }
    val navigateToSettings = remember(navController) {
        { navController.navigate(Screen.Settings.route) }
    }
    val navigateToDetails: (String) -> Unit = remember(navController) {
        { reportId: String -> navController.navigate(Screen.ReportDetails.createRoute(reportId)) }
    }
    val navigateToEdit: (String) -> Unit = remember(navController) {
        { reportId: String -> navController.navigate(Screen.EditReport.createRoute(reportId)) }
    }
    val navigateBack = remember(navController) {
        { navController.popBackStack() }
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            fadeIn(animationSpec = tween(ANIM_DURATION))
        },
        exitTransition = {
            fadeOut(animationSpec = tween(ANIM_DURATION))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(ANIM_DURATION))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(ANIM_DURATION))
        }
    ) {
        // Home Screen
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToCreate = navigateToCreate,
                onNavigateToDetails = navigateToDetails,
                onNavigateToMap = navigateToMap,
                onNavigateToSettings = navigateToSettings
            )
        }

        // Create Report Screen
        composable(Screen.CreateReport.route) {
            CreateReportScreen(
                onNavigateBack = { navigateBack() }
            )
        }

        // Report Details Screen
        composable(
            route = Screen.ReportDetails.route,
            arguments = listOf(
                navArgument("reportId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
            DetailsScreen(
                reportId = reportId,
                onNavigateBack = { navigateBack() },
                onNavigateToEdit = navigateToEdit
            )
        }

        // Edit Report Screen
        composable(
            route = Screen.EditReport.route,
            arguments = listOf(
                navArgument("reportId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val reportId = backStackEntry.arguments?.getString("reportId") ?: ""
            EditReportScreen(
                reportId = reportId,
                onNavigateBack = { navigateBack() }
            )
        }

        // Map Screen
        composable(Screen.Map.route) {
            MapScreen(
                onNavigateBack = { navigateBack() },
                onNavigateToDetails = navigateToDetails
            )
        }

        // Settings Screen
        composable(Screen.Settings.route) {
            SettingsScreen(
                onNavigateBack = { navigateBack() }
            )
        }
    }
}
