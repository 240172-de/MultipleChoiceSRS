package com.example.multiplechoicesrs.ext

fun Boolean.toInt(): Int {
    return if(this) 1 else 0
}