package com.mercadobitcoin.core.common

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

object DateFormatter {
    private val formatter =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        DateTimeFormatter.ofPattern("dd/MM/yyyy")
    } else {
        SimpleDateFormat("yyyy-MM-dd", Locale.US)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun format(date: LocalDate?): String {
        return date?.format(formatter as DateTimeFormatter?) ?: "-"
    }
}