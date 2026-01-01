package ai.mysmartassistant.mysa.ui.auth.otp

import ai.mysmartassistant.mysa.domain.auth.LoginMedium
import ai.mysmartassistant.mysa.ui.auth.LoginUiEvent
import ai.mysmartassistant.mysa.platform.sms.SmsRetrieverListener
import ai.mysmartassistant.mysa.platform.sms.SmsRetrieverStarter
import ai.mysmartassistant.mysa.ui.common.SafeArea
import ai.mysmartassistant.mysa.viewmodel.auth.otp.OtpViewModel
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun OtpScreen(
    windowSizeClass: WindowSizeClass,
    phoneNumber: String,
    medium: LoginMedium,
    onEditNumber: () -> Unit,
    onBack: () -> Unit,
    onLoginSuccess: () -> Unit,
    onNavigateToOnboarding: () -> Unit,
    viewModel: OtpViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.init(phoneNumber,medium)
        SmsRetrieverStarter.start(context)
        viewModel.events.collect { event ->
            when (event) {
                OtpUIEvents.NavigateToHome -> {
                    onLoginSuccess()
                }

                OtpUIEvents.NavigateToOnboarding -> {
                    onNavigateToOnboarding()
                }
            }
        }
    }

    BackHandler { onBack() }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        SmsRetrieverListener { otp ->
            viewModel.onOtpAutoFilled(otp)
        }
        SafeArea {
            when (windowSizeClass.widthSizeClass) {

                WindowWidthSizeClass.Compact ->
                    OtpContent(uiState, viewModel, onEditNumber)

                WindowWidthSizeClass.Medium,
                WindowWidthSizeClass.Expanded ->
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        OtpContent(
                            uiState,
                            viewModel,
                            onEditNumber,
                            Modifier.widthIn(max = 420.dp)
                        )
                    }
            }
        }
    }
}