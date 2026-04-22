package com.standtime.clock.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.components.LocalGalleryScaleFactor
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage

@Composable
fun TypographyFocusClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val scale = LocalGalleryScaleFactor.current
    val hourInt = parts.hours.toIntOrNull() ?: 0
    val hourWord = when (language) {
        StandTimeLanguage.UZBEK -> getUzbekHourWord(hourInt)
        StandTimeLanguage.RUSSIAN -> getRussianHourWord(hourInt)
        else -> getEnglishHourWord(hourInt)
    }


    val baseFontSize = (78f * scale).coerceIn(42f, 88f).sp
    val baseLineHeight = (70f * scale).coerceIn(38f, 78f).sp

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFFF1E8D8),
                        Color(0xFFE4D8C4),
                        Color(0xFFD7C8AE)
                    )
                )
            ),
        contentAlignment = Alignment.CenterStart
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = (48f * scale).coerceIn(24f, 60f).dp,
                vertical = (32f * scale).coerceIn(20f, 48f).dp
            )
        ) {

            Text(
                text = if (language == StandTimeLanguage.UZBEK) "HOZIR" else "IT IS",
                color = Color(0xFF18181B),
                fontSize = baseFontSize,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                lineHeight = baseLineHeight
            )

            Text(
                text = hourWord.uppercase(),
                color = Color(0xFF111827),
                fontSize = baseFontSize,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                lineHeight = baseLineHeight
            )

            Text(
                text = when (language) {
                    StandTimeLanguage.UZBEK -> "DAN ${parts.minutes} DAQIQA"
                    StandTimeLanguage.RUSSIAN -> "${parts.minutes} МИНУТ"
                    else -> "PAST ${parts.minutes}"
                },
                color = Color(0xFF6B4F3A),
                fontSize = baseFontSize,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                lineHeight = baseLineHeight
            )

            Text(
                text = if (language == StandTimeLanguage.UZBEK) "O'TDI." else "NOW.",
                color = Color(0xFF18181B),
                fontSize = baseFontSize,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                lineHeight = baseLineHeight
            )


            Box(
                modifier = Modifier
                    .padding(top = (32f * scale).coerceIn(16f, 40f).dp)
                    .width((84f * scale).coerceIn(48f, 100f).dp)
                    .height(3.dp)
                    .background(accentColor.copy(alpha = 0.9f))
            )
        }
    }
}

private fun getUzbekHourWord(hour: Int): String {
    val words = listOf("O'n ikki", "Bir", "Ikki", "Uch", "To'rt", "Besh", "Olti", "Yetti", "Sakkiz", "To'qqiz", "O'n", "O'n bir")
    return words[hour % 12]
}

private fun getEnglishHourWord(hour: Int): String {
    val words = listOf("Twelve", "One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight", "Nine", "Ten", "Eleven")
    return words[hour % 12]
}

private fun getRussianHourWord(hour: Int): String {
    val words = listOf("Двенадцать", "Один", "Два", "Три", "Четыре", "Пять", "Шесть", "Семь", "Восемь", "Девять", "Десять", "Одиннадцать")
    return words[hour % 12]
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun TypographyFocusClockStylePreview() = ClockStylePreviewFrame { modifier ->
    TypographyFocusClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
