package io.mvs.bethelper

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.observe
import io.mvs.bethelper.data.Match
import io.mvs.bethelper.data.Team
import io.mvs.bethelper.service.BetAnalyzer

class MainActivity : AppCompatActivity() {

    private val gamesViewModel : GamesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val analyzer = BetAnalyzer()

//        val matches = ArrayList<Match>()
//
//        matches.add(Match(Team("TA1"), Team("TB1"), 0.7f, 0.3f,0f,2f, 3f))
//        matches.add(Match(Team("TC1"), Team("TD1"), 0.5f, 0.5f,0f,2f, 3f))
//        matches.add(Match(Team("TA2"), Team("TB2"), 0.75f, 0.25f,0f,3f, 2f))
//        matches.add(Match(Team("TC2"), Team("TD2"), 0.35f, 0.65f,0f,1.5f, 1.5f))
//
//        matches.forEach {
//            Log.i("MATCH:", "$it")
//        }
//        val data = analyzer.performAnalysts(matches)
//
//        data.forEach {
//            Log.i("PREDICTION:", "$it  proposed Bet: ${it.proposedBetPart*100}")
//        }

        setupObservers()

    }

    private fun setupObservers(){
        gamesViewModel.gamesLiveData.observe(this){
            System.err.println(it)
        }
    }
}