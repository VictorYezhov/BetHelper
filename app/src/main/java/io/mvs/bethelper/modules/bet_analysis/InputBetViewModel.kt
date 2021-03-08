package io.mvs.bethelper.modules.bet_analysis

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.debatty.java.stringsimilarity.JaroWinkler
import io.mvs.bethelper.data.BetData
import io.mvs.bethelper.data.Match
import io.mvs.bethelper.data.Team
import io.mvs.bethelper.inject
import io.mvs.bethelper.networking.managers.game.GameDataManager
import io.mvs.bethelper.networking.managers.prediction.PredictionDataManager
import io.mvs.bethelper.service.BetAnalyzer
import io.mvs.bethelper.service.GeneralStats
import io.mvs.bethelper.service.OutCome
import io.mvs.bethelper.service.OutcomeGenerator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InputBetViewModel : ViewModel() {

    val betData = MutableLiveData<ArrayList<BetData>>()
    val outComes =MutableLiveData<ArrayList<OutCome>>()
    val betStats = MutableLiveData<GeneralStats>()
    var betSize = 0
    private val analyzer = BetAnalyzer()
    private val statsGenerator = OutcomeGenerator()

    private val gameDataManager : GameDataManager by inject()
    private val predictionDataManager : PredictionDataManager by inject()

    val gamesLiveData = MutableLiveData<ArrayList<Match>>()

    suspend fun getGames() : ArrayList<Match>{

            val gameDataResponse =  gameDataManager.getGameData()?.data

            val predictionDataResponse = predictionDataManager.getGameDataPrediction().data

            val matches = ArrayList<Match>()

            for (i in gameDataResponse ?: ArrayList()){

                val homeTeam = i.teams[0]
                val awayTeam = i.teams[1]

                val winCoefficient = i.sites[0].odds.h2h[0]
                val loseCoefficient = i.sites[0].odds.h2h[1]
                val drawCoefficient = if (i.sites[0].odds.h2h.size == 3) i.sites[0].odds.h2h[2] else 0f

                val jw = JaroWinkler()

                predictionDataResponse.find {
                    (jw.similarity(it.home_team.toLowerCase(), homeTeam.toLowerCase()) > 0.7 && jw.similarity(it.away_team.toLowerCase(), awayTeam.toLowerCase()) > 0.7) ||
                            (jw.similarity(it.home_team.toLowerCase(), awayTeam.toLowerCase()) > 0.7 && jw.similarity(it.away_team.toLowerCase(), homeTeam.toLowerCase()) > 0.7)
                }?.let {
                    val match = Match(Team(homeTeam), Team(awayTeam), it.probabilities.winHomeTeam.toFloat(), it.probabilities.winAwayTeam.toFloat(), it.probabilities.draw.toFloat(), winCoefficient, loseCoefficient, drawCoefficient)
                    matches.add(match)
                }

            }

            gamesLiveData.postValue(matches)
        return  matches

    }

    fun analyse(betSize : Int){
        this.betSize = betSize
        viewModelScope.launch(Dispatchers.IO){
            val matches = getGames()
            val data = analyzer.performAnalysts(matches)
            betData.postValue(data)
            val outComesData = statsGenerator.calculateOutcome(data, betSize.toFloat())
            outComes.postValue(outComesData)
            calculateStats(outComesData)
        }
    }

    private fun calculateStats(outComes : ArrayList<OutCome>){

        var totalWinPercent = 0f
        var maxClearWin = 0f
        var minClearWin = Int.MAX_VALUE.toFloat()
        var minLose = Int.MAX_VALUE.toFloat()


        outComes.forEach { outCome->
            if(outCome.winAmount > 0f){
                totalWinPercent += outCome.outComePercent

                if(outCome.winAmount > maxClearWin){
                    maxClearWin = outCome.winAmount
                }

                if(outCome.winAmount < minClearWin){
                    minClearWin = outCome.winAmount
                }
            }else{
                if(outCome.winAmount < minLose){
                    minLose = outCome.winAmount
                }
            }
        }
        Log.i("ANALISIs", "WIN: $totalWinPercent")

        betStats.postValue(GeneralStats(totalWinPercent,
            1-totalWinPercent,
            maxClearWin+betSize,
            minClearWin+betSize,
            maxClearWin,
            minClearWin,
            betSize.toFloat(),
            minLose))
    }


}