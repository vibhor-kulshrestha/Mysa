package ai.mysmartassistant.mysa.camera.data

import ai.mysmartassistant.mysa.camera.core.CameraXManager
import ai.mysmartassistant.mysa.camera.domain.repository.CameraRepository
import android.net.Uri
import androidx.camera.core.SurfaceRequest
import androidx.lifecycle.LifecycleOwner
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class CameraRepositoryImpl @Inject constructor(
    private val cameraXManager: CameraXManager
) : CameraRepository {
    override val previewStream: Flow<SurfaceRequest?>
        get() = cameraXManager.surfaceRequest

    override fun startCamera(
        lifecycleOwner: LifecycleOwner,
        onError: (Throwable) -> Unit
    ) {
        cameraXManager.startCamera(
            lifeCycleOwner = lifecycleOwner,
            onError = onError
        )
    }

    override fun capturePhoto(
        onSuccess: (Uri) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        cameraXManager.takePhoto(
            onCaptured = onSuccess,
            onError = onError
        )
    }
}