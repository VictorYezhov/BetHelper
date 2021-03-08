package io.mvs.bethelper.utils

import io.mvs.bethelper.networking.managers.game.GameDataManager
import io.mvs.bethelper.networking.managers.game.GameDataManagerImpl
import io.mvs.bethelper.networking.managers.prediction.PredictionDataManager
import io.mvs.bethelper.networking.managers.prediction.PredictionDataManagerImpl
import org.koin.dsl.bind
import org.koin.dsl.module

object KoinModules {

        val module = module {
                single { GameDataManagerImpl() } bind GameDataManager::class
                single { PredictionDataManagerImpl() } bind PredictionDataManager::class
        }
}