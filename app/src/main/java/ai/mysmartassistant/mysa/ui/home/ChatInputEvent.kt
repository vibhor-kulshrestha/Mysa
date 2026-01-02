package ai.mysmartassistant.mysa.ui.home

sealed interface ChatInputEvent {
    data class TextChanged(val value: String) : ChatInputEvent
    data object SendClicked : ChatInputEvent
    data object AttachClicked : ChatInputEvent
    data object EmojiClicked : ChatInputEvent
    data object MicLongPressed : ChatInputEvent
}