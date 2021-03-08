package io.mvs.bethelper.networking.managers.prediction

import io.mvs.bethelper.data.BaseData
import io.mvs.bethelper.data.PredictionData

interface PredictionDataManager {
    suspend fun getGameDataPrediction() : BaseData<ArrayList<PredictionData>>
}