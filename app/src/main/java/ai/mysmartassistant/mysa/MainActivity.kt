package ai.mysmartassistant.mysa

import ai.mysmartassistant.mysa.data.auth.TruecallerOAuthManager
import ai.mysmartassistant.mysa.ui.auth.TruecallerUiConfig
import ai.mysmartassistant.mysa.ui.navigation.AppNavHost
import ai.mysmartassistant.mysa.ui.theme.MysaTheme
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.fragment.app.FragmentActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private lateinit var truecallerManager: TruecallerOAuthManager
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        truecallerManager = TruecallerOAuthManager(this)

        enableEdgeToEdge()
        setContent {
            MysaTheme {
                val windowSizeClass = calculateWindowSizeClass(this)
                val colorScheme = MaterialTheme.colorScheme
                val uiConfig = remember(colorScheme) {
                    TruecallerUiConfig(
                        buttonColor = colorScheme.primary.toArgb(),
                        buttonTextColor = colorScheme.onPrimary.toArgb()
                    )
                }

                LaunchedEffect(Unit) {
                    truecallerManager.init(uiConfig)
                }
                AppNavHost(
                    windowSizeClass = windowSizeClass,
                    truecallerManager = truecallerManager
                )
            }
        }
    }
}

