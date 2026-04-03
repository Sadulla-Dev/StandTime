package com.example.standtime.standtime.feature.components.style

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
fun Ps5ClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Box(modifier = modifier.background(Brush.verticalGradient(listOf(Color.Black, Color(0xFF071A3C))))) {
        Box(modifier = Modifier.padding( 40.dp).size(480.dp).align(Alignment.TopCenter).clip(CircleShape).background(Color(0x332567FF)))
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "${parts.hours} | ${parts.minutes}", style = TextStyle(fontWeight = FontWeight.Light, fontSize = 100.sp, letterSpacing = 6.sp), color = Color.White)
            Text(
                text = localizedStringResource(R.string.gallery_ps5_symbols, language),
                modifier = Modifier.padding(top = 20.dp),
                style = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 24.sp, letterSpacing = 6.sp),
                color = Color.White.copy(alpha = 0.45f)
            )
        }
    }
}
