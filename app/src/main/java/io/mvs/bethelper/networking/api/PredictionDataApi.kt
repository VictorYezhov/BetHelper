package io.mvs.bethelper.networking.api

import io.mvs.bethelper.data.BaseData
import io.mvs.bethelper.data.PredictionData
import retrofit2.http.GET
import retrofit2.http.Headers

interface PredictionDataApi {

    @Headers("X-RapidAPI-Key: cc54ad74b0mshd8d782b2573bfcep1f7d13jsn8201c0ec289e")
    @GET ("/api/v2/predictions")
    suspend fun getGameDataPrediction() : BaseData<ArrayList<PredictionData>>
}