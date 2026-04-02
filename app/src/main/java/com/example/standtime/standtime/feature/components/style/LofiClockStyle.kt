package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.standtime.R
import com.example.standtime.standtime.feature.components.GalleryClockParts
import com.example.standtime.standtime.feature.utils.StandTimeLanguage
import com.example.standtime.standtime.feature.utils.localizedStringResource

@Composable
fun LofiClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize().background(Brush.linearGradient(listOf(Color(0xFF1A1C2C), Color(0xFF0F172A)))))
        Box(modifier = Modifier.align(Alignment.Center).clip(RoundedCornerShape(48.dp)).background(Color.Black.copy(alpha = 0.24f)).padding(horizontal = 48.dp, vertical = 36.dp)) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(text = "${parts.hours}:${parts.minutes}", style = TextStyle(fontWeight = FontWeight.ExtraLight, fontSize = 92.sp, letterSpacing = 6.sp), color = Color(0xFFC7D2FE))
                Text(text = localizedStringResource(R.string.gallery_lofi_station, language), modifier = Modifier.padding(top = 10.dp), style = TextStyle(fontStyle = FontStyle.Italic, fontSize = 20.sp), color = Color.White.copy(alpha = 0.4f))
            }
        }
    }
}
