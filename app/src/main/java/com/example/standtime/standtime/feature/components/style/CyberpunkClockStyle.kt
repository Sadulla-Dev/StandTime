package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage

@Composable
fun CyberpunkClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(Brush.linearGradient(listOf(Color.Black, Color(0xFF2E1065)))),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "${parts.hours}:${parts.minutes}", color = Color(0xFFC026D3), fontSize = 108.sp, fontWeight = FontWeight.Black)
        Text(text = "${parts.hours}:${parts.minutes}", modifier = Modifier.padding(start = 6.dp, top = 6.dp), color = accentColor, fontSize = 108.sp, fontWeight = FontWeight.Black)
    }
}
