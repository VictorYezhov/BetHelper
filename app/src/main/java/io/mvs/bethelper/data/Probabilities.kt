package io.mvs.bethelper.data

import com.google.gson.annotations.SerializedName

data class Probabilities(
    @SerializedName("1")
    val winHomeTeam: Double,
    val `12`: Double,
    val `1X`: Double,
    @SerializedName("2")
    val winAwayTeam: Double,
    @SerializedName("X")
    val draw: Double,
    val X2: Double
)