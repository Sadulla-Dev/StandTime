package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
@Composable
fun RetroFlipClockStyle(
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(Color(0xFFE5E7EB), Color(0xFFD4D4D8)))),
        horizontalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterHorizontally),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FlipBlock(parts.hours)
        FlipBlock(parts.minutes)
    }
}

@Composable
private fun FlipBlock(text: String) {
    AnimatedContent(
        targetState = text,
        transitionSpec = {
            (fadeIn(animationSpec = tween(300, delayMillis = 100)) +
                    slideInVertically { it / 2 } +
                    expandVertically(expandFrom = Alignment.Top)
                    ).togetherWith(
                    fadeOut(animationSpec = tween(300)) +
                            slideOutVertically { -it / 2 } +
                            shrinkVertically(shrinkTowards = Alignment.Bottom)
                )
        },
        label = "FlipAnimation"
    ) { animatedText ->
        Box(
            modifier = Modifier
                .size(width = 220.dp, height = 280.dp)
                .graphicsLayer {
                    cameraDistance = 12f * density
                }
                .clip(RoundedCornerShape(30.dp))
                .background(Color(0xFF1F2937)),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawLine(
                    color = Color.Black.copy(alpha = 0.6f),
                    start = Offset(0f, size.height / 2),
                    end = Offset(size.width, size.height / 2),
                    strokeWidth = 4.dp.toPx()
                )
            }

            Text(
                text = animatedText,
                style = TextStyle(
                    color = Color.White,
                    fontSize = 180.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = 4.sp
                )
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun RetroFlipClockStylePreview() = ClockStylePreviewFrame { modifier ->
    RetroFlipClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
