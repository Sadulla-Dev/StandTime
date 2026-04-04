package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource

@Composable
fun TokyoClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.background(Brush.linearGradient(listOf(Color(0xFF020617), Color(0xFF111827)))),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(parts.kanjiHours, color = Color(0xFFEF4444), fontSize = 142.sp, fontWeight = FontWeight.Black)
        Text(localizedStringResource(R.string.gallery_tokyo_label, language), modifier = Modifier.padding(horizontal = 20.dp), color = Color.White.copy(alpha = 0.2f), fontSize = 26.sp)
        Text(parts.kanjiMinutes, color = Color(0xFFEF4444), fontSize = 142.sp, fontWeight = FontWeight.Black)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun TokyoClockStylePreview() = ClockStylePreviewFrame { modifier ->
    TokyoClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
