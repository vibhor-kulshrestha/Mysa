package ai.mysmartassistant.mysa.ui.home

data class ChatInputUiState(
    val text: String = "",
    val isRecording: Boolean = false
) {
    val showSend: Boolean get() = text.isNotBlank()
}