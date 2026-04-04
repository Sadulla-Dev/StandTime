package com.example.standtime.standtime.feature.components.style

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun TypewriterClockStyle(parts: GalleryClockParts, language: StandTimeLanguage, accentColor: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.background(Brush.linearGradient(listOf(Color(0xFFF5F1E8), Color(0xFFE7E0D1)))),
        verticalArrangement = androidx.compose.foundation.layout.Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(localizedStringResource(R.string.gallery_current_moment, language), color = Color(0xFF78716C).copy(alpha = 0.5f), fontSize = 28.sp, fontStyle = FontStyle.Italic)
        Text(text = "${parts.hours}:${parts.minutes}", modifier = Modifier.padding(top = 10.dp), color = Color(0xFF44403C), fontSize = 218.sp, fontStyle = FontStyle.Italic, fontWeight = FontWeight.Bold)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF101418, widthDp = 800, heightDp = 360)
@Composable
private fun TypewriterClockStylePreview() = ClockStylePreviewFrame { modifier ->
    TypewriterClockStyle(ClockStylePreviewParts, StandTimeLanguage.ENGLISH, ClockStylePreviewAccent, modifier)
}
