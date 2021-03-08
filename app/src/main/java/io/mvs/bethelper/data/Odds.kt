package io.mvs.bethelper.data

data class ResponseOdd(
    val success : Boolean,
    val data : ArrayList<Odds>
)

data class Odds (
    val sport_key : String,
    val sport_nice : String,
    val teams : ArrayList<String>,
    val home_team : String,
    val sites : ArrayList<Bookmaker>
)

data class Bookmaker(
    val site_key : String,
    val site_nice : String,
    val odds : Coefficients
)

data class Coefficients(
    val h2h : ArrayList<Float>
)