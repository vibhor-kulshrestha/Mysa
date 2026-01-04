package ai.mysmartassistant.mysa.ui.home

import androidx.compose.ui.unit.IntOffset

sealed interface ChatInputEvent {
    data class TextChanged(val value: String) : ChatInputEvent
    data class AttachmentPosition(val value: IntOffset) : ChatInputEvent
    data object SendClicked : ChatInputEvent
    data object AttachClicked : ChatInputEvent
    data object EmojiClicked : ChatInputEvent
    data object RecordingStart : ChatInputEvent
    data object RecordingEnd : ChatInputEvent
    data object RecordingCanceled : ChatInputEvent
    data class DragX(val value: Float) : ChatInputEvent
}