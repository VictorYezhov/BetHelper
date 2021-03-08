package io.mvs.bethelper

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import io.mvs.bethelper.utils.KoinModules
import org.koin.android.ext.android.getKoin
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class BetHelperApplication : Application() {

    companion object {
        private const val TAG = "CoffeeCRMApplication"
        private lateinit var INSTANCE: BetHelperApplication
        var activity: Activity? = null

        fun instance(): android.app.Application {
            return INSTANCE
        }
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this

        startKoin {
            // Android context
            androidContext(this@BetHelperApplication)
            // modules
            modules(KoinModules.module)
        }


        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(p0: Activity) {
                if (p0 is MainActivity) {
                    Log.i(TAG, "onActivityPaused")
                    activity = null
                }
            }

            override fun onActivityStarted(p0: Activity) {

                if (p0 is MainActivity) {
                    Log.i(TAG, "onActivityStarted")
                    activity = p0
                }

            }

            override fun onActivityDestroyed(p0: Activity) {

                if (p0 is MainActivity) {
                    Log.i(TAG, "onActivityDestroyed")
                    activity = null
                }
            }

            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
                Log.i(TAG, "onActivitySaveInstanceState")

            }

            override fun onActivityStopped(p0: Activity) {

                if (p0 is MainActivity) {
                    Log.i(TAG, "onActivityStopped")
                    activity = null
                }
            }

            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
                if (p0 is MainActivity) {
                    Log.i(TAG, "onActivityCreated")
                    activity = p0
                }
            }

            override fun onActivityResumed(p0: Activity) {
                if (p0 is MainActivity) {
                    Log.i(TAG, "onActivityResumed")
                    activity = p0
                }
            }
        })


    }
}

inline fun <reified T> inject(): Lazy<T> = BetHelperApplication.instance().getKoin().inject()