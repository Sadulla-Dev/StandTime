package com.example.standtime.standtime.feature.components.style

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.standtime.StandTimeLanguage

@Composable
fun CyberGlitchClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
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
                    fontSize = 240.sp,
                    fontWeight = FontWeight.Black,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = (-12).sp
                )
            }

            Text(
                text = "SYSTEM_OVERRIDE",
                color = Color.White.copy(alpha = 0.3f),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                letterSpacing = 16.sp,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}
