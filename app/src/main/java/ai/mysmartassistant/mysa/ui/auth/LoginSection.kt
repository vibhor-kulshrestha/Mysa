package ai.mysmartassistant.mysa.ui.auth

import ai.mysmartassistant.mysa.data.auth.TruecallerOAuthManager
import ai.mysmartassistant.mysa.domain.auth.LoginMedium
import ai.mysmartassistant.mysa.viewmodel.auth.LoginViewModel
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun LoginSection(
    uiState: LoginUIState,
    viewModel: LoginViewModel,
    truecallerManager: TruecallerOAuthManager,
) {
    val phoneState by viewModel.phoneState.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    when {
        uiState.isLoading -> {
            LoginShimmer()
        }

        uiState.isNotAllowed -> {
            Text(
                text = "Login is not available in your country",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
        }

        else -> {
            AnimatedContent(
                targetState = uiState.mode,
                transitionSpec = {
                    slideInVertically(
                        initialOffsetY = { it }
                    ) + fadeIn() togetherWith
                            slideOutVertically(
                                targetOffsetY = { -it / 2 }
                            ) + fadeOut()
                },
                label = "login_mode_transition"
            ) { mode ->
                when (mode) {
                    LoginMode.Options -> {
                        LoginOptionsContent(
                            loginMediums = uiState.loginMediums,
                            onWhatsAppClick = viewModel::onWhatsAppClicked,
                            onMobileClick = viewModel::onMobileClicked,
                            onTruecallerClick = {
                                truecallerManager.startOAuth { result ->
                                    result.onSuccess { payload ->
                                        viewModel.onTruecallerPayload(payload)
                                    }.onFailure { error ->
                                        viewModel.onTruecallerError(error)
                                    }
                                }
                            }
                        )
                    }

                    LoginMode.WhatsAppOtp,
                    LoginMode.SmsOtp -> {
                        NumberInputSection(
                            loginMode = mode,
                            phoneState = phoneState,
                            uiState = uiState,
                            onPhoneChange = viewModel::onPhoneChanged,
                            onSubmit = viewModel::onPhoneOtpRequested
                        )
                    }
                }

            }
        }
    }
}

@Composable
fun LoginOptionsContent(
    loginMediums: List<LoginMedium>,
    onWhatsAppClick: () -> Unit,
    onMobileClick: () -> Unit,
    onTruecallerClick: () -> Unit
) {
    Column {
        Text(
            text = "Login / Signup with",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(12.dp))

        if (LoginMedium.WHATSAPP in loginMediums) {
            WhatsAppLoginButton(
                onClick = onWhatsAppClick
            )
        }

        Spacer(Modifier.height(12.dp))

        SecondaryLoginRow(
            showTruecaller = LoginMedium.TRUECALLER in loginMediums,
            showMobile = LoginMedium.SMS in loginMediums,
            onMobileClick = onMobileClick,
            onTruecallerClick = onTruecallerClick
        )
    }

}