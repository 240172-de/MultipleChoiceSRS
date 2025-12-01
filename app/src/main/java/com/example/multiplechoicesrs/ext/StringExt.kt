package com.example.multiplechoicesrs.ext

fun String.isNumber(): Boolean {
    return this.all { it.isDigit() }
}