package ai.mysmartassistant.mysa.ui.navigation

import ai.mysmartassistant.mysa.ui.onboard.OnBoardScreen
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.onboardNavGraph(
    navController: NavController,
    windowSizeClass: WindowSizeClass
) {
    navigation(
        route = Route.OnBoard.route,
        startDestination = Route.ProfileDetails.route
    ) {
        composable(Route.ProfileDetails.route) {
            OnBoardScreen(
                windowSizeClass = windowSizeClass,
                onOnBoardSuccess = {
                    navController.navigate(Route.Home.route) {
                        popUpTo(Route.Auth.route) { inclusive = true }
                    }
                }
            )
        }
    }
}