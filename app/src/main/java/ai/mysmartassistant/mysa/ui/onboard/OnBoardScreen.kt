package ai.mysmartassistant.mysa.ui.onboard

import ai.mysmartassistant.mysa.ui.auth.BrandHeader
import ai.mysmartassistant.mysa.viewmodel.OnboardViewModel
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun OnBoardScreen(
    windowSizeClass: WindowSizeClass,
    onOnBoardSuccess: () -> Unit,
    viewModel: OnboardViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.events.collect { it ->
            when (it) {
                OnboardingEvent.NavigateToHome -> onOnBoardSuccess()
            }
        }
    }
    val isFormValid = viewModel.isFormValid.collectAsStateWithLifecycle()
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AnimatedContent(
                targetState = windowSizeClass.widthSizeClass,
                label = "OnBoardLayoutTransition"
            ) { widthSizeClass ->
                when (widthSizeClass) {
                    WindowWidthSizeClass.Compact -> {
                        OnBoardCompact(
                            uiState = uiState.value,
                            isFormValid = isFormValid.value,
                            onAction = viewModel::onAction
                        )
                    }

                    WindowWidthSizeClass.Medium -> {
                        OnBoardMedium(
                            uiState = uiState.value,
                            isFormValid = isFormValid.value,
                            onAction = viewModel::onAction
                        )
                    }

                    WindowWidthSizeClass.Expanded -> {
                        OnBoardExpanded(
                            uiState = uiState.value,
                            isFormValid = isFormValid.value,
                            onAction = viewModel::onAction
                        )
                    }

                    else -> {
                        OnBoardCompact(
                            uiState = uiState.value,
                            isFormValid = isFormValid.value,
                            onAction = viewModel::onAction
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OnBoardCompact(
    uiState: OnboardingUIState,
    isFormValid: Boolean,
    onAction: (OnboardingAction) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            BrandHeader()
            Spacer(Modifier.height(16.dp))
            AssistantIllustration()
            Spacer(Modifier.height(16.dp))
            HeaderText()
        }

        Column {
            NameFields(uiState, onAction)
            Spacer(Modifier.height(12.dp))
            GenderSelector(
                selected = uiState.gender,
                onSelect = { onAction(OnboardingAction.GenderChanged(it)) }
            )
            Spacer(Modifier.height(32.dp))
            PrimaryContinueButton(
                enabled = isFormValid && !uiState.isSubmitting,
                isSubmitting = uiState.isSubmitting,
                onClick = { onAction(OnboardingAction.Submit) }
            )
            Spacer(Modifier.height(64.dp)) // CTA space + bottom padding
        }
    }
}

@Composable
private fun OnBoardMedium(
    uiState: OnboardingUIState,
    isFormValid: Boolean,
    onAction: (OnboardingAction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BrandHeader()
                Spacer(modifier = Modifier.weight(1f))
                AssistantIllustration()
                Spacer(modifier = Modifier.weight(1f))
            }

        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 32.dp)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.weight(1f)) // Push content to center if possible, or just space
            HeaderText()
            Spacer(Modifier.height(32.dp))
            NameFields(uiState, onAction)
            Spacer(Modifier.height(16.dp))
            GenderSelector(
                selected = uiState.gender,
                onSelect = { onAction(OnboardingAction.GenderChanged(it)) }
            )
            Spacer(Modifier.height(32.dp))
            PrimaryContinueButton(
                enabled = isFormValid && !uiState.isSubmitting,
                isSubmitting = uiState.isSubmitting,
                onClick = { onAction(OnboardingAction.Submit) }
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun OnBoardExpanded(
    uiState: OnboardingUIState,
    isFormValid: Boolean,
    onAction: (OnboardingAction) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp)
    ) {
        // Weighted 1.2f to match LoginExpanded
        Box(
            modifier = Modifier.weight(1.2f),
            contentAlignment = Alignment.Center
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BrandHeader()
                Spacer(modifier = Modifier.weight(1f))
                AssistantIllustration()
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .widthIn(max = 420.dp)
                .fillMaxHeight()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(1f))
            HeaderText()
            Spacer(Modifier.height(32.dp))
            NameFields(uiState, onAction)
            Spacer(Modifier.height(16.dp))
            GenderSelector(
                selected = uiState.gender,
                onSelect = { onAction(OnboardingAction.GenderChanged(it)) }
            )
            Spacer(Modifier.height(32.dp))
            PrimaryContinueButton(
                enabled = isFormValid && !uiState.isSubmitting,
                isSubmitting = uiState.isSubmitting,
                onClick = { onAction(OnboardingAction.Submit) }
            )
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}




