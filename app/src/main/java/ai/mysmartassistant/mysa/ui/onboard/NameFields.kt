package ai.mysmartassistant.mysa.ui.onboard

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@Composable
fun NameFields(
    uiState: OnboardingUIState,
    onAction: (OnboardingAction) -> Unit
) {
    OutlinedTextField(
        value = uiState.firstName,
        onValueChange = {
            onAction(OnboardingAction.FirstNameChanged(it))
        },
        label = { Text("First name") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next
        )
    )

    Spacer(Modifier.height(12.dp))

    OutlinedTextField(
        value = uiState.lastName,
        onValueChange = {
            onAction(OnboardingAction.LastNameChanged(it))
        },
        label = { Text("Last name") },
        singleLine = true,
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        )
    )
}