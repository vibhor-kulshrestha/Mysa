package ai.mysmartassistant.mysa.camera.viewmodel

import ai.mysmartassistant.mysa.camera.domain.usecase.CapturePhotoUseCase
import ai.mysmartassistant.mysa.camera.domain.usecase.ObserveCameraPreviewUseCase
import ai.mysmartassistant.mysa.camera.domain.usecase.StartCameraPreviewUseCase
import ai.mysmartassistant.mysa.camera.ui.CameraUiState
import androidx.camera.core.SurfaceRequest
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val startCameraPreview: StartCameraPreviewUseCase,
    private val capturePhoto: CapturePhotoUseCase,
    private val observePreview: ObserveCameraPreviewUseCase
) : ViewModel() {

    val surfaceRequest: StateFlow<SurfaceRequest?> =
        observePreview()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    private val _uiState = MutableStateFlow(CameraUiState())
    val uiState = _uiState.asStateFlow()

    fun startCamera(lifecycleOwner: LifecycleOwner) {
        startCameraPreview(
            lifecycleOwner
        ) { t ->
            _uiState.update {
                it.copy(lastPhotoUri = null, error = t)
            }
        }
    }

    fun takePhoto() {
        capturePhoto(
            onSuccess = { uri ->
                _uiState.update {
                    it.copy(lastPhotoUri = uri, error = null)
                }
            },
            onError = { t ->
                _uiState.update {
                    it.copy(lastPhotoUri = null, error = t)
                }
            }
        )
    }
}