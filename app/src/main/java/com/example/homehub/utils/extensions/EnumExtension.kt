package com.example.homehub.utils.extensions

inline fun <reified T : Enum<T>> String.toEnumOrDefault(default: T): T {
    return try {
        enumValueOf<T>(this.uppercase())
    } catch (e: Exception) {
        default
    }
}