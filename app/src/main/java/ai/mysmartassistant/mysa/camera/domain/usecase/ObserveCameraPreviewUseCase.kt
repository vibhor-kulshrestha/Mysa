package ai.mysmartassistant.mysa.camera.domain.usecase

import ai.mysmartassistant.mysa.camera.domain.repository.CameraRepository
import androidx.camera.core.SurfaceRequest
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ObserveCameraPreviewUseCase @Inject constructor(
    private val repository: CameraRepository
) {
    operator fun invoke(): Flow<SurfaceRequest?> =
        repository.previewStream
}