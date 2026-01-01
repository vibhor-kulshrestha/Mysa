package ai.mysmartassistant.mysa.ui.common

import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass

fun resolveDrawerMode(
    windowSizeClass: WindowSizeClass
): DrawerMode =
    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> DrawerMode.Hidden
        WindowWidthSizeClass.Medium -> DrawerMode.HomeOnly
        WindowWidthSizeClass.Expanded -> DrawerMode.AlwaysVisible
        else -> DrawerMode.Hidden
    }