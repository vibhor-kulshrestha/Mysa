package ai.mysmartassistant.mysa.ui.navigation

import ai.mysmartassistant.mysa.camera.ui.CameraScreen
import ai.mysmartassistant.mysa.ui.home.ChatScreen
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
            ChatScreen(
                windowSizeClass = windowSizeClass,
                openCamera = {
                    navController.navigate(
                        Route.Camera.route
                    )
                }
            )
        }

        composable(route = Route.Camera.route) {
            CameraScreen()
        }
    }
}