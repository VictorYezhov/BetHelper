package io.mvs.bethelper.networking.managers.prediction

import io.mvs.bethelper.BuildConfig
import io.mvs.bethelper.data.BaseData
import io.mvs.bethelper.data.PredictionData
import io.mvs.bethelper.networking.api.PredictionDataApi
import io.mvs.bethelper.networking.base.BaseApiConnector
import java.lang.Exception

class PredictionDataManagerImpl : PredictionDataManager, BaseApiConnector<PredictionDataApi>(PredictionDataApi::class.java, BuildConfig.FOOTBALL_PREDICTION) {
    override suspend fun getGameDataPrediction(): BaseData<ArrayList<PredictionData>> {
        return try {
            apiClient.getGameDataPrediction()
        } catch (ex : Exception){
            ex.printStackTrace()
            BaseData(ArrayList())
        }
    }

}