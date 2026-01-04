package ai.mysmartassistant.mysa.ui.home.attachment

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.AudioFile
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

val attachmentItems =  listOf(
    AttachmentItem("Document", Icons.Default.AttachFile, Color(0xFF7F66FF)),
    AttachmentItem("Camera", Icons.Default.CameraAlt, Color(0xFFE91E63)),
    AttachmentItem("Gallery", Icons.Default.Image, Color(0xFF9C27B0)),
    AttachmentItem("Audio", Icons.Default.AudioFile, Color(0xFFFF9800)),
)
@Composable
fun AttachmentScreen(
    isVisible: Boolean,
    items: List<AttachmentItem>,
    isKeyboardOpen: Boolean,
    keyboardHeight: Dp,
    maxFloatingHeight: Dp,
    pinPosition: Offset,
    onDismiss: () -> Unit
) {

}