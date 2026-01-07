package ai.mysmartassistant.mysa.camera.ui

import ai.mysmartassistant.mysa.camera.viewmodel.CameraViewModel
import ai.mysmartassistant.mysa.ui.common.SafeArea
import androidx.camera.compose.CameraXViewfinder
import androidx.camera.core.SurfaceRequest
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlipCameraIos
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.net.Uri
import coil.compose.AsyncImage
import coil.request.ImageRequest

@Composable
fun CameraScreen(
    viewModel: CameraViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val surfaceRequest by viewModel.surfaceRequest
        .collectAsStateWithLifecycle()
    val currentLifecycleOwner = LocalLifecycleOwner.current
    
    // Camera start effect
    LaunchedEffect(currentLifecycleOwner) {
        viewModel.startCamera(currentLifecycleOwner)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Camera Preview
        if (uiState.lastPhotoUri == null) {
            CameraPreview(
                surfaceRequest = surfaceRequest,
                modifier = Modifier.fillMaxSize()
            )
        } else {
             AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(uiState.lastPhotoUri)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // Camera Controls
        CameraControls(
            onCapture = { viewModel.takePhoto() },
            onSwitchCamera = { viewModel.toggleCamera(currentLifecycleOwner) },
            lastPhotoUri = uiState.lastPhotoUri,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(bottom = 48.dp)
        )
    }
}

@Composable
fun CameraControls(
    onCapture: () -> Unit,
    onSwitchCamera: () -> Unit,
    lastPhotoUri: Uri?,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Gallery / Last Photo
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                .border(2.dp, Color.White, CircleShape)
                .clip(CircleShape)
        ) {
            if (lastPhotoUri != null) {
                AsyncImage(
                    model = lastPhotoUri,
                    contentDescription = "Gallery",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        // Shutter Button
        ShutterButton(onClick = onCapture)

        // Switch Camera
        var rotationState by remember { mutableStateOf(0f) }
        val rotation by animateFloatAsState(
            targetValue = rotationState,
            animationSpec = tween(durationMillis = 300)
        )
        
        IconButton(
            onClick = {
                rotationState += 180f
                onSwitchCamera()
            },
            modifier = Modifier
                .size(56.dp)
                .background(Color.Black.copy(alpha = 0.5f), CircleShape)
        ) {
            Icon(
                imageVector = Icons.Default.FlipCameraIos,
                contentDescription = "Switch Camera",
                tint = Color.White,
                modifier = Modifier.rotate(rotation)
            )
        }
    }
}

@Composable
fun ShutterButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.85f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy)
    )

    Box(
        modifier = Modifier
            .size(84.dp)
            .scale(scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Outer ring
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(4.dp, Color.White, CircleShape)
        )
        // Inner circle
        Box(
            modifier = Modifier
                .size(68.dp)
                .background(Color.White, CircleShape)
        )
    }
}

@Composable
fun CameraPreview(surfaceRequest: SurfaceRequest?, modifier: Modifier) {
    surfaceRequest?.let {
        CameraXViewfinder(
            surfaceRequest = it,
            modifier = modifier
        )
    }
}