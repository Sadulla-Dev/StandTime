package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource

@Composable
fun PhotoFrameClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFF1E3A8A), Color(0xFF0F766E), Color(0xFF14532D)))))
        Column(modifier = Modifier.align(Alignment.BottomStart).padding(36.dp)) {
            Text("${parts.hours}:${parts.minutes}", color = Color.White, fontSize = 92.sp, fontWeight = FontWeight.Light)
            Text(
                parts.locationName.ifBlank { localizedStringResource(R.string.gallery_location_tashkent, language) },
                modifier = Modifier.padding(top = 8.dp),
                color = Color.White.copy(alpha = 0.82f),
                fontSize = 28.sp
            )
        }
    }
}
