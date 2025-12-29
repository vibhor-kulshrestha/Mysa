package ai.mysmartassistant.mysa.ui.auth

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MobileNumberTextField(
    state: PhoneInputState,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = state.phoneNumber,
        onValueChange = { newValue ->
            if (newValue.all { it.isDigit() }) {
                onValueChange(newValue)
            }
        },
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp)
            .height(56.dp),
        singleLine = true,
        leadingIcon = {
            CountryCodePrefix(
                countryIso = state.countryIso,
                countryCode = state.countryCode
            )
        },
        placeholder = {
            Text(text = "Enter Mobile Number")
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done
        ),
        shape = RoundedCornerShape(28.dp)
    )
}

@Composable
private fun CountryCodePrefix(
    countryIso: String,
    countryCode: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 12.dp, end = 8.dp)
    ) {
        Text(
            text = countryIso.toFlagEmoji(),
            fontSize = 18.sp
        )
        Spacer(Modifier.width(6.dp))
        Text(
            text = countryCode,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.width(8.dp))
        VerticalDivider(
            modifier = Modifier
                .height(24.dp)
                .width(1.dp)
        )
    }
}

fun String.toFlagEmoji(): String {
    if (length != 2) return "🏳️"
    val first = this[0].uppercaseChar() - 'A' + 0x1F1E6
    val second = this[1].uppercaseChar() - 'A' + 0x1F1E6
    return String(Character.toChars(first)) +
            String(Character.toChars(second))
}