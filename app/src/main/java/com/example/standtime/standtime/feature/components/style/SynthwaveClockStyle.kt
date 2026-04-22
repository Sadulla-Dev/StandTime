package com.standtime.clock.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage

@Composable
fun SynthwaveClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(Brush.linearGradient(listOf(Color(0xFF120422), Color(0xFF312E81)))),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "${parts.hours}:${parts.minutes}", color = Color(0xFF67E8F9), fontSize = 168.sp, fontWeight = FontWeight.Black)
        Text(text = "${parts.hours}:${parts.minutes}", modifier = Modifier.offset(y = 8.dp), color = Color(0xFFF472B6).copy(alpha = 0.55f), fontSize = 168.sp, fontWeight = FontWeight.Black)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun SynthwaveClockStylePreview() = ClockStylePreviewFrame { modifier ->
    SynthwaveClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
