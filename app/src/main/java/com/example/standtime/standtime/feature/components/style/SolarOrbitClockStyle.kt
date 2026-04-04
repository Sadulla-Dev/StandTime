package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource


@Composable
fun SolarOrbitClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val second = parts.seconds.toFloatOrNull() ?: 0f

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(Color(0xFF020617), Color(0xFF111827)))),
        contentAlignment = Alignment.Center
    ) {
        // 1. Orbitalar (Chiziqlar) - Bular harakatlanmaydi, faqat fon bo'lib xizmat qiladi
        Canvas(modifier = Modifier.fillMaxSize()) {
            // Tashqi orbita chizig'i (420.dp)
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 210.dp.toPx(),
                style = Stroke(
                    width = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                )
            )
            // Ichki orbita chizig'i (280.dp)
            drawCircle(
                color = Color.White.copy(alpha = 0.1f),
                radius = 140.dp.toPx(),
                style = Stroke(width = 1.dp.toPx())
            )
        }

        // 2. Tashqi orbita sayyorasi (To'q sariq)
        Box(
            modifier = Modifier
                .size(445.dp)
                .graphicsLayer { rotationZ = second * 6f } // Sekundiga qarab aylanadi
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFF97316))
                    // Sayyoraga yengil nur berish
                    .shadow(elevation = 10.dp, shape = CircleShape, ambientColor = Color(0xFFF97316), spotColor = Color(0xFFF97316))
            )
        }

        // 3. Ichki orbita sayyorasi (Moviy)
        Box(
            modifier = Modifier
                .size(290.dp)
                .graphicsLayer { rotationZ = second * 12f }
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .size(14.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF38BDF8))
                    .shadow(elevation = 8.dp, shape = CircleShape, ambientColor = Color(0xFF38BDF8), spotColor = Color(0xFF38BDF8))
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${parts.hours}:${parts.minutes}",
                style = TextStyle(
                    color = Color.White,
                    fontSize = 92.sp,
                    fontWeight = FontWeight.Light,
                    letterSpacing = 10.sp
                )
            )
            Text(
                text = localizedStringResource(R.string.gallery_orbit_cycle, language, parts.seconds),
                modifier = Modifier.padding(top = 12.dp),
                style = TextStyle(
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 18.sp,
                    letterSpacing = 4.sp
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun SolarOrbitClockStylePreview() = ClockStylePreviewFrame { modifier ->
    SolarOrbitClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
