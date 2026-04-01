package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.standtime.StandTimeLanguage

@Composable
fun AbstractGeometricClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        // Large dark circle
        Box(
            modifier = Modifier
                .size(480.dp)
                .offset(x = (-220).dp, y = (-160).dp)
                .background(Color(0xFF18181B).copy(alpha = 0.9f), CircleShape)
        )

        // Orange square border
        Box(
            modifier = Modifier
                .size(400.dp)
                .offset(x = 240.dp)
                .rotate(45f)
                .border(24.dp, Color(0xFFF97316).copy(alpha = 0.2f), RectangleShape)
        )

        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = parts.hours,
                color = Color.Black,
                fontSize = 240.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 200.sp,
                modifier = Modifier.offset(x = (-40).dp)
            )
            Text(
                text = parts.minutes,
                color = Color.Black.copy(alpha = 0.6f),
                fontSize = 180.sp,
                fontWeight = FontWeight.Light,
                lineHeight = 160.sp,
                modifier = Modifier.offset(x = 40.dp, y = (-40).dp)
            )
        }

        // Gallery Edition Label
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(48.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "GALLERY EDITION",
                color = Color.Black.copy(alpha = 0.4f),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 4.sp
            )
            Text(
                text = "2026",
                color = Color.Black,
                fontSize = 48.sp,
                fontWeight = FontWeight.Black
            )
        }
    }
}
