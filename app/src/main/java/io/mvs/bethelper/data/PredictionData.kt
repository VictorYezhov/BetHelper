package io.mvs.bethelper.data

data class PredictionData(
    val away_team: String,
    val competition_cluster: String,
    val competition_name: String,
    val federation: String,
    val home_team: String,
    val id: Int,
    val is_expired: Boolean,
    val last_update_at: String,
    val market: String,
    val odds: OddsX,
    val prediction: String,
    val probabilities: Probabilities,
    val result: String,
    val season: String,
    val start_date: String,
    val status: String
)