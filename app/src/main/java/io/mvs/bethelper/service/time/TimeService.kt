package io.mvs.bethelper.service.time



interface TimeService{

    fun getTimeStringFromLong(format : String, time : Long) : String

    fun getTimeValueFromString(string: String, format: String) : Long

    fun getLocalizedMonths() : ArrayList<String>

    fun getLocalizedMothName(month: Int) : String

    fun getLocalizedWeekDays() : ArrayList<String>

    fun getLocalizedDayName(day : Int) : String



}