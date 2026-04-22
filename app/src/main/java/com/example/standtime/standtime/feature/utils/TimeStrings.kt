package com.standtime.clock.standtime.feature.utils

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.platform.LocalContext
import java.util.Locale

@Composable
@ReadOnlyComposable
fun localizedStringResource(
    @StringRes id: Int,
    language: StandTimeLanguage
): String {
    val context = LocalContext.current
    return context.localizedContext(language).getString(id)
}

@Composable
@ReadOnlyComposable
fun localizedStringResource(
    @StringRes id: Int,
    language: StandTimeLanguage,
    vararg formatArgs: Any
): String {
    val context = LocalContext.current
    return context.localizedContext(language).getString(id, *formatArgs)
}

private fun Context.localizedContext(language: StandTimeLanguage): Context {
    val locale = when (language) {
        StandTimeLanguage.ENGLISH -> Locale.ENGLISH
        StandTimeLanguage.UZBEK -> Locale.forLanguageTag("uz")
        StandTimeLanguage.RUSSIAN -> Locale.forLanguageTag("ru")
    }
    val configuration = Configuration(resources.configuration)
    configuration.setLocale(locale)
    return createConfigurationContext(configuration)
}
