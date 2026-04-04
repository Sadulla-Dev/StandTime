package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource

@Composable
fun TerminalClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.background(Brush.linearGradient(listOf(Color.Black, Color(0xFF031806)))).padding(start = 40.dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center
    ) {
        Text(text = localizedStringResource(R.string.gallery_terminal_init, language), color = Color(0xFF22C55E), fontFamily = FontFamily.Monospace, fontSize = 22.sp)
        Text(text = "${parts.hours}_${parts.minutes}", modifier = Modifier.padding(top = 12.dp), color = Color(0xFF22C55E), fontFamily = FontFamily.Monospace, fontSize = 100.sp, fontWeight = FontWeight.Bold)
        Text(text = localizedStringResource(R.string.gallery_terminal_status, language,parts.batteryInfo), modifier = Modifier.padding(top = 12.dp), color = Color(0xFF22C55E).copy(alpha = 0.7f), fontFamily = FontFamily.Monospace, fontSize = 18.sp)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun TerminalClockStylePreview() = ClockStylePreviewFrame { modifier ->
    TerminalClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
