package com.example.multiplechoicesrs.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val deckId: Int,
    val categoryId: Int,
    val name: String
)
