package ai.mysmartassistant.mysa.ui.home

data class ChatInputUiState(
    val text: String = "",
    val isRecording: Boolean = false,
    val dragX: Float = 0f,
) {
    val showSend: Boolean get() = text.isNotBlank()
}