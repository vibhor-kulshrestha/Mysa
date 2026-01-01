package ai.mysmartassistant.mysa.ui.auth

import ai.mysmartassistant.mysa.data.auth.TruecallerOAuthManager
import ai.mysmartassistant.mysa.domain.auth.LoginMedium
import ai.mysmartassistant.mysa.ui.auth.LoginUiEvent
import ai.mysmartassistant.mysa.ui.common.SafeArea
import ai.mysmartassistant.mysa.viewmodel.auth.LoginViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginScreen(
    truecallerManager: TruecallerOAuthManager,
    onLoginSuccess: () -> Unit,
    windowSizeClass: WindowSizeClass,
    viewModel: LoginViewModel = hiltViewModel(),
    onNavigateToOtp: (String, LoginMedium) -> Unit,
    onNavigateToOnboarding: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginUiEvent.NavigateToOtp -> {
                    onNavigateToOtp(event.phone, event.medium)
                }
                LoginUiEvent.NavigateToHome -> {
                    onLoginSuccess()
                }

                LoginUiEvent.NavigateToOnboarding -> {
                    onNavigateToOnboarding()
                }
            }
        }
    }
    BackHandler(
        enabled = uiState.mode != LoginMode.Options
    ) {
        viewModel.onBackFromOtp()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        SafeArea {
            when (windowSizeClass.widthSizeClass) {
                WindowWidthSizeClass.Compact ->
                    LoginCompact(uiState, viewModel, truecallerManager)

                WindowWidthSizeClass.Medium ->
                    LoginMedium(uiState, viewModel, truecallerManager)

                WindowWidthSizeClass.Expanded ->
                    LoginExpanded(uiState, viewModel, truecallerManager)
            }
        }
    }

}







