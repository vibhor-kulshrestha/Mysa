package ai.mysmartassistant.mysa.ui.common

import ai.mysmartassistant.mysa.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun CenteredTitleAppBar(
    title: String,
    onBack: () -> Unit,
    actions: @Composable RowScope.() -> Unit = {}
) {
    TranslucentAppBar {
        Image(
            painter = painterResource(R.drawable.ic_menu),
            contentDescription = "Menu"
        )

        Spacer(Modifier.weight(1f))

        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(Modifier.weight(1f))

        actions()
    }
}