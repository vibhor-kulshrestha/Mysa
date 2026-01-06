package ai.mysmartassistant.mysa.ui.home.attachment

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.view.Gravity
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.savedstate.compose.LocalSavedStateRegistryOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner


@Composable
fun keyboardHeightDp(): Dp {
    val density = LocalDensity.current
    val imeBottom = WindowInsets.ime.getBottom(density)
    val navBottom = WindowInsets.navigationBars.getBottom(density)

    val heightPx = (imeBottom - navBottom).coerceAtLeast(0)

    return with(density) {
        heightPx.toDp()
    }
}

@Composable
fun PopupWindow(
    visibleState: MutableTransitionState<Boolean>,
    pinCoords: IntOffset,
    items: List<AttachmentItem>
) {
    val context = LocalContext.current
    val view = LocalView.current
    val keyboardHeight = keyboardHeightDp()
    val lifecycleOwner = LocalLifecycleOwner.current
    val savedStateRegistryOwner = LocalSavedStateRegistryOwner.current
    val compositionContext = rememberCompositionContext()

    val popupWindow = rememberAttachmentPopupWindow(context)
    var contentVisibleState by remember { mutableStateOf<MutableTransitionState<Boolean>?>(null) }

    LaunchedEffect(visibleState.targetState) {
        if (visibleState.targetState) {
            val newState = MutableTransitionState(false).apply { targetState = true }
            contentVisibleState = newState

            val composeView = ComposeView(context).apply {
                setViewTreeLifecycleOwner(lifecycleOwner)
                setViewTreeSavedStateRegistryOwner(savedStateRegistryOwner)
                setParentCompositionContext(compositionContext)

                setContent {
                    // contentVisibleState is non-null here
                    contentVisibleState?.let {
                        AttachmentPopupContent(
                            visibleState = it,
                            pinCoords = pinCoords,
                            keyboardHeight = keyboardHeight,
                            items = items
                        )
                    }
                }
            }
            popupWindow.contentView = composeView

            if (!popupWindow.isShowing) {
                try {
                    popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } else {
            contentVisibleState?.targetState = false
        }
    }

    val localState = contentVisibleState
    if (localState != null) {
        LaunchedEffect(localState.isIdle, localState.targetState) {
            if (localState.isIdle && !localState.targetState) {
                popupWindow.dismiss()
                contentVisibleState = null // Cleanup
            }
        }
    }

    DisposableEffect(Unit) {
        popupWindow.setOnDismissListener {
            if (visibleState.targetState) {
                visibleState.targetState = false
            }
        }
        onDispose {
            popupWindow.setOnDismissListener(null)
            popupWindow.dismiss()
        }
    }
}

@Composable
private fun rememberAttachmentPopupWindow(context: Context): PopupWindow {
    return remember {
        PopupWindow(context).apply {
            isOutsideTouchable = false
            isFocusable = true
            width = ViewGroup.LayoutParams.MATCH_PARENT
            height = ViewGroup.LayoutParams.MATCH_PARENT
            isClippingEnabled = false
            setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))

            isAttachedInDecor = false
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setIsClippedToScreen(false)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                windowLayoutType = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
            }
            elevation = 50f

            inputMethodMode = PopupWindow.INPUT_METHOD_NOT_NEEDED
            softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
private fun AttachmentPopupContent(
    visibleState: MutableTransitionState<Boolean>,
    pinCoords: IntOffset,
    keyboardHeight: Dp,
    items: List<AttachmentItem>
) {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val density = LocalDensity.current
    val screenWidthPx = with(density) { screenWidth.toPx() }
    val pivotX = if (screenWidthPx > 0) pinCoords.x / screenWidthPx else 0f

    val isKeyboardOpen = keyboardHeight > 0.dp
    val navBottom = WindowInsets.navigationBars.getBottom(density)

    val activeHeight = if (isKeyboardOpen) keyboardHeight else 320.dp
    val bottomPad = if (isKeyboardOpen) 0.dp else with(density) { (pinCoords.y + navBottom).toDp() }

    val pivotY = if (isKeyboardOpen) 0f else 1f
    val fastSpatialSpec = MaterialTheme.motionScheme.fastSpatialSpec<Float>()

    DisposableEffect(Unit) {
        onDispose { }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                visibleState.targetState = false
            }
    ) {
        AnimatedVisibility(
            visibleState = visibleState,
            enter = scaleIn(
                transformOrigin = TransformOrigin(pivotX, pivotY),
                animationSpec = fastSpatialSpec,
                initialScale = 0f
            )+ fadeIn(
                animationSpec = tween(100)
            ),
            exit = scaleOut(
                transformOrigin = TransformOrigin(pivotX, pivotY),
                animationSpec = tween(100),
                targetScale = 0f
            )+ fadeOut(
                animationSpec = tween(75)
            ),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = bottomPad)
                .padding(horizontal = 10.dp)
        ) {
            Box(
                modifier = Modifier
                    .width(screenWidth)
                    .height(activeHeight)
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,
                        MaterialTheme.shapes.large
                    )
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                    }
            ) {
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Adaptive(minSize = 100.dp),
                    contentPadding = PaddingValues(15.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(15.dp)
                ) {
                    items(items) { item ->
                        Attachment(item)
                    }
                }

            }
        }
    }
}

@Composable
fun Attachment(item: AttachmentItem) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.background,
                    MaterialTheme.shapes.medium
                )
                .border(
                    border = BorderStroke(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    ),
                    shape = MaterialTheme.shapes.medium
                )
                .padding(15.dp),
        ) {
            Icon(
                modifier = Modifier.size(35.dp),
                imageVector = item.icon,
                contentDescription = item.name,
                tint = item.color
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = item.name,
            overflow = TextOverflow.Ellipsis,
            maxLines = 2,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            )
        )
    }
}
