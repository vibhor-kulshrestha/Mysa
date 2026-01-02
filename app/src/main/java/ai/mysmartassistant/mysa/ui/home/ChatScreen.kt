package ai.mysmartassistant.mysa.ui.home

import ai.mysmartassistant.mysa.ui.common.AppDrawer
import ai.mysmartassistant.mysa.ui.common.ResponsiveNavScaffold
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier

@Composable
fun ChatScreen(
    windowSizeClass: WindowSizeClass,
) {
    var text by rememberSaveable { mutableStateOf("") }

    val inputState = ChatInputUiState(
        text = text,
        isRecording = false
    )
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
    ) { _ ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {

            ChatMessages(
                modifier = Modifier.weight(1f)
            )

            ChatInputBar(
                state = inputState,
                onEvent = { event ->
                    when (event) {
                        ChatInputEvent.AttachClicked -> TODO()
                        ChatInputEvent.EmojiClicked -> TODO()
                        ChatInputEvent.MicLongPressed -> TODO()
                        ChatInputEvent.SendClicked -> TODO()
                        is ChatInputEvent.TextChanged -> text = event.value
                    }
                }
            )
        }
    }
}

@Composable
fun ChatMessages(modifier: Modifier) {
    Box(
        modifier = modifier
    ) {

    }
}