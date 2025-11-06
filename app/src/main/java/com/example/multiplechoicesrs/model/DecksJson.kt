package com.example.multiplechoicesrs.model

import kotlinx.serialization.Serializable

@Serializable
data class DecksJson (
    var status : String,
    var count : Int,
    var data : List<Deck>
)