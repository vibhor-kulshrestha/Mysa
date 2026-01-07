package ai.mysmartassistant.mysa.camera.domain.usecase

import ai.mysmartassistant.mysa.camera.domain.repository.CameraRepository
import androidx.lifecycle.LifecycleOwner
import javax.inject.Inject

class StartCameraPreviewUseCase @Inject constructor(
    private val cameraRepository: CameraRepository
) {
    operator fun invoke(
        lifecycleOwner: LifecycleOwner,
        onError: (Throwable) -> Unit
    ) = cameraRepository.startCamera(lifecycleOwner, onError)
}