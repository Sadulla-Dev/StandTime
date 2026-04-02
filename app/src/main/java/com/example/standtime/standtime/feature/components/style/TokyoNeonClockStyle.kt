package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource

@Composable
fun TokyoNeonClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Background Glow
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(Color(0xFFD946EF).copy(alpha = 0.05f), Color.Transparent),
                        radius = 1000f
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.Center) {
                // Blur effect
                Text(
                    text = "${parts.hours}:${parts.minutes}",
                    color = Color(0xFFD946EF).copy(alpha = 0.3f),
                    fontSize = 200.sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.blur(24.dp)
                )
                
                // Main Text
                Text(
                    text = "${parts.hours}:${parts.minutes}",
                    style = androidx.compose.ui.text.TextStyle(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.White, Color(0xFFD946EF), Color(0xFFC026D3))
                        ),
                        fontSize = 200.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-8).sp
                    )
                )
            }

            Row(
                modifier = Modifier.padding(top = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(80.dp)
            ) {
                // Left Line
                Box(
                    modifier = Modifier
                        .width(96.dp)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color.Transparent, Color(0xFFD946EF))
                            )
                        )
                )

                Text(
                    text = localizedStringResource(R.string.gallery_tokyo_neon_label, language),
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    letterSpacing = 8.sp
                )

                // Right Line
                Box(
                    modifier = Modifier
                        .width(96.dp)
                        .height(1.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFFD946EF), Color.Transparent)
                            )
                        )
                )
            }
        }
    }
}
