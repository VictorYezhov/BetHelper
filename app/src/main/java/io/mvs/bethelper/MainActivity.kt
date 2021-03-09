package io.mvs.bethelper

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import io.mvs.bethelper.data.Match
import io.mvs.bethelper.service.BetAnalyzer
import io.mvs.bethelper.service.OutCome
import io.mvs.bethelper.service.OutcomeGenerator
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val gamesViewModel : GamesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val analyzer = BetAnalyzer()

        val matches = ArrayList<Match>()

        val data = analyzer.performWinLoseAnalysis(matches)

        data.forEach {
            Log.i(
                "PREDICTION:",
                "Bet on Team: ${it.winningTeam.name} proposed betPart: ${it.proposedBetPart}"
            )
        }
        Log.i("ANALIZATOR:", "----------------------------------------")

        val outComeAnalyzer = OutcomeGenerator()

        val results = outComeAnalyzer.calculateOutcome(data, 100f);
        results.forEachIndexed { index, outCome ->
            Log.i("-------------", "---------------")
            Log.i("STATS:", "OutCome $index")
            printOutCome(outCome)
        }

        var totalWinPercent = 0f

        results.forEach {
            if(it.winAmount  > 0f){
                totalWinPercent+= it.outComePercent
            }
        }

        Log.i("GENERAL STATS: ", "Percent of going up: ${totalWinPercent*100}% Percent on going down: ${(1-totalWinPercent)*100}% ")


        setupObservers()
        findNavController(R.id.nav_host_fragment)
    }


    fun printOutCome(outCome: OutCome) {
        println(" ")
        outCome.games.forEachIndexed { index, betData ->
            println("${betData.winningTeam.name} ${betData.winningPercentage * 100}%  ${if (outCome.possibleResults[index]) "win" else "lose"} ")
        }
        println("Total win: ${outCome.winAmount}")
        println("OutCome percent: ${outCome.outComePercent * 100}%")
    }

    private fun setupObservers() {
        gamesViewModel.gamesLiveData.observe(this) {
            System.err.println(it)
        }
    }


}