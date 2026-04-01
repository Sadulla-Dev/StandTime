package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.standtime.StandTimeLanguage

@Composable
fun NothingDotClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = parts.hours,
                    color = Color.White,
                    fontSize = 240.sp,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = (-12).sp
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(Color(0xFFDC2626), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(Color(0xFF27272A), CircleShape)
                    )
                }

                Text(
                    text = parts.minutes,
                    color = Color.White.copy(alpha = 0.4f),
                    fontSize = 240.sp,
                    fontFamily = FontFamily.Monospace,
                    letterSpacing = (-12).sp
                )
            }

            Column(
                modifier = Modifier.padding(top = 48.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .width(200.dp)
                        .height(1.dp)
                        .background(Color.White.copy(alpha = 0.1f))
                )
                Text(
                    text = "NOTHING DIGITAL",
                    color = Color.White.copy(alpha = 0.2f),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    letterSpacing = 16.sp,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}
