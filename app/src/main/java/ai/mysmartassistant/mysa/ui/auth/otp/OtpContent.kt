package ai.mysmartassistant.mysa.ui.auth.otp

import ai.mysmartassistant.mysa.R
import ai.mysmartassistant.mysa.ui.auth.LoginMode
import ai.mysmartassistant.mysa.ui.auth.PrimaryCtaButton
import ai.mysmartassistant.mysa.viewmodel.auth.otp.OtpViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painterResource(R.drawable.ic_mobile),
            contentDescription = null,
            modifier = Modifier.size(48.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = "Verification Code",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(Modifier.height(8.dp))

        Text(
            text = "We have sent the verification code to",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = uiState.phoneNumber,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.width(8.dp))
            TextButton(onClick = onEditNumber) {
                Text("Edit")
            }
        }

        Spacer(Modifier.height(32.dp))

        OtpTextField(
            value = uiState.otp,
            onValueChange = viewModel::onOtpChanged
        )

        Spacer(Modifier.height(24.dp))

        Text(
            text = if (uiState.canResend)
                "Didn’t receive OTP?"
            else
                "Resend OTP in ${uiState.resendSeconds} sec",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )

        if (uiState.canResend) {
            TextButton(onClick = viewModel::onResendOtp) {
                Text("Resend OTP")
            }
        }

        Spacer(Modifier.height(32.dp))

        PrimaryCtaButton(
            loginMode = LoginMode.SmsOtp,
            enabled = uiState.otp.length == 4,
            onClick = viewModel::onSubmitOtp,
            isLoading = uiState.isSubmitting,
        )
    }
}
