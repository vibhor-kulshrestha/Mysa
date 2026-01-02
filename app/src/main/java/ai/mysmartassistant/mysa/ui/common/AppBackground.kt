package ai.mysmartassistant.mysa.ui.common

import ai.mysmartassistant.mysa.R
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

@Composable
fun AppBackground() {
    val isDark = isSystemInDarkTheme()

    val patternColor = if (isDark) {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.04f)
    } else {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.08f)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Icon pattern image overlay
        Image(
            painter = painterResource(id = R.drawable.chat_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
            colorFilter = ColorFilter.tint(
                color = patternColor,
                blendMode = BlendMode.SrcIn
            )
        )
    }
}