package ai.mysmartassistant.mysa.ui.home

import ai.mysmartassistant.mysa.ui.common.AppDrawer
import ai.mysmartassistant.mysa.ui.common.ResponsiveNavScaffold
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier

@Composable
fun ChatScreen(
    windowSizeClass: WindowSizeClass,
) {
    ResponsiveNavScaffold(
        windowSizeClass = windowSizeClass,
        isHomeScreen = true,
        appBar = {
            HomeAppBar {

            }
        },
        drawerContent = {
            AppDrawer()
        }
    ) { _->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {

            ChatMessages(
                modifier = Modifier.weight(1f)
            )

            ChatInputBar()
        }
    }
}


@Composable
fun ChatInputBar() {
    var text = remember { mutableStateOf("") }
    TextField(
        value = text.value,
        onValueChange = { text.value = it },
        label = { Text("Enter your name") },
        placeholder = { Text("John Doe") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()

    )
}

@Composable
fun ChatMessages(modifier: Modifier) {
    Box(
        modifier = modifier
    ) {

    }
}