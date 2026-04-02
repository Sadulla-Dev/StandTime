package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource

@Composable
fun NasaClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Brush.linearGradient(listOf(Color(0xFF0A0A0A), Color.Black)))
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0x14000000))
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)) {
            Text(localizedStringResource(R.string.gallery_nasa_latitude, language), color = Color(0xFFFF8A3D), fontSize = 12.sp)
            Text("41.2995° N", color = Color(0xFFFF8A3D), fontSize = 30.sp, fontFamily = FontFamily.Monospace)
            Text(localizedStringResource(R.string.gallery_nasa_longitude, language), color = Color(0xFFFF8A3D), fontSize = 12.sp)
            Text("69.2401° E", color = Color(0xFFFF8A3D), fontSize = 30.sp, fontFamily = FontFamily.Monospace)
        }
        Column(modifier = Modifier.weight(1.4f), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "${parts.hours}:${parts.minutes}:${parts.seconds}", style = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold, fontSize = 62.sp), color = Color(0xFFFF8A3D))
            Text(
                text = localizedStringResource(R.string.gallery_nasa_status, language),
                modifier = Modifier.padding(top = 12.dp).background(Color(0xFFFF8A3D)).padding(horizontal = 12.dp, vertical = 6.dp),
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
        Column(modifier = Modifier.weight(1f), horizontalAlignment = Alignment.End, verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(16.dp)) {
            Text(localizedStringResource(R.string.gallery_nasa_oxygen, language), color = Color(0xFFFF8A3D), fontSize = 12.sp)
            Text("98.2%", color = Color(0xFFFF8A3D), fontSize = 30.sp, fontFamily = FontFamily.Monospace)
            Text(localizedStringResource(R.string.gallery_nasa_pressure, language), color = Color(0xFFFF8A3D), fontSize = 12.sp)
            Text("1.0 ATM", color = Color(0xFFFF8A3D), fontSize = 30.sp, fontFamily = FontFamily.Monospace)
        }
    }
}
