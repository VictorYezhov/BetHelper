package io.mvs.bethelper

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.observe
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import io.mvs.bethelper.data.Match
import io.mvs.bethelper.data.Team
import io.mvs.bethelper.service.BetAnalyzer
import io.mvs.bethelper.service.OutCome
import io.mvs.bethelper.service.OutcomeGenerator
import java.util.*
import java.util.stream.Collectors.toList
import java.util.stream.IntStream
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    private val gamesViewModel : GamesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val analyzer = BetAnalyzer()

        val matches = ArrayList<Match>()

        matches.add(Match(Team("Schalke"), Team("Mainz"), 0.3f, 0.7f,0f, 0f, 6.6f, 2.82f))
        matches.add(Match(Team("Barcelona"), Team("Saski Baskonia"), 0.80f, 0.2f,0f, 1.2f, 5f, 6.37f))
        matches.add(Match(Team("Valencia"), Team("Villarreal"), 0.25f, 0.75f,0f, 1.25f, 11f, 1.34f))
        matches.add(Match(Team("Huddersfield Town"), Team("Cardiff City"), 0.4f, 0.6f, 0f,0f, 4.97f, 2.82f))
        matches.forEach {
            Log.i("MATCH:", "$it")
        }
        val data = analyzer.performAnalysts(matches)

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