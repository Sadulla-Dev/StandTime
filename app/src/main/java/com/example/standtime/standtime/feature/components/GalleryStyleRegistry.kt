package com.example.standtime.standtime.feature.components

import androidx.annotation.StringRes
import com.example.standtime.R

data class GalleryStyleEntry(
    @param:StringRes val nameRes: Int,
)

val galleryStyles: List<GalleryStyleEntry> = listOf(
    GalleryStyleEntry(R.string.gallery_style_nothing_official),
    GalleryStyleEntry(R.string.gallery_style_ps5),
    GalleryStyleEntry(R.string.gallery_style_tesla),
    GalleryStyleEntry(R.string.gallery_style_nasa),
    GalleryStyleEntry(R.string.gallery_style_pixel),
    GalleryStyleEntry(R.string.gallery_style_tokyo),
    GalleryStyleEntry(R.string.gallery_style_braun),
    GalleryStyleEntry(R.string.gallery_style_terminal),
    GalleryStyleEntry(R.string.gallery_style_cyberpunk),
    GalleryStyleEntry(R.string.gallery_style_pixel_pet),
    GalleryStyleEntry(R.string.gallery_style_lofi),
    GalleryStyleEntry(R.string.gallery_style_rolex),
    GalleryStyleEntry(R.string.gallery_style_glass),
    GalleryStyleEntry(R.string.gallery_style_luxury),
    GalleryStyleEntry(R.string.gallery_style_bauhaus),
    GalleryStyleEntry(R.string.gallery_style_macos),
    GalleryStyleEntry(R.string.gallery_style_words),
    GalleryStyleEntry(R.string.gallery_style_coffee),
    GalleryStyleEntry(R.string.gallery_style_night_owl),
    GalleryStyleEntry(R.string.gallery_style_analog),
    GalleryStyleEntry(R.string.gallery_style_flip),
    GalleryStyleEntry(R.string.gallery_style_binary),
    GalleryStyleEntry(R.string.gallery_style_solar),
    GalleryStyleEntry(R.string.gallery_style_typewriter),
    GalleryStyleEntry(R.string.gallery_style_liquid),
    GalleryStyleEntry(R.string.gallery_style_admin),
    GalleryStyleEntry(R.string.gallery_style_synthwave),
    GalleryStyleEntry(R.string.gallery_style_zen),
    GalleryStyleEntry(R.string.gallery_style_architect),
    GalleryStyleEntry(R.string.gallery_style_oled),
    GalleryStyleEntry(R.string.gallery_style_nordic),
    GalleryStyleEntry(R.string.gallery_style_swiss),
    GalleryStyleEntry(R.string.gallery_style_industrial),
    GalleryStyleEntry(R.string.gallery_style_frosted),
    GalleryStyleEntry(R.string.gallery_style_tokyo_neon),
    GalleryStyleEntry(R.string.gallery_style_paper),
    GalleryStyleEntry(R.string.gallery_style_glitch),
    GalleryStyleEntry(R.string.gallery_style_abstract),
    GalleryStyleEntry(R.string.gallery_style_typography),
    GalleryStyleEntry(R.string.gallery_style_nothing_dot),
)

val galleryStyleCount: Int
    get() = galleryStyles.size

fun galleryStyleAt(index: Int): GalleryStyleEntry = galleryStyles[index]
