package com.example.standtime.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.standtime.R

object StandTimeFontFamilies {
    val Inter = FontFamily(
        Font(R.font.inter_variable, FontWeight.Light),
        Font(R.font.inter_variable, FontWeight.Normal),
        Font(R.font.inter_variable, FontWeight.Medium),
        Font(R.font.inter_variable, FontWeight.SemiBold),
        Font(R.font.inter_variable, FontWeight.Bold),
        Font(R.font.inter_variable, FontWeight.Black)
    )

    val Poppins = FontFamily(
        Font(R.font.poppins_regular, FontWeight.Normal),
        Font(R.font.poppins_medium, FontWeight.Medium),
        Font(R.font.poppins_semibold, FontWeight.SemiBold),
        Font(R.font.poppins_bold, FontWeight.Bold)
    )

    val Oswald = FontFamily(
        Font(R.font.oswald_variable, FontWeight.Normal),
        Font(R.font.oswald_variable, FontWeight.Medium),
        Font(R.font.oswald_variable, FontWeight.SemiBold),
        Font(R.font.oswald_variable, FontWeight.Bold)
    )

    val Nunito = FontFamily(
        Font(R.font.nunito_variable, FontWeight.Normal),
        Font(R.font.nunito_variable, FontWeight.Medium),
        Font(R.font.nunito_variable, FontWeight.Bold)
    )

    val PlayfairDisplay = FontFamily(
        Font(R.font.playfair_display_variable, FontWeight.Normal),
        Font(R.font.playfair_display_variable, FontWeight.Medium),
        Font(R.font.playfair_display_variable, FontWeight.Bold)
    )

    val Caveat = FontFamily(
        Font(R.font.caveat_variable, FontWeight.Normal),
        Font(R.font.caveat_variable, FontWeight.Medium),
        Font(R.font.caveat_variable, FontWeight.Bold)
    )

    val PressStart2P = FontFamily(
        Font(R.font.press_start_2p_regular, FontWeight.Normal)
    )
}
