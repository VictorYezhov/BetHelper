package io.mvs.bethelper.networking.managers

import io.mvs.bethelper.data.ResponseOdd

interface GameDataManager {
    suspend fun getGameData() : ResponseOdd?
}