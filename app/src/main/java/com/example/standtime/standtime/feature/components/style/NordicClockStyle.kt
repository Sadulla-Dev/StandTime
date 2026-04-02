package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage

@Composable
fun NordicClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Brush.linearGradient(listOf(Color(0xFFEAEAEA), Color(0xFFDADADA))))
            .padding(24.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(parts.hours, color = Color(0xFF18181B), fontSize = 152.sp, fontWeight = FontWeight.Medium)
        Box(modifier = Modifier.width(1.dp).height(220.dp).background(Color(0xFFD4D4D8)))
        Text(parts.minutes, color = Color(0xFF52525B), fontSize = 152.sp, fontWeight = FontWeight.Light)
        Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(18.dp)) {
            Box(modifier = Modifier.width(64.dp).height(64.dp).clip(CircleShape).background(Color.Transparent))
            Box(modifier = Modifier.width(64.dp).height(64.dp).clip(CircleShape).background(Color(0xFF18181B)), contentAlignment = Alignment.Center) {
                Text(parts.weatherTemperature.ifBlank { "21°" }, color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
