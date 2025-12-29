package ai.mysmartassistant.mysa.ui.auth.otp

import ai.mysmartassistant.mysa.ui.auth.LoginMode
import ai.mysmartassistant.mysa.ui.auth.PrimaryCtaButton
import ai.mysmartassistant.mysa.viewmodel.auth.otp.OtpViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun OtpContent(
    uiState: OtpUiState,
    viewModel: OtpViewModel,
    onEditNumber: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(horizontal = 24.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            text = "We have sent you an OTP on your mobile",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = uiState.phoneNumber,
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(Modifier.width(8.dp))
            TextButton(onClick = onEditNumber) {
                Text("Edit")
            }
        }

        Spacer(Modifier.height(24.dp))

        OtpTextField(
            value = uiState.otp,
            onValueChange = viewModel::onOtpChanged
        )

        Spacer(Modifier.height(12.dp))

        Text(
            text = if (uiState.canResend)
                "Didn’t receive OTP?"
            else
                "Resend OTP in ${uiState.resendSeconds} sec",
            style = MaterialTheme.typography.bodySmall
        )

        if (uiState.canResend) {
            TextButton(onClick = viewModel::onResendOtp) {
                Text("Resend OTP")
            }
        }

        Spacer(Modifier.height(24.dp))

        PrimaryCtaButton(
            loginMode = LoginMode.SmsOtp,
            enabled = uiState.otp.length == 4,
            onClick = viewModel::onSubmitOtp,
            isLoading = uiState.isSubmitting,
        )
    }
}
