package com.example.homehub.utils
//import android.content.Context
//import android.util.Log
//import com.example.homehub.constants.Constants
//import com.example.homehub.constants.Constants.LOG_FILE_NAME
//import java.io.File
//import java.io.FileWriter
//import java.io.IOException
//import java.text.SimpleDateFormat
//import java.util.Locale
//
//
//    companion Logs {
//        private const val LOGGING_ENABLED: Boolean = false
//        private const val MAX_LOG_FILE_SIZE = 500 * 1024 // 500 kB in bytes
//        private lateinit var instance: Logs
//
//        fun initialize(context: Context) {
//            if (!::instance.isInitialized) {
//                instance = Logs(context.applicationContext) // Use application context to avoid memory leaks
//            }
//        }
//
//        fun d(text: String) {
//            if (this::instance.isInitialized) {
//                log("DEBUG: $text")
//            }
//        }
//
//        fun e(error: Exception) {
//            if (this::instance.isInitialized) {
//                log("ERROR: ${error.message}")
//            }
//        }
//
//        // Move the log function here to make it accessible
//        private fun log(message: String) {
//            if (LOGGING_ENABLED) {
//                logToFile(message)
//            }
//        }
//
//        private fun logToFile(message: String) {
//            try {
//                val logFile = File(instance.appContext.getExternalFilesDir(null), LOG_FILE_NAME)
//
//                // Truncate the log file if it exceeds the maximum size
//                if (logFile.exists() && logFile.length() > MAX_LOG_FILE_SIZE) {
//                    truncateLogFile(logFile)
//                }
//
//                // Read existing log content
//                val existingContent = if (logFile.exists()) logFile.readText() else ""
//
//                // Create the new log entry with timestamp
//                val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
//                val newLogEntry = "${dateFormat.format(System.currentTimeMillis())}:  $message\n"
//
//                // Write the new log entry followed by the existing content
//                FileWriter(logFile).use { writer ->
//                    writer.write(newLogEntry + existingContent)
//                }
//            } catch (e: IOException) {
//                Log.e("SG/E", "Failed to write log to file: ${e.message}")
//            }
//        }
//
//        private fun truncateLogFile(logFile: File) {
//            try {
//                val lines = logFile.readLines()
//                val lineCount = lines.size
//
//                // If the file has more than 500 lines, keep only the last 500 lines
//                if (lineCount > 500) {
//                    val newLogContent = lines.takeLast(500).joinToString("\n")
//                    FileWriter(logFile).use { it.write(newLogContent) } // Truncate by rewriting the file with new content
//                    Log.d("SG/D", "Log file truncated to maintain size limit")
//                }
//            } catch (e: IOException) {
//                Log.e("SG/E", "Failed to truncate log file: ${e.message}")
//            }
//        }
//
//        fun getLogFileContent(): String {
//            val logFile = File(instance.appContext.getExternalFilesDir(null), LOG_FILE_NAME)
//
//            return if (logFile.exists()) {
//                logFile.readText()
//            } else {
//                "No logs available."
//            }
//        }
//    }
