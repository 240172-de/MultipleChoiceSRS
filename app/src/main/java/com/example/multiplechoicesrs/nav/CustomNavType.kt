package com.example.multiplechoicesrs.nav

import android.net.Uri
import android.os.Bundle
import androidx.navigation.NavType
import kotlinx.serialization.json.Json
import kotlin.reflect.KClass

////TODO: Create CustomNavType for Deck etc.
////https://stackoverflow.com/questions/78858250/type-safe-navigation-custom-list-navtype
////Otherwise error java.lang.IllegalArgumentException: Route com.example.multiplechoicesrs.view.Screen.CategoryListScreen could not find any NavType for argument deck of type com.example.multiplechoicesrs.model.Deck - typeMap received was {}

inline fun <reified T> navTypeOf(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String): T? =
        bundle.getString(key)?.let(json::decodeFromString)

    override fun parseValue(value: String): T = json.decodeFromString(Uri.decode(value))

    override fun serializeAsValue(value: T): String = Uri.encode(json.encodeToString(value))

    override fun put(bundle: Bundle, key: String, value: T) =
        bundle.putString(key, json.encodeToString(value))
}
//
////https://stackoverflow.com/questions/79194126/jetpack-compose-navigation-adding-custom-navtype-results-in-immediate-return-t
//inline fun <reified T : Any> serializableType(
//    isNullableAllowed: Boolean = false,
//    json: Json = Json,
//) = object : CustomNavType<T>(
//    type = T::class,
//    isNullableAllowed = isNullableAllowed,
//) {
//    override fun get(bundle: Bundle, key: String) =
//        bundle.getString(key)?.let<String, T>(json::decodeFromString)
//
//    override fun parseValue(value: String): T = json.decodeFromString(value)
//
//    override fun serializeAsValue(value: T): String = json.encodeToString(value)
//
//    override fun put(bundle: Bundle, key: String, value: T) {
//        bundle.putString(key, json.encodeToString(value))
//    }
//
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (other == null || javaClass != other.javaClass) return false
//        val that = other as CustomNavType<*>
//        return type == that.type
//    }
//}
//
//abstract class CustomNavType<T : Any>(
//    val type: KClass<T>,
//    isNullableAllowed: Boolean = false,
//) : NavType<T>(isNullableAllowed)