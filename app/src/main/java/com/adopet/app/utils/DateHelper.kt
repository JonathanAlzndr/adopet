 package com.adopet.app.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

 object DateHelper {

    private const val INPUT_FORMAT = "yyyy-MM-dd'T'HH:mm:ss"

    fun parseDate(input: String): Date? {
        return try {
            val formatter = SimpleDateFormat(INPUT_FORMAT, Locale.getDefault())
            formatter.parse(input)
        } catch (e: ParseException) {
            e.printStackTrace()
            null
        }
    }

    fun formatDate(input: String, outputFormat: String = "dd MMM yyyy, HH:mm"): String? {
        val date = parseDate(input) ?: return null
        val formatter = SimpleDateFormat(outputFormat, Locale.getDefault())
        return formatter.format(date)
    }
}
