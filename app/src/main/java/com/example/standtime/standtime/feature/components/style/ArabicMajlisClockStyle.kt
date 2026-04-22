package com.standtime.clock.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.standtime.clock.R
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import com.standtime.clock.standtime.feature.utils.localizedStringResource

@Composable
fun ArabicMajlisClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val primary = if (accentColor.alpha > 0f) accentColor.copy(alpha = 0.92f) else Color(0xFFE2725B)

    Row(
        modifier = modifier.background(
            Brush.linearGradient(
                listOf(
                    Color(0xFF020617),
                    Color(0xFF111827)
                )
            )
        ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = parts.hours.toArabicDigits(),
            color = primary,
            fontSize = 238.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Serif
        )
        Text(
            text = localizedStringResource(R.string.gallery_arabic_label, language),
            modifier = Modifier.padding(horizontal = 22.dp),
            color = Color.White.copy(alpha = 0.24f),
            fontSize = 38.sp,
            fontWeight = FontWeight.Medium,
            fontFamily = FontFamily.Serif
        )
        Text(
            text = parts.minutes.toArabicDigits(),
            color = primary,
            fontSize = 238.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily.Serif
        )
    }
}

private fun String.toArabicDigits(): String {
    val digits = mapOf(
        '0' to '٠',
        '1' to '١',
        '2' to '٢',
        '3' to '٣',
        '4' to '٤',
        '5' to '٥',
        '6' to '٦',
        '7' to '٧',
        '8' to '٨',
        '9' to '٩'
    )
    return map { digits[it] ?: it }.joinToString("")
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun ArabicMajlisClockStylePreview() = ClockStylePreviewFrame { modifier ->
    ArabicMajlisClockStyle(
        ClockStylePreviewParts,
        StandTimeLanguage.ENGLISH,
        ClockStylePreviewAccent,
        modifier
    )
}
