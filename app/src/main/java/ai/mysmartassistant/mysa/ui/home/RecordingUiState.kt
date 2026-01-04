package ai.mysmartassistant.mysa.ui.home

sealed interface RecordingUiState {
    object Idle : RecordingUiState
    object Recording : RecordingUiState
    object SlidingToCancel : RecordingUiState
    object Cancelled : RecordingUiState
    object Locked : RecordingUiState
}