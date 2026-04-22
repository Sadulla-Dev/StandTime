package com.standtime.clock.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.standtime.clock.R
import com.standtime.clock.standtime.feature.components.GalleryClockParts
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage
import com.standtime.clock.standtime.feature.utils.localizedStringResource

@Composable
fun CyberGlitchClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.linearGradient(
                    listOf(
                        Color(0xFF1649E2),
                        Color(0xFF0A1020),
                        Color(0x0F37F30D)
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        // Horizontal glitch line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFF22D3EE).copy(alpha = 0.2f))
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Red offset
                Text(
                    text = "${parts.hours}${parts.minutes}",
                    color = Color.Red.copy(alpha = 0.4f),
                    fontSize = 240.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = (-12).sp,
                    modifier = Modifier.offset(x = 6.dp, y = 6.dp)
                )
                
                // Blue offset
                Text(
                    text = "${parts.hours}${parts.minutes}",
                    color = Color.Blue.copy(alpha = 0.4f),
                    fontSize = 240.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = (-12).sp,
                    modifier = Modifier.offset(x = (-6).dp, y = (-6).dp)
                )

                // Main Text
                Text(
                    text = "${parts.hours}${parts.minutes}",
                    color = Color(0xFF22D3EE),
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color(0xFF22D3EE).copy(alpha = 0.55f),
                            offset = Offset(0f, 0f),
                            blurRadius = 28f
                        )
                    ),
                    fontSize = 240.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = (-12).sp
                )
            }

            Text(
                text = localizedStringResource(R.string.gallery_glitch_override, language),
                color = Color.White.copy(alpha = 0.3f),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 16.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun CyberGlitchClockStylePreview() = ClockStylePreviewFrame { modifier ->
    CyberGlitchClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
