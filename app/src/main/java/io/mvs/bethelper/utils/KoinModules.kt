package io.mvs.bethelper.utils

import io.mvs.bethelper.networking.managers.GameDataManager
import io.mvs.bethelper.networking.managers.GameDataManagerImpl
import org.koin.dsl.bind
import org.koin.dsl.module

object KoinModules {

        val module = module {
                single { GameDataManagerImpl() } bind GameDataManager::class

        }
}