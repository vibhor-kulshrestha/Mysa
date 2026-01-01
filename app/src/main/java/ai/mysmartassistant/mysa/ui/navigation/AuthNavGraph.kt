package ai.mysmartassistant.mysa.ui.navigation

import ai.mysmartassistant.mysa.data.auth.TruecallerOAuthManager
import ai.mysmartassistant.mysa.domain.auth.LoginMedium
import ai.mysmartassistant.mysa.ui.auth.LoginScreen
import ai.mysmartassistant.mysa.ui.auth.otp.OtpScreen
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    truecallerManager: TruecallerOAuthManager
) {
    navigation(
        route = Route.Auth.route,
        startDestination = Route.Login.route
    ) {
        composable(Route.Login.route) {
            LoginScreen(
                truecallerManager = truecallerManager,
                onLoginSuccess = {
                    navController.navigate(Route.Home.route) {
                        popUpTo(Route.Auth.route) { inclusive = true }
                    }
                },
                windowSizeClass = windowSizeClass,
                onNavigateToOtp = { phone ,medium->
                    navController.navigate(
                        Route.Otp.createRoute(phone,medium)
                    )
                },
                onNavigateToOnboarding = {
                    navController.navigate(Route.OnBoard.route) {
                        popUpTo(Route.Auth.route) { inclusive = true }
                    }
                }
            )
        }
        composable(Route.Otp.route) { backStack ->
            OtpScreen(
                phoneNumber = backStack.arguments?.getString("phone")!!,
                medium = LoginMedium.valueOf(backStack.arguments?.getString("medium")!!),
                windowSizeClass = windowSizeClass,
                onEditNumber = {
                    navController.popBackStack() // back to login
                },
                onBack = {
                    navController.popBackStack()
                },
                onLoginSuccess = {
                    navController.navigate(Route.Home.route) {
                        popUpTo(Route.Auth.route) { inclusive = true }
                    }
                },
                onNavigateToOnboarding = {
                    navController.navigate(Route.OnBoard.route) {
                        popUpTo(Route.Auth.route) { inclusive = true }
                    }
                }
            )
        }
    }
}