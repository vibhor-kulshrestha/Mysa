package ai.mysmartassistant.mysa.ui.onboard

import ai.mysmartassistant.mysa.ui.Gender
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

@Composable
fun GenderSelector(
    selected: Gender?,
    onSelect: (Gender) -> Unit
) {
    SingleChoiceSegmentedButtonRow {
        Gender.entries.forEach { gender ->
            SegmentedButton(
                selected = selected == gender,
                onClick = { onSelect(gender) },
                label = { Text(gender.name.lowercase().replaceFirstChar { it.uppercase() }) },
                shape = RoundedCornerShape(5.dp)
            )
        }
    }
}