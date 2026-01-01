package ai.mysmartassistant.mysa.ui.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ResponsiveNavScaffold(
    windowSizeClass: WindowSizeClass,
    isHomeScreen: Boolean,
    drawerContent: @Composable () -> Unit,
    appBar: @Composable () -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val drawerMode = remember(windowSizeClass) {
        resolveDrawerMode(windowSizeClass)
    }

    val showPersistentDrawer =
        drawerMode == DrawerMode.AlwaysVisible ||
                (drawerMode == DrawerMode.HomeOnly && isHomeScreen)

    Row(modifier = Modifier.fillMaxSize()) {

        if (showPersistentDrawer) {
            Surface(
                modifier = Modifier.width(280.dp),
                tonalElevation = 2.dp
            ) {
                drawerContent()
            }

            VerticalDivider()
        }

        Box(modifier = Modifier.weight(1f)) {
            AppScaffold(
                appBar = appBar,
                drawerContent = if (
                    drawerMode == DrawerMode.Hidden && isHomeScreen
                ) drawerContent else null,
                content = {paddingValues ->  content(paddingValues) }
            )
        }
    }
}