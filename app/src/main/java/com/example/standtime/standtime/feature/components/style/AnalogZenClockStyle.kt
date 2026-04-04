package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.standtime.standtime.feature.components.ClockHand
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage

@Composable
fun AnalogZenClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    val hour = parts.hours.toIntOrNull() ?: 0
    val minute = parts.minutes.toIntOrNull() ?: 0
    val hourAngle = (hour % 12) * 30f + minute * 0.5f
    val minuteAngle = minute * 6f
    Box(
        modifier = modifier.background(Brush.linearGradient(listOf(Color(0xFFF8FAFC), Color(0xFFE5E7EB)))),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(340.dp)
                .clip(CircleShape)
                .background(Color(0xFFFCFCFD))
                .border(2.dp, Color(0xFFD4D4D8), CircleShape)
        ) {
            ClockHand(length = 90.dp, width = 6.dp, angle = hourAngle, color = Color.Black)
            ClockHand(length = 126.dp, width = 3.dp, angle = minuteAngle, color = Color(0xFF9CA3AF))
            Box(modifier = Modifier.align(Alignment.Center).size(12.dp).clip(CircleShape).background(Color(0xFFEF4444)))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun AnalogZenClockStylePreview() = ClockStylePreviewFrame { modifier ->
    AnalogZenClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
