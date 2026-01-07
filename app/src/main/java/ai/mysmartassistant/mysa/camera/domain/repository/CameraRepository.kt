package ai.mysmartassistant.mysa.camera.domain.repository

import android.net.Uri
import androidx.camera.core.SurfaceRequest
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.Flow

interface CameraRepository {

    val previewStream: Flow<SurfaceRequest?>

    fun startCamera(
        lifecycleOwner: LifecycleOwner,
        onError: (Throwable) -> Unit
    )

    fun capturePhoto(
        onSuccess: (Uri) -> Unit,
        onError: (Throwable) -> Unit
    )

    fun switchCamera(
        lifecycleOwner: LifecycleOwner,
        onError: (Throwable) -> Unit
    )
}