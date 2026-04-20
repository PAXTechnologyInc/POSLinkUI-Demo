package com.paxus.pay.poslinkui.demo.ui.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.paxus.pay.poslinkui.demo.ui.theme.PosLinkDesignTokens

/**
 * Unified text entry point for Compose screens so typography roles stay consistent.
 *
 * @param text Display text.
 * @param modifier Optional modifier.
 * @param role Semantic typography role used by the screen.
 * @param color Optional text color override.
 * @param textAlign Optional alignment.
 */
@Composable
fun PosLinkText(
    text: String,
    modifier: Modifier = Modifier,
    role: PosLinkTextRole = PosLinkTextRole.Body,
    color: Color = role.defaultColor(),
    textAlign: TextAlign? = null
) {
    Text(
        text = text,
        modifier = modifier,
        style = role.textStyle(),
        color = color,
        textAlign = textAlign
    )
}

enum class PosLinkTextRole {
    ScreenTitle,
    /** 与 `fragment_confirmation.xml` 中 message TextView 一致：24sp（text_size_title）、字重 regular。 */
    ConfirmationMessage,
    SectionTitle,
    Body,
    Supporting,
    Status
}

@Composable
private fun PosLinkTextRole.textStyle() = when (this) {
    PosLinkTextRole.ScreenTitle -> MaterialTheme.typography.titleLarge
    PosLinkTextRole.ConfirmationMessage ->
        MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal)
    PosLinkTextRole.SectionTitle -> MaterialTheme.typography.titleMedium
    PosLinkTextRole.Body -> MaterialTheme.typography.bodyLarge
    PosLinkTextRole.Supporting -> MaterialTheme.typography.bodyMedium
    PosLinkTextRole.Status -> MaterialTheme.typography.headlineSmall
}

private fun PosLinkTextRole.defaultColor() = when (this) {
    PosLinkTextRole.Supporting -> PosLinkDesignTokens.SecondaryTextColor
    else -> PosLinkDesignTokens.PrimaryTextColor
}
