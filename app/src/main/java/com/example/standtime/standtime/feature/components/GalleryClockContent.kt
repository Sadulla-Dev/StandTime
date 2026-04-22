package com.standtime.clock.standtime.feature.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.standtime.clock.standtime.feature.components.style.AbstractGeometricClockStyle
import com.standtime.clock.standtime.feature.components.style.AdminPanelClockStyle
import com.standtime.clock.standtime.feature.components.style.AnalogZenClockStyle
import com.standtime.clock.standtime.feature.components.style.ArabicMajlisClockStyle
import com.standtime.clock.standtime.feature.components.style.ArcticPulseClockStyle
import com.standtime.clock.standtime.feature.components.style.ArchitectStudioClockStyle
import com.standtime.clock.standtime.feature.components.style.AuraPulseClockStyle
import com.standtime.clock.standtime.feature.components.style.AuroraClockStyle
import com.standtime.clock.standtime.feature.components.style.BauhausClockStyle
import com.standtime.clock.standtime.feature.components.style.BinaryPulseClockStyle
import com.standtime.clock.standtime.feature.components.style.BioluminescentClockStyle
import com.standtime.clock.standtime.feature.components.style.BraunClockStyle
import com.standtime.clock.standtime.feature.components.style.ClockworkClockStyle
import com.standtime.clock.standtime.feature.components.style.CoffeeClockStyle
import com.standtime.clock.standtime.feature.components.style.CustomClockStyle
import com.standtime.clock.standtime.feature.components.style.CyberGlitchClockStyle
import com.standtime.clock.standtime.feature.components.style.DeepSpaceClockStyle
import com.standtime.clock.standtime.feature.components.style.DesertStormClockStyle
import com.standtime.clock.standtime.feature.components.style.CyberpunkClockStyle
import com.standtime.clock.standtime.feature.components.style.FrostedStudioClockStyle
import com.standtime.clock.standtime.feature.components.style.GlassClockStyle
import com.standtime.clock.standtime.feature.components.style.GoldLuxuryClockStyle
import com.standtime.clock.standtime.feature.components.style.IceCrystalClockStyle
import com.standtime.clock.standtime.feature.components.style.IndustrialClockStyle
import com.standtime.clock.standtime.feature.components.style.LavaLampClockStyle
import com.standtime.clock.standtime.feature.components.style.LofiClockStyle
import com.standtime.clock.standtime.feature.components.style.LuxuryClockStyle
import com.standtime.clock.standtime.feature.components.style.MacOsClockStyle
import com.standtime.clock.standtime.feature.components.style.NasaClockStyle
import com.standtime.clock.standtime.feature.components.style.NeuralNetClockStyle
import com.standtime.clock.standtime.feature.components.style.NothingDotClockStyle
import com.standtime.clock.standtime.feature.components.style.NothingOfficialClockStyle
import com.standtime.clock.standtime.feature.components.style.OledStealthClockStyle
import com.standtime.clock.standtime.feature.components.style.PaperMinimalismClockStyle
import com.standtime.clock.standtime.feature.components.style.PixelPetClockStyle
import com.standtime.clock.standtime.feature.components.style.PixelStackClockStyle
import com.standtime.clock.standtime.feature.components.style.Ps5ClockStyle
import com.standtime.clock.standtime.feature.components.style.RetroFlipClockStyle
import com.standtime.clock.standtime.feature.components.style.RolexClockStyle
import com.standtime.clock.standtime.feature.components.style.SolarOrbitClockStyle
import com.standtime.clock.standtime.feature.components.style.SwissClockStyle
import com.standtime.clock.standtime.feature.components.style.SynthwaveClockStyle
import com.standtime.clock.standtime.feature.components.style.TeslaClockStyle
import com.standtime.clock.standtime.feature.components.style.ContrastSplitClockStyle
import com.standtime.clock.standtime.feature.components.style.CyberGridClockStyle
import com.standtime.clock.standtime.feature.components.style.DarkSoulClockStyle
import com.standtime.clock.standtime.feature.components.style.GlitchClockStyle
import com.standtime.clock.standtime.feature.components.style.HologramClockStyle
import com.standtime.clock.standtime.feature.components.style.QuantumClockStyle
import com.standtime.clock.standtime.feature.components.style.RetroTerminalClockStyle
import com.standtime.clock.standtime.feature.components.style.SakuraClockStyle
import com.standtime.clock.standtime.feature.components.style.TokyoClockStyle
import com.standtime.clock.standtime.feature.components.style.TokyoNeonClockStyle
import com.standtime.clock.standtime.feature.components.style.TypewriterClockStyle
import com.standtime.clock.standtime.feature.components.style.TypographyFocusClockStyle
import com.standtime.clock.standtime.feature.components.style.VaporWaveClockStyle
import com.standtime.clock.standtime.feature.components.style.WordsClockStyle
import com.standtime.clock.standtime.feature.components.style.ZenArchitectureClockStyle
import com.standtime.clock.standtime.feature.utils.SavedCustomClockStyle
import com.standtime.clock.standtime.feature.utils.StandTimeLanguage

private val builtinClockStyles =
    listOf<@Composable (GalleryClockParts, StandTimeLanguage, Color, Modifier) -> Unit>(
        ::NothingOfficialClockStyle,     // 01
        ::ContrastSplitClockStyle,       // 02
        ::AuraPulseClockStyle,           // 03
        ::LavaLampClockStyle,            // 04
        ::QuantumClockStyle,             // 05
        ::NothingDotClockStyle,          // 06
        ::NasaClockStyle,                // 07
        ::IceCrystalClockStyle,          // 08
        ::DeepSpaceClockStyle,           // 09
        ::BauhausClockStyle,             // 10
        ::PixelStackClockStyle,          // 11
        ::IndustrialClockStyle,          // 12
        ::GlitchClockStyle,              // 13
        ::WordsClockStyle,               // 14
        ::TokyoClockStyle,               // 15
        ::SakuraClockStyle,              // 16
        ::ArabicMajlisClockStyle,        // 17
        ::RetroFlipClockStyle,           // 18
        ::VaporWaveClockStyle,           // 19
        ::SwissClockStyle,               // 20
        ::BraunClockStyle,               // 21
        ::ClockworkClockStyle,           // 22
        ::CyberpunkClockStyle,           // 23
        ::NeuralNetClockStyle,           // 24
        ::LofiClockStyle,                // 25
        ::RolexClockStyle,               // 26
        ::AuroraClockStyle,              // 27
        ::BioluminescentClockStyle,      // 28
        ::AnalogZenClockStyle,           // 29
        ::TeslaClockStyle,               // 30
        ::GlassClockStyle,               // 31
        ::PaperMinimalismClockStyle,     // 32
        ::LuxuryClockStyle,              // 33
        ::GoldLuxuryClockStyle,          // 34
        ::MacOsClockStyle,               // 35
        ::CyberGridClockStyle,           // 36
        ::CoffeeClockStyle,              // 37
        ::DesertStormClockStyle,         // 38
        ::BinaryPulseClockStyle,         // 39
        ::SolarOrbitClockStyle,          // 40
        ::ArcticPulseClockStyle,         // 41
        ::TypewriterClockStyle,          // 42
        ::AdminPanelClockStyle,          // 43
        ::SynthwaveClockStyle,           // 44
        ::ZenArchitectureClockStyle,     // 45
        ::RetroTerminalClockStyle,       // 46
        ::ArchitectStudioClockStyle,     // 47
        ::OledStealthClockStyle,         // 48
        ::FrostedStudioClockStyle,       // 49
        ::TokyoNeonClockStyle,           // 50
        ::PixelPetClockStyle,            // 51
        ::CyberGlitchClockStyle,         // 52
        ::HologramClockStyle,            // 53
        ::AbstractGeometricClockStyle,   // 54
        ::TypographyFocusClockStyle,     // 55
        ::Ps5ClockStyle,                 // 56
        ::DarkSoulClockStyle,            // 57
    )

@Composable
fun GalleryClockContent(
    index: Int,
    parts: GalleryClockParts,
    language: StandTimeLanguage,
    accentColor: Color,
    customStyles: List<SavedCustomClockStyle> = emptyList(),
    burnInProtectionEnabled: Boolean = false,
    modifier: Modifier = Modifier
) {
    ResponsiveGalleryFrame(modifier = modifier) {
        AnimatedGalleryStyle(index = index, burnInProtectionEnabled = burnInProtectionEnabled) {
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
