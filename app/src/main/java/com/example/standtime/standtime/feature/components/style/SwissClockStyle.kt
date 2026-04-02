package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.standtime.standtime.feature.components.LocalGalleryScaleFactor
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource

@Composable
fun SwissClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    val scale = LocalGalleryScaleFactor.current
    Column(
        modifier = modifier
            .background(Brush.linearGradient(listOf(Color(0xFFE63946), Color(0xFFB91C1C))))
            .padding((16f * scale).coerceIn(10f, 24f).dp),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween
    ) {
        Text(
            localizedStringResource(R.string.gallery_swiss_title, language),
            color = Color.White,
            fontSize = (28f * scale).coerceIn(16f, 34f).sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            "${parts.hours}\n${parts.minutes}",
            color = Color.White,
            fontSize = (196f * scale).coerceIn(88f, 210f).sp,
            fontWeight = FontWeight.Bold,
            lineHeight = (154f * scale).coerceIn(72f, 166f).sp
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween, verticalAlignment = Alignment.Bottom) {
            Text(
                localizedStringResource(R.string.gallery_swiss_subtitle, language),
                color = Color.White,
                fontSize = (20f * scale).coerceIn(12f, 26f).sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                parts.seconds,
                color = Color.White.copy(alpha = 0.2f),
                fontSize = (72f * scale).coerceIn(34f, 80f).sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}
