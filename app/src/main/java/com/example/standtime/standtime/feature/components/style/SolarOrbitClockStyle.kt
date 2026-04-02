package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource

@Composable
fun SolarOrbitClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    val second = parts.seconds.toFloatOrNull() ?: 0f
    Box(
        modifier = modifier.background(Brush.linearGradient(listOf(Color(0xFF020617), Color(0xFF111827)))),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.size(420.dp).clip(CircleShape).background(Color.Transparent).graphicsLayer { rotationZ = second * 6f }) {
            Box(modifier = Modifier.align(Alignment.TopCenter).size(18.dp).clip(CircleShape).background(Color(0xFFF97316)))
        }
        Box(modifier = Modifier.size(280.dp).clip(CircleShape).background(Color.Transparent).graphicsLayer { rotationZ = second * 12f }) {
            Box(modifier = Modifier.align(Alignment.TopCenter).size(14.dp).clip(CircleShape).background(Color(0xFF38BDF8)))
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("${parts.hours}:${parts.minutes}", color = Color.White, fontSize = 92.sp, fontWeight = FontWeight.Light, letterSpacing = 10.sp)
            Text(localizedStringResource(R.string.gallery_orbit_cycle, language, parts.seconds), modifier = Modifier.padding(top = 12.dp), color = Color.White.copy(alpha = 0.4f), fontSize = 18.sp, letterSpacing = 4.sp)
        }
    }
}
