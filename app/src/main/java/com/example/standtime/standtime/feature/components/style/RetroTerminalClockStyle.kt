package com.standtime.clock.standtime.feature.components.style

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.sin

@Composable
fun RetroTerminalClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "terminal")
    val blink by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(tween(800, easing = LinearEasing)),
        label = "blink"
    )
    val showCursor = sin(blink) > 0f
    val dateStr = SimpleDateFormat("EEE MMM dd yyyy", Locale.ENGLISH)
        .format(Date()).uppercase()

    Box(
        modifier = modifier.background(Color(0xFF000A00))
    ) {
        // CRT scanline overlay
        Canvas(modifier = Modifier.fillMaxSize()) {
            var y = 0f
            while (y < size.height) {
                drawLine(
                    color = Color.Black.copy(alpha = 0.3f),
                    start = androidx.compose.ui.geometry.Offset(0f, y),
                    end = androidx.compose.ui.geometry.Offset(size.width, y),
                    strokeWidth = 1f
                )
                y += 2f
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 32.dp, top = 34.dp),
            verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(6.dp)
        ) {
            val dimStyle = TextStyle(
                color = Color(0xFF006622),
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace
            )
            Text("> SYSTEM BOOT OK", style = dimStyle)
            Text("> DATE: $dateStr", style = dimStyle)
            Text("> LOADING CLOCK MODULE...", style = dimStyle)

            Text(
                text = "${parts.hours}:${parts.minutes}:${parts.seconds}",
                style = TextStyle(
                    color = Color(0xFF00CC33),
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    shadow = Shadow(color = Color(0xFF00FF44), blurRadius = 10f)
                ),
                modifier = Modifier.padding(top = 4.dp)
            )

            Text(
                text = "> ${if (showCursor) "█" else " "}",
                style = TextStyle(
                    color = Color(0xFF009922),
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000A00, widthDp = 800, heightDp = 360)
@Composable
private fun RetroTerminalClockStylePreview() = ClockStylePreviewFrame { modifier ->
    RetroTerminalClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
