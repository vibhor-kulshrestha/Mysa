package ai.mysmartassistant.mysa.ui.auth.otp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.autofill.ContentType
import androidx.compose.ui.semantics.contentType
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun OtpTextField(
    value: String,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.length <= 6 && it.all(Char::isDigit)) {
                onValueChange(it)
            }
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp) // Slightly taller for larger text
            .semantics {
                contentType = ContentType.SmsOtpCode
            },
        placeholder = {
            Text(
                text = "0000",
                modifier = Modifier.fillMaxWidth(),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                style = androidx.compose.ui.text.TextStyle(
                    fontSize = 24.sp,
                    letterSpacing = 8.sp
                )
            )
        }, // Optional: Placeholder like 0000 or - - - -
        textStyle = androidx.compose.ui.text.TextStyle(
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            fontSize = 24.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
            letterSpacing = 8.sp,
            color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.NumberPassword,
            imeAction = ImeAction.Done
        ),
        singleLine = true,
        shape = androidx.compose.foundation.shape.RoundedCornerShape(12.dp) // More rounded
    )
}