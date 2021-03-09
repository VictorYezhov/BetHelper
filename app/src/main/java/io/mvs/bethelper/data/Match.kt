package io.mvs.bethelper.data

data class Match(
    val homeTeam : Team,
    val awayTeam : Team,
    val homeWinPercent : Float,
    val awayWinPercent : Float,
    val drawPercent : Float,
    val homeTeamCoefficient : Float,
    val awayTeamCoefficient : Float,
    val drawCoefficient : Float,
    val homeTeamWinOrDrawCoefficient : Float,
    val awayTeamWinOrDrawCoefficient : Float,
    val date : String
)