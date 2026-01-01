package ai.mysmartassistant.mysa.viewmodel.start

import ai.mysmartassistant.mysa.domain.session.SessionRepository
import ai.mysmartassistant.mysa.ui.navigation.Route
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class AppStartViewModel @Inject constructor(
    private val sessionRepository: SessionRepository
) : ViewModel() {
    val _startDestination = MutableStateFlow<String?>(null)
    val startDestination: StateFlow<String?> = _startDestination.asStateFlow()

    init {
        viewModelScope.launch {
            val isLoggedIn = sessionRepository.hasValidSession()
            _startDestination.value =
                if (isLoggedIn) Route.Home.route
                else Route.Home.route
        }
    }
}