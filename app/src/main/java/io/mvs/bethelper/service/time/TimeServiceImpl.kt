package io.mvs.bethelper.service.time

import android.text.format.DateFormat
import android.util.Log
import java.text.DateFormatSymbols
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class TimeServiceImpl : TimeService {

    override fun getTimeStringFromLong(format: String, time: Long): String {
        Log.i("TimeService:", "Long time : $time")
        return DateFormat.format(format, time).toString()
    }

    override fun getTimeValueFromString(string: String, format: String): Long {
        val f = SimpleDateFormat(format, Locale.getDefault())
        return try {
            val d= f.parse(string)
            d?.time ?: 0

        } catch (e: ParseException) {
            e.printStackTrace()
            0
        }
    }


    override fun getLocalizedMonths(): ArrayList<String>  = ArrayList(DateFormatSymbols().months.toList())

    override fun getLocalizedMothName(month: Int): String  = DateFormatSymbols().months[month-1]

    override fun getLocalizedWeekDays(): ArrayList<String> {
        var days = java.util.ArrayList(DateFormatSymbols().weekdays.toList())
        days.trimToSize()
        val daysNew = java.util.ArrayList(days.takeLast(days.size - 2))
        daysNew.add(days[1])


        daysNew.trimToSize()

        return daysNew

    }

    override fun getLocalizedDayName(day: Int): String = DateFormatSymbols().weekdays[day-1]

}