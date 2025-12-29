package ai.mysmartassistant.mysa.ui.auth

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryCtaButton(
    loginMode: LoginMode,
    isLoading: Boolean,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    val text = when (loginMode) {
        LoginMode.WhatsAppOtp -> "Get OTP on WhatsApp"
        LoginMode.SmsOtp -> "Get OTP"
        else -> ""
    }

    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp)
            .height(56.dp),
        shape = RoundedCornerShape(28.dp)
    ) {
        when(isLoading) {
            true -> CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 3.dp
            )
            false -> Text(
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}