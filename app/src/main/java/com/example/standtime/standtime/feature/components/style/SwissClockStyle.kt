package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.components.LocalGalleryScaleFactor
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource

@Composable
fun SwissClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val scale = LocalGalleryScaleFactor.current

    val mainFontSize = (160f * scale).coerceIn(80f, 180f).sp
    val mainLineHeight = (130f * scale).coerceIn(68f, 150f).sp

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(Color(0xFFE63946), Color(0xFFB91C1C))))
            .padding((24f * scale).coerceIn(16f, 32f).dp), // Paddingni biroz kengaytirdik
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = localizedStringResource(R.string.gallery_swiss_title, language),
            color = Color.White,
            fontSize = (24f * scale).coerceIn(14f, 30f).sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = (-1).sp
        )

        Text(
            text = "${parts.hours}\n${parts.minutes}",
            color = Color.White,
            style = TextStyle(
                fontSize = mainFontSize,
                fontWeight = FontWeight.Bold,
                lineHeight = mainLineHeight,
                letterSpacing = (-4).sp
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Text(
                text = localizedStringResource(R.string.gallery_swiss_subtitle, language),
                color = Color.White,
                fontSize = (18f * scale).coerceIn(12f, 24f).sp,
                fontWeight = FontWeight.Medium
            )

            Text(
                text = parts.seconds,
                color = Color.White.copy(alpha = 0.25f),
                fontSize = (60f * scale).coerceIn(30f, 70f).sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun SwissClockStylePreview() = ClockStylePreviewFrame { modifier ->
    SwissClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
