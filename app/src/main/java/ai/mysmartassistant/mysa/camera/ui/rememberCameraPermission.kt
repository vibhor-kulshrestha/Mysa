package ai.mysmartassistant.mysa.camera.ui

import android.Manifest
import android.app.Activity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat

@Composable
fun rememberCameraPermission(
    onPermissionResult: (CameraPermissionState) -> Unit
): () -> Unit {
    val context = LocalContext.current
    val activity = context as Activity
    val launcher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (granted) {
                onPermissionResult(CameraPermissionState.Granted)
            } else {
                val permanentlyDenied =
                    !ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.CAMERA
                    )
                onPermissionResult(
                    if (permanentlyDenied)
                        CameraPermissionState.PermanentlyDenied
                    else
                        CameraPermissionState.Denied
                )
            }
        }
    return {
        launcher.launch(Manifest.permission.CAMERA)
    }
}