package com.standtime.clock.standtime.feature.components.style

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import kotlin.random.Random

@Composable
fun GlitchClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "glitch")
    val t by transition.animateFloat(
        initialValue = 0f,
        targetValue = 100f,
        animationSpec = infiniteRepeatable(tween(2000, easing = LinearEasing)),
        label = "t"
    )

    // Glitch fires every ~40 frames
    val isGlitch = (t.toInt() % 40) < 6 && Random.nextFloat() > 0.4f
    val rgbShift = if (isGlitch) Random.nextFloat() * 8f + 2f else 0f

    Box(
        modifier = modifier.background(Color(0xFF050000)),
        contentAlignment = Alignment.Center
    ) {
        // Glitch color bands
        if (isGlitch) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                repeat(4) {
                    val bandY = Random.nextFloat() * size.height
                    val bandH = Random.nextFloat() * 14f + 2f
                    val shift = (Random.nextFloat() - 0.5f) * 30f
                    val bandColor = if (Random.nextBoolean())
                        Color(0xFFFF0000).copy(alpha = 0.3f)
                    else
                        Color(0xFF00FFFF).copy(alpha = 0.3f)
                    drawRect(
                        color = bandColor,
                        topLeft = Offset(shift, bandY),
                        size = androidx.compose.ui.geometry.Size(size.width, bandH)
                    )
                }
            }
        }

        // RGB split red
        if (rgbShift > 0f) {
            Text(
                text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
                style = TextStyle(
                    color = Color(0xFFFF0000).copy(alpha = 0.55f),
                    fontSize = 122.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                ),
                modifier = Modifier.offset(x = androidx.compose.ui.unit.Dp(rgbShift / 3f))
            )
            // RGB split cyan
            Text(
                text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
                style = TextStyle(
                    color = Color(0xFF00FFFF).copy(alpha = 0.55f),
                    fontSize = 122.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace
                ),
                modifier = Modifier.offset(x = androidx.compose.ui.unit.Dp(-rgbShift / 3f))
            )
        }

        // Main time
        Text(
            text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
            style = TextStyle(
                color = Color(0xFFFF4466),
                fontSize = 122.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                shadow = Shadow(color = Color(0xFFFF0044), blurRadius = 12f)
            )
        )

        // Scanlines
        Canvas(modifier = Modifier.fillMaxSize()) {
            var y = 0f
            while (y < size.height) {
                drawLine(
                    color = Color.Black.copy(alpha = 0.15f),
                    start = Offset(0f, y),
                    end = Offset(size.width, y),
                    strokeWidth = 1f
                )
                y += 3f
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF050000, widthDp = 800, heightDp = 360)
@Composable
private fun GlitchClockStylePreview() = ClockStylePreviewFrame { modifier ->
    GlitchClockStyle(
        ClockStylePreviewParts,
        StandTimeLanguage.ENGLISH,
        ClockStylePreviewAccent,
        modifier
    )
}
