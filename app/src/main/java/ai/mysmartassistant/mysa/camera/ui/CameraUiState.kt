package ai.mysmartassistant.mysa.camera.ui

import android.net.Uri

data class CameraUiState(
    val lastPhotoUri: Uri? = null,
    val error: Throwable? = null
)