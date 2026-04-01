package com.example.standtime.standtime.feature.components.style

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.standtime.R

data class GalleryStyleEntry(
    @StringRes val nameRes: Int,
    val background: Brush,
    val overlayColor: Color,
)

val galleryStyles: List<GalleryStyleEntry> = listOf(
    GalleryStyleEntry(R.string.gallery_style_nothing_official, Brush.linearGradient(listOf(Color.Black, Color(0xFF090909))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_ps5, Brush.verticalGradient(listOf(Color.Black, Color(0xFF071A3C))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_tesla, Brush.linearGradient(listOf(Color(0xFF111111), Color(0xFF050505))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_minecraft, Brush.linearGradient(listOf(Color(0xFF313131), Color(0xFF222222))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_spotify, Brush.linearGradient(listOf(Color(0xFF121212), Color(0xFF050505))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_nasa, Brush.linearGradient(listOf(Color(0xFF0A0A0A), Color.Black)), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_pixel, Brush.linearGradient(listOf(Color.Black, Color(0xFF0B1220))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_tokyo, Brush.linearGradient(listOf(Color(0xFF020617), Color(0xFF111827))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_ios, Brush.linearGradient(listOf(Color(0xFF18181B), Color(0xFF09090B))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_braun, Brush.linearGradient(listOf(Color(0xFFE5E5E5), Color(0xFFCFCFCF))), Color(0xFF18181B).copy(alpha = 0.7f)),
    GalleryStyleEntry(R.string.gallery_style_terminal, Brush.linearGradient(listOf(Color.Black, Color(0xFF031806))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_cyberpunk, Brush.linearGradient(listOf(Color.Black, Color(0xFF2E1065))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_pixel_pet, Brush.linearGradient(listOf(Color(0xFF98FB98), Color(0xFFB7F7B7))), Color(0xFF18181B).copy(alpha = 0.7f)),
    GalleryStyleEntry(R.string.gallery_style_lofi, Brush.linearGradient(listOf(Color(0xFF1A1C2C), Color(0xFF0F172A))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_rolex, Brush.linearGradient(listOf(Color(0xFF1A1A1A), Color(0xFF050505))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_glass, Brush.linearGradient(listOf(Color(0xFF7C3AED), Color(0xFFF97316))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_luxury, Brush.linearGradient(listOf(Color(0xFF14110D), Color(0xFF0C0A09))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_bauhaus, Brush.linearGradient(listOf(Color(0xFFF2EBDC), Color(0xFFE8DFC7))), Color(0xFF18181B).copy(alpha = 0.7f)),
    GalleryStyleEntry(R.string.gallery_style_macos, Brush.linearGradient(listOf(Color(0xFF2563EB), Color(0xFF0F172A))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_words, Brush.linearGradient(listOf(Color(0xFF09090B), Color(0xFF18181B))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_coffee, Brush.linearGradient(listOf(Color(0xFF2C1810), Color(0xFF120B08))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_night_owl, Brush.linearGradient(listOf(Color(0xFF020617), Color(0xFF1E1B4B))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_arcade, Brush.linearGradient(listOf(Color.Black, Color(0xFF1F2937))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_analog, Brush.linearGradient(listOf(Color(0xFFF8FAFC), Color(0xFFE5E7EB))), Color(0xFF18181B).copy(alpha = 0.7f)),
    GalleryStyleEntry(R.string.gallery_style_flip, Brush.linearGradient(listOf(Color(0xFFE5E7EB), Color(0xFFD4D4D8))), Color(0xFF18181B).copy(alpha = 0.7f)),
    GalleryStyleEntry(R.string.gallery_style_binary, Brush.linearGradient(listOf(Color.Black, Color(0xFF052E16))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_solar, Brush.linearGradient(listOf(Color(0xFF020617), Color(0xFF111827))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_typewriter, Brush.linearGradient(listOf(Color(0xFFF5F1E8), Color(0xFFE7E0D1))), Color(0xFF18181B).copy(alpha = 0.7f)),
    GalleryStyleEntry(R.string.gallery_style_liquid, Brush.linearGradient(listOf(Color(0xFF4F46E5), Color(0xFFEC4899), Color(0xFFF59E0B))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_admin, Brush.linearGradient(listOf(Color.Black, Color(0xFF111827))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_frame, Brush.linearGradient(listOf(Color(0xFF0F172A), Color(0xFF134E4A), Color(0xFF365314))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_synthwave, Brush.linearGradient(listOf(Color(0xFF120422), Color(0xFF312E81))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_zen, Brush.linearGradient(listOf(Color(0xFF0F0F0F), Color(0xFF171717))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_architect, Brush.linearGradient(listOf(Color(0xFFF5F5F5), Color(0xFFEAEAEA))), Color(0xFF18181B).copy(alpha = 0.7f)),
    GalleryStyleEntry(R.string.gallery_style_oled, Brush.linearGradient(listOf(Color.Black, Color(0xFF040404))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_nordic, Brush.linearGradient(listOf(Color(0xFFEAEAEA), Color(0xFFDADADA))), Color(0xFF18181B).copy(alpha = 0.7f)),
    GalleryStyleEntry(R.string.gallery_style_swiss, Brush.linearGradient(listOf(Color(0xFFE63946), Color(0xFFB91C1C))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_industrial, Brush.linearGradient(listOf(Color(0xFF1C1C1C), Color(0xFF090909))), Color.White.copy(alpha = 0.6f)),
    GalleryStyleEntry(R.string.gallery_style_frosted, Brush.linearGradient(listOf(Color(0xFF3F3F46), Color.Black)), Color.White.copy(alpha = 0.6f)),
//    GalleryStyleEntry(R.string.gallery_style_tokyo_neon, Brush.linearGradient(listOf(Color(0xFF020205), Color.Black)), Color.White.copy(alpha = 0.6f)),
//    GalleryStyleEntry(R.string.gallery_style_paper, Brush.linearGradient(listOf(Color(0xFFE5E7EB), Color(0xFFD4D4D8))), Color(0xFF18181B).copy(alpha = 0.7f)),
//    GalleryStyleEntry(R.string.gallery_style_glitch, Brush.linearGradient(listOf(Color(0xFF050505), Color.Black)), Color.White.copy(alpha = 0.6f)),
//    GalleryStyleEntry(R.string.gallery_style_abstract, Brush.linearGradient(listOf(Color(0xFFF9FAFB), Color(0xFFF3F4F6))), Color(0xFF18181B).copy(alpha = 0.7f)),
//    GalleryStyleEntry(R.string.gallery_style_typography, Brush.linearGradient(listOf(Color.White, Color(0xFFF9FAFB))), Color(0xFF18181B).copy(alpha = 0.7f)),
//    GalleryStyleEntry(R.string.gallery_style_nothing_dot, Brush.linearGradient(listOf(Color.Black, Color(0xFF090909))), Color.White.copy(alpha = 0.6f)),
)

val galleryStyleCount: Int
    get() = galleryStyles.size

fun galleryStyleAt(index: Int): GalleryStyleEntry = galleryStyles[index]
