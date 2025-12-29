package ai.mysmartassistant.mysa.viewmodel

import ai.mysmartassistant.mysa.core.network.ApiResult
import ai.mysmartassistant.mysa.domain.onboard.UpdateProfileUseCase
import ai.mysmartassistant.mysa.ui.onboard.OnboardingAction
import ai.mysmartassistant.mysa.ui.onboard.OnboardingEvent
import ai.mysmartassistant.mysa.ui.onboard.OnboardingUIState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(
    private val updateProfileUseCase: UpdateProfileUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUIState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<OnboardingEvent>()
    val events = _events.asSharedFlow()

    val isFormValid: StateFlow<Boolean> =
        _uiState.map {
            it.firstName.isNotBlank() &&
                    it.lastName.isNotBlank() &&
                    it.gender != null
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun onAction(action: OnboardingAction) {
        when (action) {

            is OnboardingAction.FirstNameChanged -> {
                updateState { copy(firstName = action.value) }
            }

            is OnboardingAction.LastNameChanged -> {
                updateState { copy(lastName = action.value) }
            }

            is OnboardingAction.GenderChanged -> {
                updateState { copy(gender = action.gender) }
            }

            OnboardingAction.Submit -> {
                submit()
            }
        }
    }

    private fun submit() {
        if (!isFormValid.value || uiState.value.isSubmitting) return

        viewModelScope.launch {
            updateState { copy(isSubmitting = true, error = null) }

            when (val result = updateProfileUseCase(
                firstName = uiState.value.firstName,
                lastName = uiState.value.lastName,
                gender = uiState.value.gender!!.name.uppercase(),
                language = uiState.value.language
            )
            ) {
                is ApiResult.Error -> updateState {
                    copy(
                        isSubmitting = false,
                        error = "something went wrong"
                    )
                }

                is ApiResult.Success -> TODO()
            }
        }
    }

    private fun updateState(
        reducer: OnboardingUIState.() -> OnboardingUIState
    ) {
        _uiState.update(reducer)
    }
}