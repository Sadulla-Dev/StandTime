package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.components.LocalGalleryScaleFactor
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource

@Composable
fun AbstractGeometricClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    val scale = LocalGalleryScaleFactor.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(Color(0xFFF9FAFB), Color(0xFFF3F4F6)))),
        contentAlignment = Alignment.Center
    ) {
        // Large dark circle
        Box(
            modifier = Modifier
                .size((480f * scale).coerceIn(240f, 560f).dp)
                .offset(
                    x = (-220f * scale).coerceIn((-260f), (-100f)).dp,
                    y = (-160f * scale).coerceIn((-200f), (-70f)).dp
                )
                .background(Color(0xFF18181B).copy(alpha = 0.9f), CircleShape)
        )

        // Orange square border
        Box(
            modifier = Modifier
                .size((400f * scale).coerceIn(220f, 500f).dp)
                .offset(x = (240f * scale).coerceIn(110f, 280f).dp)
                .rotate(45f)
                .border(
                    (24f * scale).coerceIn(10f, 30f).dp,
                    Color(0xFFF97316).copy(alpha = 0.2f),
                    RectangleShape
                )
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = parts.hours,
                color = Color.Black,
                fontSize = (240f * scale).coerceIn(110f, 260f).sp,
                fontWeight = FontWeight.Black,
                lineHeight = (200f * scale).coerceIn(92f, 220f).sp,
                modifier = Modifier.offset(x = (-40f * scale).coerceIn((-52f), (-18f)).dp)
            )
            Text(
                text = parts.minutes,
                color = Color.Black.copy(alpha = 0.6f),
                fontSize = (180f * scale).coerceIn(84f, 200f).sp,
                fontWeight = FontWeight.Light,
                lineHeight = (160f * scale).coerceIn(74f, 182f).sp,
                modifier = Modifier.offset(
                    x = (40f * scale).coerceIn(18f, 52f).dp,
                    y = (-40f * scale).coerceIn((-52f), (-18f)).dp
                )
            )
        }

        // Gallery Edition Label
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding((48f * scale).coerceIn(18f, 56f).dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = localizedStringResource(R.string.gallery_abstract_edition, language),
                color = Color.Black.copy(alpha = 0.4f),
                fontSize = (12f * scale).coerceIn(9f, 14f).sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (4f * scale).coerceIn(2f, 5f).sp
            )
            Text(
                text = localizedStringResource(R.string.gallery_abstract_caption, language),
                color = Color.Black,
                fontSize = (18f * scale).coerceIn(12f, 22f).sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun AbstractGeometricClockStylePreview() = ClockStylePreviewFrame { modifier ->
    AbstractGeometricClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
