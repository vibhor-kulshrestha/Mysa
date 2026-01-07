package ai.mysmartassistant.mysa.ui.home

import ai.mysmartassistant.mysa.camera.ui.CameraPermissionState
import ai.mysmartassistant.mysa.camera.ui.rememberCameraPermission
import ai.mysmartassistant.mysa.ui.common.AppDrawer
import ai.mysmartassistant.mysa.ui.common.ResponsiveNavScaffold
import ai.mysmartassistant.mysa.ui.home.attachment.AttachmentItem
import ai.mysmartassistant.mysa.ui.home.attachment.PopupWindow
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.UploadFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntOffset

@Composable
fun ChatScreen(
    windowSizeClass: WindowSizeClass,
    openCamera: () -> Unit,
) {
    val context = LocalContext.current
    var showPermissionDialog by remember { mutableStateOf(false) }
    val requestCameraPermission =
        rememberCameraPermission { result ->
            when (result) {
                CameraPermissionState.Granted ->
                    openCamera()

                CameraPermissionState.Denied ->
                    showPermissionDialog = true

                CameraPermissionState.PermanentlyDenied ->
                    showPermissionDialog = true
            }
        }

    var text by rememberSaveable { mutableStateOf("") }
    var dragX by rememberSaveable { mutableFloatStateOf(0f) }
    var isRecording by rememberSaveable { mutableStateOf(false) }
    val visibleState = remember { MutableTransitionState(false) }
    var pinCoords by remember { mutableStateOf(IntOffset.Zero) }
    val inputState = ChatInputUiState(
        text = text,
        isRecording = isRecording,
        dragX = dragX
    )

    ResponsiveNavScaffold(
        windowSizeClass = windowSizeClass,
        isHomeScreen = true,
        appBar = {
            HomeAppBar {

            }
        },
        drawerContent = {
            AppDrawer()
        }
    ) { _ ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
        ) {

            ChatMessages(
                modifier = Modifier.weight(1f)
            )

            ChatInputBar(
                state = inputState,
                onEvent = { event ->
                    when (event) {
                        ChatInputEvent.AttachClicked -> {
                            visibleState.targetState = !visibleState.targetState
                        }

                        ChatInputEvent.EmojiClicked -> {}
                        is ChatInputEvent.DragX -> dragX = event.value
                        is ChatInputEvent.AttachmentPosition -> pinCoords = event.value
                        ChatInputEvent.SendClicked -> {}
                        is ChatInputEvent.TextChanged -> text = event.value
                        ChatInputEvent.RecordingStart -> {
                            isRecording = true
                        }

                        ChatInputEvent.RecordingEnd -> {
                            isRecording = false
                        }

                        ChatInputEvent.RecordingCanceled -> {
                            isRecording = false
                        }

                        ChatInputEvent.OpenCamera -> requestCameraPermission()
                    }
                }
            )
        }
        PopupWindow(
            visibleState = visibleState,
            pinCoords = pinCoords,
            listOf(
                AttachmentItem(
                    name = "Set\nReminders",
                    icon = Icons.Outlined.Notifications,
                    Color(0xFF49C97D)
                ),
                AttachmentItem(
                    name = "Schedule\nMeetings",
                    icon = Icons.Outlined.CalendarMonth,
                    Color(0xFF0FA9E2)
                ),
                AttachmentItem(
                    name = "Upload\nDocuments",
                    icon = Icons.Outlined.UploadFile,
                    Color(0xFFA68EFF)
                ),
                AttachmentItem(
                    name = "Take\nNotes",
                    icon = Icons.Outlined.EditNote,
                    Color(0xFFD2A351)
                ),
            )
        )
        if (showPermissionDialog) {
            CameraPermissionInfoDialog(
                onOpenSettings = {
                    openAppSettings(context)
                },
                onDismiss = {
                    showPermissionDialog = false
                }
            )
        }
    }
}

@Composable
fun CameraPermissionInfoDialog(
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Camera permission needed") },
        text = {
            Text("Camera access is required to take and send photos.")
        },
        confirmButton = {
            TextButton(onClick = onOpenSettings) {
                Text("Open Settings")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

fun openAppSettings(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    context.startActivity(intent)
}

@Composable
fun ChatMessages(modifier: Modifier) {
    Box(
        modifier = modifier
    ) {

    }
}
