package com.smithjilks.mpesaexpensetracker.navigation

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.smithjilks.mpesaexpensetracker.core.constants.MpesaExpenseTrackerScreens
import com.smithjilks.mpesaexpensetracker.feature.dashboard.Dashboard
import com.smithjilks.mpesaexpensetracker.feature.records.RecordDetailsScreen
import com.smithjilks.mpesaexpensetracker.feature.records.RecordsScreen
import com.smithjilks.mpesaexpensetracker.feature.splashscreen.SplashScreen

@Composable
fun MpesaExpenseTrackerNavigation() {
    val navController = rememberNavController()
    val uri = "app://open.smithjilks.mpesaexpensetracker"

    NavHost(
        navController = navController,
        startDestination = MpesaExpenseTrackerScreens.SplashScreen.name
    ) {

        composable(MpesaExpenseTrackerScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }

        composable(MpesaExpenseTrackerScreens.DashboardScreen.name) {
            Dashboard(navController = navController)
        }

        composable(MpesaExpenseTrackerScreens.StatisticsScreen.name) {

        }

        composable(MpesaExpenseTrackerScreens.RecordsScreen.name) {
            RecordsScreen(navController = navController)

        }


        val recordDetailsRoute = MpesaExpenseTrackerScreens.RecordDetailsScreen.name
        composable(
            "$recordDetailsRoute?recordId={recordId}",
            arguments = listOf(navArgument(name = "recordId") {
                type = NavType.StringType
                defaultValue = ""
            }),
            deepLinks = listOf(navDeepLink {
                action = Intent.ACTION_VIEW
                uriPattern = "$uri/$recordDetailsRoute?recordId={recordId}" })

        ) { backStackEntry ->
            RecordDetailsScreen(
                navController = navController,
                recordId = backStackEntry.arguments?.getString("recordId")
            )

        }


    }


}
