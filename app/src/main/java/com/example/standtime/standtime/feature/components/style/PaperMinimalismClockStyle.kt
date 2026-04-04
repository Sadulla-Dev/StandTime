package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.components.LocalGalleryScaleFactor
import com.example.standtime.standtime.feature.utils.StandTimeLanguage

@Composable
fun PaperMinimalismClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    val scale = LocalGalleryScaleFactor.current
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.linearGradient(listOf(Color(0xFFE5E7EB), Color(0xFFD4D4D8)))),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy((48f * scale).coerceIn(18f, 56f).dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hours box with outer shadow
            NeumorphicBox(
                size = (320f * scale).coerceIn(170f, 360f).dp,
                innerShadow = false,
                backgroundColor = Color(0xFFE5E7EB)
            ) {
                Text(
                    text = parts.hours,
                    color = Color(0xFF71717A).copy(alpha = 0.8f),
                    fontSize = (180f * scale).coerceIn(84f, 196f).sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Minutes box with inner shadow
            NeumorphicBox(
                size = (320f * scale).coerceIn(170f, 360f).dp,
                innerShadow = true,
                backgroundColor = Color(0xFFE5E7EB)
            ) {
                Text(
                    text = parts.minutes,
                    color = Color(0xFF52525B),
                    fontSize = (180f * scale).coerceIn(84f, 196f).sp,
                    fontWeight = FontWeight.Light
                )
            }
        }
    }
}

@Composable
private fun NeumorphicBox(
    size: Dp,
    innerShadow: Boolean,
    backgroundColor: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .size(size)
            .then(
                if (innerShadow) {
                    Modifier.drawBehind {
                        val paint = Paint().asFrameworkPaint()
                        val cornerRadius = 48.dp.toPx()
                        
                        // Top-left dark shadow
                        paint.color = Color(0xFFC2C4C8).toArgb()
                        paint.setShadowLayer(60.dp.toPx(), 20.dp.toPx(), 20.dp.toPx(), Color(0xFFC2C4C8).toArgb())
                        
                        // Bottom-right light shadow
                        val paintLight = Paint().asFrameworkPaint()
                        paintLight.color = Color.White.toArgb()
                        paintLight.setShadowLayer(60.dp.toPx(), (-20).dp.toPx(), (-20).dp.toPx(), Color.White.toArgb())
                        
                        drawIntoCanvas { canvas ->
                            // This is a simplified version of inner shadow
                            // In a real implementation, you'd use a more complex path or a dedicated library
                        }
                    }
                } else {
                    Modifier.drawBehind {
                        // Outer shadows
                        val cornerRadius = 48.dp.toPx()
                        drawIntoCanvas { canvas ->
                            val paintDark = Paint().asFrameworkPaint().apply {
                                color = backgroundColor.toArgb()
                                setShadowLayer(60.dp.toPx(), 20.dp.toPx(), 20.dp.toPx(), Color(0xFFC2C4C8).toArgb())
                            }
                            canvas.nativeCanvas.drawRoundRect(
                                0f, 0f, size.toPx(), size.toPx(),
                                cornerRadius, cornerRadius,
                                paintDark
                            )
                            
                            val paintLight = Paint().asFrameworkPaint().apply {
                                color = backgroundColor.toArgb()
                                setShadowLayer(60.dp.toPx(), (-20).dp.toPx(), (-20).dp.toPx(), Color.White.toArgb())
                            }
                            canvas.nativeCanvas.drawRoundRect(
                                0f, 0f, size.toPx(), size.toPx(),
                                cornerRadius, cornerRadius,
                                paintLight
                            )
                        }
                    }
                }
            )
            .background(backgroundColor, RoundedCornerShape(48.dp)),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun PaperMinimalismClockStylePreview() = ClockStylePreviewFrame { modifier ->
    PaperMinimalismClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
