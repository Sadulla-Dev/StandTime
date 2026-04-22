package com.standtime.clock.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.standtime.clock.standtime.feature.components.ClockHand
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage

@Composable
fun RolexClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    val hour = parts.hours.toIntOrNull() ?: 0
    val minute = parts.minutes.toIntOrNull() ?: 0
    val second = parts.seconds.toIntOrNull() ?: 0
    val hourAngle = (hour % 12) * 30f + minute * 0.5f
    val minuteAngle = minute * 6f
    val secondAngle = second * 6f

    Box(
        modifier = modifier.background(Brush.linearGradient(listOf(Color(0xFF1A1A1A), Color(0xFF050505)))),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.size(360.dp).clip(CircleShape).background(Color(0xFF111111))) {
            repeat(12) { index ->
                Box(
                    modifier = Modifier.align(Alignment.Center).padding(top = 18.dp).size(width = 4.dp, height = 28.dp).background(Color(0xFF5B5B5B)).graphicsLayer {
                        rotationZ = index * 30f
                        translationY = -140f
                    }
                )
            }
            ClockHand(length = 92.dp, width = 6.dp, angle = hourAngle, color = Color.White)
            ClockHand(length = 126.dp, width = 4.dp, angle = minuteAngle, color = Color(0xFFD4D4D8))
            ClockHand(length = 138.dp, width = 2.dp, angle = secondAngle, color = Color(0xFFEF4444))
            Box(modifier = Modifier.align(Alignment.Center).size(12.dp).clip(CircleShape).background(Color.White))
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun RolexClockStylePreview() = ClockStylePreviewFrame { modifier ->
    RolexClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
