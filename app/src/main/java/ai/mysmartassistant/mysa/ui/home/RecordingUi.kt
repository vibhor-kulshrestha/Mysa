package ai.mysmartassistant.mysa.ui.home

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun RecordingContent(
    modifier: Modifier = Modifier,
    dragOffset: Float
) {
    // Timer Logic
    var timeMillis by remember { mutableLongStateOf(0L) }
    LaunchedEffect(Unit) {
        val startTime = System.currentTimeMillis()
        while (true) {
            timeMillis = System.currentTimeMillis() - startTime
            delay(100) // Update every 100ms
        }
    }

    // Blinking alpha for red mic/dot
    val alphaAnim = remember { Animatable(1f) }
    LaunchedEffect(Unit) {
        while (true) {
            alphaAnim.animateTo(0f, tween(500, easing = LinearEasing))
            alphaAnim.animateTo(1f, tween(500, easing = LinearEasing))
        }
    }

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: Mic & Timer
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Mic,
                contentDescription = "Recording",
                tint = Color.Red,
                modifier = Modifier
                    .size(24.dp)
                    .alpha(alphaAnim.value)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = formatTime(timeMillis),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        // Right: Slide to cancel
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .offset { IntOffset(dragOffset.roundToInt(), 0) }
                .alpha(if (dragOffset < -10) 0.5f else 1f) // Fade out as dragged far? Or keep visible?
            // WhatsApp keeps it visible until specific point. Left as is.
        ) {
            Icon(
                imageVector = Icons.Default.ChevronLeft,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Slide to cancel",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun RecordingCancellationOverlay(
    micStartOffset: IntOffset,
    binTargetOffset: IntOffset,
    onAnimationFinished: () -> Unit
) {
    val micX = remember { Animatable(micStartOffset.x.toFloat()) }
    val micY = remember { Animatable(micStartOffset.y.toFloat()) }
    val micRotation = remember { Animatable(0f) }
    val micScale = remember { Animatable(1f) }

    val binScale = remember { Animatable(0f) }
    val binLidRotation = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            binScale.animateTo(1f, tween(300, easing = FastOutSlowInEasing))
        }

        launch {
            micX.animateTo(binTargetOffset.x.toFloat(), tween(600, easing = FastOutSlowInEasing))
        }
        launch {
            micY.animateTo(micStartOffset.y - 100f, tween(300, easing = FastOutSlowInEasing))
            micY.animateTo(binTargetOffset.y.toFloat(), tween(300, easing = FastOutLinearInEasing))
        }
        launch {
            micRotation.animateTo(-360f, tween(600, easing = LinearEasing)) // Spin
        }

        delay(400)

        launch {
            binLidRotation.animateTo(-45f, tween(100))
        }

        delay(200)

        launch {
            micScale.animateTo(0f, tween(100))
        }

        delay(100)

        binLidRotation.animateTo(0f, tween(150))

        delay(100)

        binScale.animateTo(0f, tween(200))

        delay(200)
        onAnimationFinished()
    }

    Box(modifier = Modifier.fillMaxWidth().height(70.dp)) {
        // Mic Icon (Flying)
        Box(
            modifier = Modifier
                .offset { IntOffset(micX.value.roundToInt(), micY.value.roundToInt()) }
                .graphicsLayer {
                    rotationZ = micRotation.value
                    scaleX = micScale.value
                    scaleY = micScale.value
                }
                .size(44.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Mic, null, tint = Color.Red)
        }

        Box(
            modifier = Modifier
                .offset { binTargetOffset }
                .scale(binScale.value)
        ) {
            DustbinIcon(lidRotation = binLidRotation.value)
        }
    }
}

@Composable
fun DustbinIcon(lidRotation: Float) {
    // Custom drawing for Dustbin to allow lid rotation
    Canvas(modifier = Modifier.size(50.dp)) {
        val w = size.width
        val h = size.height

        // Bin Body
        drawPath(
            path = Path().apply {
                moveTo(w * 0.25f, h * 0.35f)
                lineTo(w * 0.3f, h * 0.9f)
                quadraticTo(w * 0.5f, h * 1.0f, w * 0.7f, h * 0.9f)
                lineTo(w * 0.75f, h * 0.35f)
                close()
            },
            color = Color.Gray,
        )

        // Lid
        // Rotate around hinge/corner
        val lidPath = Path().apply {
            moveTo(w * 0.2f, h * 0.35f)
            lineTo(w * 0.8f, h * 0.35f) // Base line
            lineTo(w * 0.75f, h * 0.25f)
            lineTo(w * 0.25f, h * 0.25f)
            close()
            // Handle
            moveTo(w * 0.45f, h * 0.25f)
            lineTo(w * 0.45f, h * 0.15f)
            lineTo(w * 0.55f, h * 0.15f)
            lineTo(w * 0.55f, h * 0.25f)
        }

        // We want to rotate the LID path. 
        // Simpler: Draw body and use graphicsLayer for lid? Canvas lets us rotate logic.

        rotate(degrees = lidRotation, pivot = Offset(w * 0.2f, h * 0.35f)) {
            drawPath(lidPath, color = Color.Gray)
        }
    }
}


private fun formatTime(millis: Long): String {
    val sec = (millis / 1000) % 60
    val min = (millis / 1000) / 60
    return "%02d:%02d".format(min, sec)
}
