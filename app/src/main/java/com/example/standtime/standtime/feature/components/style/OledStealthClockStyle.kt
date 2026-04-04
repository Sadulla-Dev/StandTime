package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
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
fun OledStealthClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    val scale = LocalGalleryScaleFactor.current
    Box(
        modifier = modifier.background(Brush.linearGradient(listOf(Color.Black, Color(0xFF040404)))),
        contentAlignment = Alignment.Center
    ) {
        Box {
            Text(
                "${parts.hours}${parts.minutes}",
                modifier = Modifier.padding(start = 20.dp),
                color = Color.White.copy(alpha = 0.9f),
                fontSize = (168f * scale).coerceIn(82f, 176f).sp,
                fontWeight = FontWeight.Thin
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(top = (240f * scale).coerceIn(120f, 250f).dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height((2f * scale).coerceIn(1f, 3f).dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, Color.White.copy(alpha = 0.2f), Color.Transparent)
                            )
                        )
                )
                Text(
                    localizedStringResource(R.string.gallery_stealth_mode, language),
                    modifier = Modifier.padding(horizontal = (20f * scale).coerceIn(8f, 24f).dp),
                    color = Color.White.copy(alpha = 0.3f),
                    fontSize = (10f * scale).coerceIn(8f, 12f).sp,
                    letterSpacing = (6f * scale).coerceIn(2f, 7f).sp,
                    fontWeight = FontWeight.Bold
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height((2f * scale).coerceIn(1f, 3f).dp)
                        .background(
                            Brush.horizontalGradient(
                                listOf(Color.Transparent, Color.White.copy(alpha = 0.2f), Color.Transparent)
                            )
                        )
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun OledStealthClockStylePreview() = ClockStylePreviewFrame { modifier ->
    OledStealthClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
