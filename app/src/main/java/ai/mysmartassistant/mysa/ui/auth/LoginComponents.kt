package ai.mysmartassistant.mysa.ui.auth

import ai.mysmartassistant.mysa.data.auth.TruecallerOAuthManager
import ai.mysmartassistant.mysa.viewmodel.auth.LoginViewModel
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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoginCompact(
    uiState: LoginUIState,
    viewModel: LoginViewModel,
    truecallerManager: TruecallerOAuthManager,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        BrandHeader()
        Spacer(Modifier.height(12.dp))
        HeroSection()
        Spacer(Modifier.height(10.dp))
        LoginSection(uiState, viewModel, truecallerManager)
        Spacer(Modifier.height(16.dp))
        LegalFooter()
    }
}

@Composable
fun LoginMedium(
    uiState: LoginUIState,
    viewModel: LoginViewModel,
    truecallerManager: TruecallerOAuthManager,
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
            HeroSection(scale = 0.9f)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 32.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BrandHeader()
            Spacer(modifier = Modifier.weight(1f))
            LoginSection(uiState, viewModel, truecallerManager)
            Spacer(Modifier.height(16.dp))
            LegalFooter()
        }
    }
}

@Composable
fun LoginExpanded(
    uiState: LoginUIState,
    viewModel: LoginViewModel,
    truecallerManager: TruecallerOAuthManager,
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp)
    ) {
        HeroSection(
            modifier = Modifier.weight(1.2f),
            scale = 1.05f
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .widthIn(max = 420.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            BrandHeader()
            Spacer(modifier = Modifier.weight(1f))
            LoginSection(uiState, viewModel, truecallerManager)
            Spacer(Modifier.height(24.dp))
            LegalFooter()
        }
    }
}