package com.example.multiplechoicesrs.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    @SerialName("deck_id")
    val deckId: Int,

    @SerialName("category_id")
    val categoryId: Int,
    val name: String
)
