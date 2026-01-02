package ai.mysmartassistant.mysa.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.EmojiEmotions
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex


@Composable
fun ChatInputBar(
    state: ChatInputUiState,
    onEvent: (ChatInputEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 6.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom
    ) {

        /* Emoji */
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.secondaryContainer, RoundedCornerShape(25.dp))
                .weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                modifier = Modifier.align(Alignment.Bottom),
                onClick = { onEvent(ChatInputEvent.EmojiClicked) }
            ) {
                Icon(
                    Icons.Outlined.EmojiEmotions,
                    contentDescription = "Emoji"
                )
            }
            /* Text Input */
            BasicTextField(
                value = state.text,
                onValueChange = { onEvent(ChatInputEvent.TextChanged(it)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp),
                maxLines = 6,
                textStyle = MaterialTheme.typography.bodyLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface
                ),
                decorationBox = { inner ->
                    if (state.text.isEmpty()) {
                        Text(
                            "Message",
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                    inner()
                }
            )
            /* Attachment */
            IconButton(
                modifier = Modifier.align(Alignment.Bottom),
                onClick = { onEvent(ChatInputEvent.AttachClicked) }
            ) {
                Icon(
                    Icons.Outlined.AttachFile,
                    contentDescription = "Attach"
                )
            }
            IconButton(
                modifier = Modifier.align(Alignment.Bottom),
                onClick = { onEvent(ChatInputEvent.AttachClicked) }
            ) {
                Icon(
                    Icons.Outlined.PhotoCamera,
                    contentDescription = "Attach"
                )
            }
        }
        Spacer(modifier.width(10.dp))
        AnimatedMicSendButton(
            showSend = state.showSend,
            onSend = { onEvent(ChatInputEvent.SendClicked) },
            onMicPressStart = { },
            onMicPressEnd = { }
        )
    }
}

@Composable
private fun AnimatedMicSendButton(
    showSend: Boolean,
    onSend: () -> Unit,
    onMicPressStart: () -> Unit,
    onMicPressEnd: () -> Unit
) {
    var isZoom by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isZoom) 2f else 1f,
        animationSpec = tween(100),
        label = "mic_zoom"
    )

    Box(
        modifier = Modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
                transformOrigin = TransformOrigin.Center
            }
            .pointerInput(showSend) {
                awaitPointerEventScope {
                    while (true) {
                        awaitFirstDown()
                        if (showSend) {
                            waitForUpOrCancellation()
                            onSend()
                        } else {
                            isZoom = true
                            onMicPressStart()

                            waitForUpOrCancellation()

                            isZoom = false
                            onMicPressEnd()
                        }
                    }
                }
            }
            .zIndex(Float.MAX_VALUE),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(
                    MaterialTheme.colorScheme.primaryContainer,
                    CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = showSend,
                transitionSpec = {
                    fadeIn() + scaleIn() togetherWith
                            fadeOut() + scaleOut()
                },
                label = "mic_send_transition"
            ) { isSend ->
                Icon(
                    imageVector = if (isSend)
                        Icons.AutoMirrored.Filled.Send
                    else
                        Icons.Filled.Mic,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}