package ai.mysmartassistant.mysa.ui.navigation

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.homeNavGraph(
    navController: NavController,
    windowSizeClass: WindowSizeClass
) {
    navigation(
        route = Route.Home.route,
        startDestination = Route.Chat.route
    ) {

        composable(route = Route.Chat.route) {
//            ChatScreen()
        }
    }
}