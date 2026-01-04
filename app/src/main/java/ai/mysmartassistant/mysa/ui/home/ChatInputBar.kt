package ai.mysmartassistant.mysa.ui.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.layout.Box
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun ChatInputBar(
    state: ChatInputUiState,
    onEvent: (ChatInputEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val offsetAnim = remember { Animatable(0f) }
    var maxDrag by remember { mutableFloatStateOf(0f) }
    val scaleAnim = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()
    val density = LocalDensity.current
    val clipBuffer = with(density) { 10.dp.toPx() }
    val barPad = with(density) { 10.dp.toPx() }
    var buttonX by remember { mutableFloatStateOf(0f) }
    var barBottomY by remember { mutableFloatStateOf(0f) }

    var isCancelling by remember { mutableStateOf(false) }
    var emojiPosition by remember { mutableStateOf(IntOffset.Zero) }

    var currentDragX by remember { mutableFloatStateOf(0f) }

    var barRootPosition by remember { mutableStateOf(IntOffset.Zero) }

    LaunchedEffect(buttonX, barBottomY) {
        if (buttonX > 0 && barBottomY > 0) {
            onEvent(
                ChatInputEvent.AttachmentPosition(
                    IntOffset(
                        buttonX.toInt(),
                        barBottomY.toInt()
                    )
                )
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                barRootPosition =
                    it.positionInRoot().run { IntOffset(x.roundToInt(), y.roundToInt()) }
            },
        contentAlignment = Alignment.BottomCenter
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp)
                .padding(bottom = 5.dp)
                .onGloballyPositioned {
                    barBottomY = it.size.height + barPad
                },
            verticalAlignment = Alignment.Bottom
        ) {
            Row(
                modifier = Modifier
                    .weight(1f)
                    .onSizeChanged { size ->
                        maxDrag = size.width.toFloat() * 0.2f
                    }
                    .drawWithContent {
                        clipRect(right = size.width + offsetAnim.value + clipBuffer) {
                            this@drawWithContent.drawContent()
                        }
                    }
                    .background(
                        MaterialTheme.colorScheme.secondaryContainer,
                        MaterialTheme.shapes.extraLarge
                    )
                    .animateContentSize(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Switch between Input and Recording Content
                if ((state.isRecording || currentDragX < -5f) && !isCancelling) {
                    RecordingContent(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .defaultMinSize(minHeight = 48.dp),
                        dragOffset = currentDragX
                    )
                } else {
                    InputContent(
                        onEvent = onEvent,
                        state = state,
                        buttonX = { button ->
                            buttonX = button
                        },
                        onEmojiPositioned = { pos ->
                            emojiPosition = pos
                        },
                        isCancelling = isCancelling
                    )
                }
            }
            Spacer(modifier.width(10.dp))

            // Mic Button Container
            AnimatedMicSendButton(
                scope = scope,
                offsetAnim = offsetAnim,
                scaleAnim = scaleAnim,
                showSend = state.showSend,
                maxDrag = maxDrag,
                onSend = { onEvent(ChatInputEvent.SendClicked) },
                onMicPressStart = { onEvent(ChatInputEvent.RecordingStart) },
                onMicPressEnd = { onEvent(ChatInputEvent.RecordingEnd) },
                onMicDragCanceled = {
                    isCancelling = true
                },
                onDrag = { drag ->
                    currentDragX = drag
                }
            )
        }

        if (isCancelling) {
            val relativeBinTarget = emojiPosition - barRootPosition

            RecordingCancellationOverlay(
                modifier = Modifier.matchParentSize(),
                micStartOffset = relativeBinTarget, // Start at Emoji
                binTargetOffset = relativeBinTarget, // End at Emoji
                onAnimationFinished = {
                    isCancelling = false
                    currentDragX = 0f
                    onEvent(ChatInputEvent.RecordingCanceled)
                }
            )
        }
    }
}

@Composable
fun RowScope.InputContent(
    onEvent: (ChatInputEvent) -> Unit,
    state: ChatInputUiState,
    buttonX: (Float) -> Unit,
    onEmojiPositioned: (IntOffset) -> Unit,
    isCancelling: Boolean
) {
    IconButton(
        modifier = Modifier
            .align(Alignment.Bottom)
            .onGloballyPositioned {
                onEmojiPositioned(
                    it.positionInRoot().run { IntOffset(x.roundToInt(), y.roundToInt()) })
            }
            .alpha(if (isCancelling) 0f else 1f),
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
        modifier = Modifier
            .align(Alignment.Bottom)
            .onGloballyPositioned {
                // Capture X only
                buttonX(it.positionInRoot().x + (it.size.width / 2))
            },
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

@Composable
private fun AnimatedMicSendButton(
    showSend: Boolean,
    onSend: () -> Unit,
    onMicPressStart: () -> Unit,
    onMicPressEnd: () -> Unit,
    onMicDragCanceled: () -> Unit,
    onDrag: (Float) -> Unit,
    scope: CoroutineScope,
    offsetAnim: Animatable<Float, AnimationVector1D>,
    scaleAnim: Animatable<Float, AnimationVector1D>,
    maxDrag: Float
) {
    Box(
        modifier = Modifier
            .offset { IntOffset(offsetAnim.value.roundToInt(), 0) }
            .scale(scaleAnim.value)
            .pointerInput(showSend) {
                awaitEachGesture {
                    val down = awaitFirstDown(requireUnconsumed = false)
                    if (showSend) {
                        val up = waitForUpOrCancellation()
                        if (up != null) onSend()
                    } else {
                        val dragPointer = down.id
                        scope.launch {
                            scaleAnim.animateTo(
                                2f,
                                tween(90, easing = FastOutLinearInEasing)
                            )
                        }
                        onMicPressStart()
                        onDrag(0f)

                        var currentOffset = 0f
                        var isCancelled = false
                        while (true) {
                            val event = awaitPointerEvent()
                            val change = event.changes.firstOrNull { it.id == dragPointer }
                            if (change == null || !change.pressed) break

                            val dragAmount = change.positionChange().x
                            if (dragAmount != 0f) {
                                currentOffset += dragAmount
                                currentOffset = currentOffset.coerceIn(-maxDrag, 0f)

                                runCatching {
                                    scope.launch { offsetAnim.snapTo(currentOffset) }
                                }
                                onDrag(currentOffset) // Emit updated drag
                                change.consume()

                                if (currentOffset <= -maxDrag) {
                                    isCancelled = true
                                    break
                                }
                            }
                        }

                        // Remaining logic handles animations
                        scope.launch {
                            scaleAnim.animateTo(
                                1f,
                                tween(120, easing = LinearOutSlowInEasing)
                            )
                        }

                        scope.launch {
                            offsetAnim.animateTo(
                                0f,
                                animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
                            )
                        }
                        onDrag(0f) // Reset drag

                        if (isCancelled) {
                            onMicDragCanceled()
                        } else {
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