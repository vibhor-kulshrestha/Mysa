package ai.mysmartassistant.mysa.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AppScaffold(
    appBar: @Composable () -> Unit,
    drawerContent: (@Composable () -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {

        // 1️⃣ Full-screen background
        AppBackground()

        // 2️⃣ Optional drawer (Home only)
        if (drawerContent != null) {
            ModalNavigationDrawer(
                drawerContent = drawerContent
            ) {
                ScaffoldContent(appBar, content)
            }
        } else {
            ScaffoldContent(appBar, content)
        }
    }
}
@Composable
private fun ScaffoldContent(
    appBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    Column {
        appBar()
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
        ) {
            content(PaddingValues())
        }
    }
}