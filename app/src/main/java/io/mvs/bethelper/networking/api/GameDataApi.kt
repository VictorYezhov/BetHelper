package io.mvs.bethelper.networking.api

import io.mvs.bethelper.data.ResponseOdd
import retrofit2.http.GET
import retrofit2.http.Query

interface GameDataApi {
    @GET("/v3/odds/")
    suspend fun getGameData(
        @Query("sport") sport : String,
        @Query("region") region : String,
        @Query("mkt") mkt : String,
        @Query("dateFormat") dateFormat : String,
        @Query("apiKey") apiKey : String
    ): ResponseOdd

}
