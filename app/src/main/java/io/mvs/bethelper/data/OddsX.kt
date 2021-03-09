package io.mvs.bethelper.data

import com.google.gson.annotations.SerializedName

data class OddsX(
    @SerializedName("1")
    val homeTeamWinCoefficient : Double,
    val `12`: Double,
    val `1X`: Double,
    @SerializedName("2")
    val awayTeamWinCoefficient : Double,
    @SerializedName("X")
    val drawCoefficient : Double,
    val X2: Double
)