package io.mvs.bethelper.networking.managers

import io.mvs.bethelper.BuildConfig
import io.mvs.bethelper.data.ResponseOdd
import io.mvs.bethelper.networking.api.GameDataApi
import io.mvs.bethelper.networking.base.BaseApiConnector
import io.mvs.bethelper.utils.Constants

class GameDataManagerImpl : GameDataManager, BaseApiConnector<GameDataApi>(GameDataApi::class.java, BuildConfig.BASE_URL) {
    override suspend fun getGameData(
    ): ResponseOdd? {
        return try {
            apiClient.getGameData("soccer_epl", "eu", "h2h", "iso", Constants.apiKey)
        } catch (ex : Exception){
            null
        }
    }

}