package com.example.multiplechoicesrs.model

import androidx.compose.ui.graphics.Color
import com.example.multiplechoicesrs.ui.theme.GreenCorrectAnswer
import com.example.multiplechoicesrs.ui.theme.LightBlue
import com.example.multiplechoicesrs.ui.theme.MutedBlack
import com.example.multiplechoicesrs.ui.theme.MutedWhite
import com.example.multiplechoicesrs.ui.theme.RedIncorrectAnswer
import com.example.multiplechoicesrs.ui.theme.YellowWaring

enum class SnackbarColor(
    val backgroundColor: Color,
    val foregroundColor: Color
) {
    SUCCESS(
        backgroundColor = GreenCorrectAnswer,
        foregroundColor = MutedBlack
    ),
    FAILURE(
        backgroundColor = RedIncorrectAnswer,
        foregroundColor = MutedWhite
    ),
    WARNING(
        backgroundColor = YellowWaring,
        foregroundColor = MutedBlack
    ),
    INFO(
        backgroundColor = LightBlue,
        foregroundColor = MutedBlack
    )
}