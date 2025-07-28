package com.example.homehub.viewmodels

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject

@HiltViewModel
class LogsScreenViewModel @Inject constructor() : ViewModel() {
    // Add log-related state and actions here as needed
    // For example, you could expose log reading or clearing methods if implemented in Logs
}

