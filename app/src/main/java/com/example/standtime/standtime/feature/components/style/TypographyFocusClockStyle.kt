package com.example.standtime.standtime.feature.components.style

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
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.components.LocalGalleryScaleFactor
import com.example.standtime.standtime.feature.utils.StandTimeLanguage

@Composable
fun TypographyFocusClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    val scale = LocalGalleryScaleFactor.current
    val hourInt = parts.hours.toIntOrNull() ?: 0
    val hourWord = when (language) {
        StandTimeLanguage.UZBEK -> getUzbekHourWord(hourInt)
        StandTimeLanguage.RUSSIAN -> getRussianHourWord(hourInt)
        else -> getEnglishHourWord(hourInt)
    }

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
                horizontal = (64f * scale).coerceIn(28f, 74f).dp,
                vertical = (42f * scale).coerceIn(24f, 56f).dp
            )
        ) {
            Text(
                text = if (language == StandTimeLanguage.UZBEK) "HOZIR" else "IT IS",
                color = Color(0xFF18181B),
                fontSize = (120f * scale).coerceIn(56f, 128f).sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                lineHeight = (100f * scale).coerceIn(48f, 106f).sp
            )
            Text(
                text = hourWord.uppercase(),
                color = Color(0xFF111827),
                fontSize = (120f * scale).coerceIn(56f, 128f).sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                lineHeight = (100f * scale).coerceIn(48f, 106f).sp
            )
            Text(
                text = when (language) {
                    StandTimeLanguage.UZBEK -> "DAN ${parts.minutes} DAQIQA"
                    StandTimeLanguage.RUSSIAN -> "${parts.minutes} МИНУТ"
                    else -> "PAST ${parts.minutes}"
                },
                color = Color(0xFF6B4F3A),
                fontSize = (120f * scale).coerceIn(56f, 128f).sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                lineHeight = (100f * scale).coerceIn(48f, 106f).sp
            )
            Text(
                text = if (language == StandTimeLanguage.UZBEK) "O'TDI." else "NOW.",
                color = Color(0xFF18181B),
                fontSize = (120f * scale).coerceIn(56f, 128f).sp,
                fontWeight = FontWeight.Black,
                fontStyle = FontStyle.Italic,
                lineHeight = (100f * scale).coerceIn(48f, 106f).sp
            )

            Box(
                modifier = Modifier
                    .padding(top = (48f * scale).coerceIn(20f, 52f).dp)
                    .width((128f * scale).coerceIn(72f, 138f).dp)
                    .height(4.dp)
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
