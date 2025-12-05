package com.example.multiplechoicesrs.ext

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import com.example.multiplechoicesrs.ui.theme.CustomColorPalette
import com.example.multiplechoicesrs.ui.theme.LocalCustomColorPalette

val MaterialTheme.customColorPalette: CustomColorPalette
    @Composable
    @ReadOnlyComposable
    get() = LocalCustomColorPalette.current
