package com.example.standtime.standtime.feature.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.standtime.standtime.feature.components.style.AbstractGeometricClockStyle
import com.example.standtime.standtime.feature.components.style.AdminPanelClockStyle
import com.example.standtime.standtime.feature.components.style.AnalogZenClockStyle
import com.example.standtime.standtime.feature.components.style.ArabicMajlisClockStyle
import com.example.standtime.standtime.feature.components.style.ArchitectStudioClockStyle
import com.example.standtime.standtime.feature.components.style.AuraPulseClockStyle
import com.example.standtime.standtime.feature.components.style.BauhausClockStyle
import com.example.standtime.standtime.feature.components.style.BinaryPulseClockStyle
import com.example.standtime.standtime.feature.components.style.BraunClockStyle
import com.example.standtime.standtime.feature.components.style.CoffeeClockStyle
import com.example.standtime.standtime.feature.components.style.CustomClockStyle
import com.example.standtime.standtime.feature.components.style.CyberGlitchClockStyle
import com.example.standtime.standtime.feature.components.style.CyberpunkClockStyle
import com.example.standtime.standtime.feature.components.style.FrostedStudioClockStyle
import com.example.standtime.standtime.feature.components.style.GlassClockStyle
import com.example.standtime.standtime.feature.components.style.HorizonStudioClockStyle
import com.example.standtime.standtime.feature.components.style.IndustrialClockStyle
import com.example.standtime.standtime.feature.components.style.LofiClockStyle
import com.example.standtime.standtime.feature.components.style.LuxuryClockStyle
import com.example.standtime.standtime.feature.components.style.MacOsClockStyle
import com.example.standtime.standtime.feature.components.style.NasaClockStyle
import com.example.standtime.standtime.feature.components.style.NothingDotClockStyle
import com.example.standtime.standtime.feature.components.style.NothingOfficialClockStyle
import com.example.standtime.standtime.feature.components.style.OledStealthClockStyle
import com.example.standtime.standtime.feature.components.style.PaperMinimalismClockStyle
import com.example.standtime.standtime.feature.components.style.PixelPetClockStyle
import com.example.standtime.standtime.feature.components.style.PixelStackClockStyle
import com.example.standtime.standtime.feature.components.style.Ps5ClockStyle
import com.example.standtime.standtime.feature.components.style.RetroFlipClockStyle
import com.example.standtime.standtime.feature.components.style.RolexClockStyle
import com.example.standtime.standtime.feature.components.style.SolarOrbitClockStyle
import com.example.standtime.standtime.feature.components.style.SwissClockStyle
import com.example.standtime.standtime.feature.components.style.SynthwaveClockStyle
import com.example.standtime.standtime.feature.components.style.TerminalClockStyle
import com.example.standtime.standtime.feature.components.style.TeslaClockStyle
import com.example.standtime.standtime.feature.components.style.ContrastSplitClockStyle
import com.example.standtime.standtime.feature.components.style.TokyoClockStyle
import com.example.standtime.standtime.feature.components.style.TokyoNeonClockStyle
import com.example.standtime.standtime.feature.components.style.TypewriterClockStyle
import com.example.standtime.standtime.feature.components.style.TypographyFocusClockStyle
import com.example.standtime.standtime.feature.components.style.WordsClockStyle
import com.example.standtime.standtime.feature.components.style.ZenArchitectureClockStyle
import com.example.standtime.standtime.feature.utils.SavedCustomClockStyle
import com.example.standtime.standtime.feature.utils.StandTimeLanguage

private val builtinClockStyles =
    listOf<@Composable (GalleryClockParts, StandTimeLanguage, Color, Modifier) -> Unit>(
        ::NothingOfficialClockStyle,     // 01
        ::ContrastSplitClockStyle,      // 02
        ::AuraPulseClockStyle,          // 03
        ::NothingDotClockStyle,         // 04
        ::NasaClockStyle,               // 05
        ::BauhausClockStyle,            // 18
        ::PixelStackClockStyle,         // 06
        ::IndustrialClockStyle,         // 32
        ::WordsClockStyle,              // 20
        ::TokyoClockStyle,              // 07
        ::ArabicMajlisClockStyle,       // 08
        ::RetroFlipClockStyle,          // 22
        ::SwissClockStyle,              // 31
        ::BraunClockStyle,              // 08
        ::TerminalClockStyle,           // 09
        ::CyberpunkClockStyle,          // 10
        ::LofiClockStyle,               // 12
        ::RolexClockStyle,              // 13
        ::AnalogZenClockStyle,          // 14
        ::TeslaClockStyle,              // 15
        ::GlassClockStyle,              // 16
        ::PaperMinimalismClockStyle,    // 35
        ::LuxuryClockStyle,             // 17
        ::MacOsClockStyle,              // 19
        ::CoffeeClockStyle,             // 21
        ::BinaryPulseClockStyle,        // 23
        ::SolarOrbitClockStyle,         // 24
        ::TypewriterClockStyle,         // 25
        ::AdminPanelClockStyle,         // 26
        ::SynthwaveClockStyle,          // 27
        ::ZenArchitectureClockStyle,    // 28
        ::ArchitectStudioClockStyle,    // 29
        ::OledStealthClockStyle,        // 30
        ::FrostedStudioClockStyle,      // 33
        ::TokyoNeonClockStyle,          // 34
        ::PixelPetClockStyle,           // 11
        ::CyberGlitchClockStyle,        // 36
        ::AbstractGeometricClockStyle,  // 37
        ::TypographyFocusClockStyle,    // 38
        ::HorizonStudioClockStyle,      // 39
        ::Ps5ClockStyle,               // 40
    )

@Composable
fun GalleryClockContent(
    index: Int,
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    customStyles: List<SavedCustomClockStyle> = emptyList(),
    modifier: Modifier = Modifier
) {
    ResponsiveGalleryFrame(modifier = modifier) {
        AnimatedGalleryStyle(index = index) {
            if (index < builtinClockStyles.size) {
                builtinClockStyles[index].invoke(
                    parts,
                    language,
                    accentColor,
                    Modifier.fillMaxSize()
                )
            } else {
                customStyles.getOrNull(index - builtinClockStyles.size)?.let { savedStyle ->
                    CustomClockStyle(
                        parts = parts,
                        custom = savedStyle.settings,
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: FrostedStudioClockStyle(
                    parts,
                    language,
                    accentColor,
                    Modifier.fillMaxSize()
                )
            }
        }
    }
}
