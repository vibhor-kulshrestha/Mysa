package ai.mysmartassistant.mysa.ui.navigation

import ai.mysmartassistant.mysa.data.auth.TruecallerOAuthManager
import ai.mysmartassistant.mysa.viewmodel.start.AppStartViewModel
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavHost(
    windowSizeClass: WindowSizeClass,
    truecallerManager: TruecallerOAuthManager,
    viewModel: AppStartViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val startRoute by viewModel.startDestination.collectAsStateWithLifecycle()
    if (startRoute == null) {
        AppLaunchPlaceholder()
        return
    }
    NavHost(
        navController = navController,
        startDestination = startRoute!!
    ) {
        authNavGraph(
            navController,
            windowSizeClass = windowSizeClass,
            truecallerManager = truecallerManager
        )
        homeNavGraph(
            navController,
            windowSizeClass = windowSizeClass
        )
        onboardNavGraph(
            navController,
            windowSizeClass = windowSizeClass
        )
    }
}