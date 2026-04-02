package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun PixelPetClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(24.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f).clip(RoundedCornerShape(40.dp)).background(Color.Black.copy(alpha = 0.08f)).padding(24.dp)) {
            Text(text = "${parts.hours}:${parts.minutes}", style = TextStyle(fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Black, fontSize = 72.sp), color = Color(0xFF101010))
            Text(text = localizedStringResource(R.string.gallery_pixel_pet_health, language), modifier = Modifier.padding(top = 8.dp), style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 18.sp, letterSpacing = 2.sp), color = Color(0xFF101010).copy(alpha = 0.55f))
        }
        Box(modifier = Modifier.size(240.dp).clip(RoundedCornerShape(36.dp)).background(Color.White).padding(16.dp), contentAlignment = Alignment.Center) {
            Text(text = "\uD83E\uDD96", fontSize = 108.sp)
        }
    }
}
