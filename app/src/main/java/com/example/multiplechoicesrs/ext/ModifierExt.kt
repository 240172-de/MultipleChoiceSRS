package com.example.multiplechoicesrs.ext

import androidx.compose.ui.Modifier

fun Modifier.modifyIf(condition: Boolean, modify: Modifier.() -> Modifier): Modifier {
    return if (condition) modify() else this
}
