package ai.mysmartassistant.mysa.camera.core

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceRequest
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.net.toUri
import androidx.lifecycle.LifecycleOwner
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File
import java.util.concurrent.ExecutorService
import javax.inject.Inject

class CameraXManager @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val cameraExecutor: ExecutorService
) {
    private val cameraProvider = ProcessCameraProvider.getInstance(context)
    lateinit var imageCapture: ImageCapture
    private val _surfaceRequest = MutableStateFlow<SurfaceRequest?>(null)
    val surfaceRequest: StateFlow<SurfaceRequest?> = _surfaceRequest

    fun startCamera(
        lifeCycleOwner: LifecycleOwner,
        onError: (Throwable) -> Unit
    ) {
        val cameraProvider = cameraProvider.get()
        val preview = Preview.Builder().build().also {previewUseCase ->
            previewUseCase.setSurfaceProvider { request ->
                _surfaceRequest.value = request
            }
        }
        imageCapture = ImageCapture.Builder()
            .build()
        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
        try {
            cameraProvider.unbindAll()
            cameraProvider.bindToLifecycle(
                lifeCycleOwner,
                cameraSelector,
                preview,
                imageCapture
            )
        } catch (e: Exception) {
            Log.e("CameraXManager", "exception occured while starting camera", e)
            onError(e)
        }
    }

    fun takePhoto(
        onCaptured: (Uri) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        if (!::imageCapture.isInitialized) {
            onError(IllegalStateException("ImageCapture not initialized"))
            return
        }
        val photoFile = File(
            context.cacheDir,
            "IMG_${System.currentTimeMillis()}.jpg"
        )
        val outputOptions = ImageCapture.OutputFileOptions
            .Builder(photoFile)
            .build()
        imageCapture.takePicture(
            outputOptions,
            cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    val savedUri = outputFileResults.savedUri ?: photoFile.toUri()
                    onCaptured(savedUri)
                }

                override fun onError(exception: ImageCaptureException) {
                    onError(exception)
                }

            }
        )
    }

    fun shutdown() {
        cameraExecutor.shutdown()
    }
}