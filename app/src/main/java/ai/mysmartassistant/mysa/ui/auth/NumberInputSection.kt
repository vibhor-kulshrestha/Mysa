package ai.mysmartassistant.mysa.ui.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NumberInputSection(
    loginMode: LoginMode,
    phoneState: PhoneInputState,
    uiState: LoginUIState,
    onPhoneChange: (String) -> Unit,
    onSubmit: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth().align(Alignment.Start),
            text = "Login / Signup with",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.height(12.dp))

        MobileNumberTextField(
            state = phoneState,
            onValueChange = onPhoneChange
        )


        Spacer(Modifier.height(24.dp))

        PrimaryCtaButton(
            loginMode = loginMode,
            isLoading = uiState.isSendingOtp,
            enabled = phoneState.isValid,
            onClick = onSubmit
        )
    }
}