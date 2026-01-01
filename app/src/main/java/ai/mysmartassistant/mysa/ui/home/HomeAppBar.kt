package ai.mysmartassistant.mysa.ui.home

import ai.mysmartassistant.mysa.R
import ai.mysmartassistant.mysa.ui.common.TranslucentAppBar
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun HomeAppBar(
    onMenuClick: () -> Unit
) {
    TranslucentAppBar {
        Image(
            modifier = Modifier.clickable(
                enabled = true,
                onClick = onMenuClick,
            ),
            painter = painterResource(R.drawable.ic_menu),
            contentDescription = "Menu"
        )

        Spacer(Modifier.width(8.dp))

        Column {
            Text(
                text = "MySa",
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
            Text(
                text = "MySmartAssistant",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    }
}