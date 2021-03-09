package io.mvs.bethelper.data

data class BetData(
    val targetMatch : Match,
    val winningTeam : Team,
    val betType : BetType,
    val winningPercentage : Float = 0f, // Percent that team will win
    val coefficient: Float = 0f, // Betting coefficient for this team
    val winOrDrawPercentage : Float = 0f,
    val winOrDrawCoefficient : Float = 0f,
    var winningPercentCoefficient : Float = 0f,
    var bettingCoefficient : Float = 0f,
    var generalCoefficient : Float =0f,
    var proposedBetPart : Float = 0f,

)