package com.example.multiplechoicesrs.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Deck(
    @SerialName("deck_id")
    val deckId: Int,
    val name: String,

    @SerialName("version_id")
    val versionId: Int,
    var categories: List<Category>? = null,
    var questions: List<Question>? = null,

    @SerialName("questions_json")
    var questionsJson: List<QuestionJson>? = null
)