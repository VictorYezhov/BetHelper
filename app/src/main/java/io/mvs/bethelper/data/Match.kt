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
    val date : String,
    val competitionCluster : String,
    val competitionName : String
)