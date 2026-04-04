package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage

@Composable
fun LofiClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(listOf(Color(0xFF1A1C2C), Color(0xFF0F172A))))
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .clip(RoundedCornerShape(72.dp))
                .background(Color.Black.copy(alpha = 0.24f))
                .padding(horizontal = 72.dp, vertical = 54.dp)
        ) {
            Text(
                text = "${parts.hours}:${parts.minutes}",
                style = TextStyle(
                    fontWeight = FontWeight.ExtraLight,
                    fontSize = 138.sp,
                    letterSpacing = 9.sp
                ),
                color = Color(0xFFC7D2FE)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun LofiClockStylePreview() = ClockStylePreviewFrame { modifier ->
    LofiClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
