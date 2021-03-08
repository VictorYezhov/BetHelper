package io.mvs.bethelper.networking.managers.game

import io.mvs.bethelper.data.ResponseOdd

interface GameDataManager {
    suspend fun getGameData() : ResponseOdd?
}