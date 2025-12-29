package ai.mysmartassistant.mysa.ui.auth

import ai.mysmartassistant.mysa.R
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun HeroSection(
    modifier: Modifier = Modifier,
    scale: Float = 1f
) {
    Box(
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            },
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(R.drawable.ic_assistant),
            contentDescription = null,
            modifier = Modifier.heightIn(max = 420.dp),
            contentScale = ContentScale.Fit
        )

    }
}

@Composable
fun BrandHeader(
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.app_logo),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )

        Spacer(Modifier.width(12.dp))

        Text(
            text = "MySmartAssistant",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
fun LegalFooter(
    modifier: Modifier = Modifier,
    onTermsClick: () -> Unit = {},
    onPrivacyClick: () -> Unit = {}
) {
    val text = buildAnnotatedString {
        append("By continuing, you accept our ")

        pushLink(
            LinkAnnotation.Clickable(
                tag = "terms",
                linkInteractionListener = { onTermsClick() }
            )
        )
        withStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        ) {
            append("Terms & Conditions")
        }
        pop()

        append(" and ")

        pushLink(
            LinkAnnotation.Clickable(
                tag = "privacy",
                linkInteractionListener = { onPrivacyClick() }
            )
        )
        withStyle(
            SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        ) {
            append("Privacy Policy")
        }
        pop()
    }

    Text(
        text = text,
        modifier = modifier.padding(horizontal = 16.dp),
        style = MaterialTheme.typography.bodySmall.copy(
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}

@Composable
fun WhatsAppLoginButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 420.dp)
            .height(52.dp),
        shape = RoundedCornerShape(26.dp)
    ) {
        Icon(
            modifier = Modifier.size(25.dp),
            painter = painterResource(R.drawable.ic_whatsapp),
            contentDescription = null
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = "WhatsApp",
            style = MaterialTheme.typography.titleMedium
        )
    }
}

@Composable
fun SecondaryLoginRow(
    showTruecaller: Boolean = true,
    showMobile: Boolean = true,
    onTruecallerClick: () -> Unit = {},
    onMobileClick: () -> Unit = {}
) {
    val visibleCount = listOf(showTruecaller, showMobile).count { it }

    if (visibleCount == 1) {
        if (showTruecaller) {
            SecondaryButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Truecaller",
                icon = null,
                onClick = onTruecallerClick
            )
        } else {
            SecondaryButton(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Mobile",
                icon = R.drawable.ic_mobile,
                onClick = onMobileClick
            )
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 420.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (showTruecaller) {
                SecondaryButton(
                    modifier = Modifier.weight(1f),
                    text = "Truecaller",
                    icon = null,
                    onClick = onTruecallerClick
                )
            }
            if (showMobile) {
                SecondaryButton(
                    modifier = Modifier.weight(1f),
                    text = "Mobile",
                    icon = R.drawable.ic_mobile,
                    onClick = onMobileClick
                )
            }
        }
    }
}

@Composable
private fun SecondaryButton(
    modifier: Modifier = Modifier,
    text: String,
    @DrawableRes icon: Int?,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(48.dp),
        shape = RoundedCornerShape(24.dp)
    ) {
        icon?.let {
            Icon(
                modifier = Modifier.height(20.dp),
                painter = painterResource(icon),
                contentDescription = null
            )
        }
        Spacer(Modifier.width(6.dp))
        Text(text)
    }
}