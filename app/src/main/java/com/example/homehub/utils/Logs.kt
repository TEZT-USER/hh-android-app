package com.example.homehub.utils

import android.content.Context
import android.util.Log
import com.example.homehub.constants.Constants.LOG_FILE_NAME
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

object Logs {
    private const val LOGGING_ENABLED: Boolean = false
    private const val MAX_LOG_FILE_SIZE = 500 * 1024 // 500 kB

    private lateinit var appContext: Context

    fun initialize(context: Context) {
        if (!::appContext.isInitialized) {
            appContext = context.applicationContext
        }
    }

    fun d(text: String) {
        log("DEBUG: $text")
    }

    fun e(error: Exception) {
        log("ERROR: ${error.message}")
    }

    private fun log(message: String) {
        if (!LOGGING_ENABLED || !::appContext.isInitialized) return
        logToFile(message)
    }

    private fun logToFile(message: String) {
        try {
            val logFile = File(appContext.getExternalFilesDir(null), LOG_FILE_NAME)
            if (logFile.exists() && logFile.length() > MAX_LOG_FILE_SIZE) {
                truncateLogFile(logFile)
            }
            val existingContent = if (logFile.exists()) logFile.readText() else ""
            val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val newLogEntry = "${dateFormat.format(System.currentTimeMillis())}:  $message\n"
            FileWriter(logFile).use { writer ->
                writer.write(newLogEntry + existingContent)
            }
        } catch (e: IOException) {
            Log.e("Logs", "Failed to write log to file: ${e.message}")
        }
    }

    private fun truncateLogFile(logFile: File) {
        try {
            val lines = logFile.readLines()
            if (lines.size > 500) {
                val newContent = lines.takeLast(500).joinToString("\n")
                FileWriter(logFile).use { it.write(newContent) }
                Log.d("Logs", "Log file truncated to maintain size limit")
            }
        } catch (e: IOException) {
            Log.e("Logs", "Failed to truncate log file: ${e.message}")
        }
    }

    fun getLogFileContent(): String {
        if (!::appContext.isInitialized) return ""
        val logFile = File(appContext.getExternalFilesDir(null), LOG_FILE_NAME)
        return if (logFile.exists()) {
            logFile.readText()
        } else {
            "No logs available."
        }
    }
}
