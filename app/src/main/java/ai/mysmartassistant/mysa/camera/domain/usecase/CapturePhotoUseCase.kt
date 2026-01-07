package ai.mysmartassistant.mysa.camera.domain.usecase

import ai.mysmartassistant.mysa.camera.domain.repository.CameraRepository
import android.net.Uri
import javax.inject.Inject

class CapturePhotoUseCase @Inject constructor(
    private val cameraRepository: CameraRepository
) {
    operator fun invoke(
        onSuccess: (Uri) -> Unit,
        onError: (Throwable) -> Unit
    ) = cameraRepository.capturePhoto(onSuccess, onError)
}