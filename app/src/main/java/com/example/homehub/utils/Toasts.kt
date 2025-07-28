package com.example.homehub.utils

import android.content.Context
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import jakarta.inject.Inject

class Toasts @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun show(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}