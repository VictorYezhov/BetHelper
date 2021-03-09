package io.mvs.bethelper.modules.bet_analysis

import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.debatty.java.stringsimilarity.JaroWinkler
import io.mvs.bethelper.data.BetData
import io.mvs.bethelper.data.Match
import io.mvs.bethelper.data.PredictionData
import io.mvs.bethelper.data.Team
import io.mvs.bethelper.inject
import io.mvs.bethelper.networking.managers.game.GameDataManager
import io.mvs.bethelper.networking.managers.prediction.PredictionDataManager
import io.mvs.bethelper.service.BetAnalyzer
import io.mvs.bethelper.service.GeneralStats
import io.mvs.bethelper.service.OutCome
import io.mvs.bethelper.service.OutcomeGenerator
import io.mvs.bethelper.service.time.TimeService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class InputBetViewModel : ViewModel() {

    val betData = MutableLiveData<ArrayList<BetData>>()
    val outComes =MutableLiveData<ArrayList<OutCome>>()
    val betStats = MutableLiveData<GeneralStats>()
    var betSize = 0
    private val analyzer = BetAnalyzer()
    private val statsGenerator = OutcomeGenerator()

    private val gameDataManager : GameDataManager by inject()
    private val predictionDataManager : PredictionDataManager by inject()
    private val timeService : TimeService by inject()

    val gamesLiveData = MutableLiveData<ArrayList<Match>>()

    private val matchesMap = HashMap<String, PredictionData>()
    suspend fun getGames() : ArrayList<Match>{

            //val gameDataResponse =  gameDataManager.getGameData()?.data

            val jw = JaroWinkler()
            val predictionDataResponse = predictionDataManager.getGameDataPrediction().data

            predictionDataResponse.forEach {
                matchesMap["${it.home_team}-${it.away_team}"] = it
            }
            val matches = ArrayList<Match>()


            predictionDataResponse.clear()
            predictionDataResponse.addAll(matchesMap.values)

            val filtered =  predictionDataResponse.filter{DateUtils.isToday(timeService.getTimeValueFromString(it.start_date, "yyyy-MM-dd'T'hh:mm:ss"))}//.filter {  it.start_date.contains(timeService.getTimeStringFromLong("yyyy-MM-dd", System.currentTimeMillis())) }
            for (i in filtered ?: ArrayList()){

                val homeTeam = i.home_team
                val awayTeam = i.away_team

                val homeTeamCoefficient = i.odds.homeTeamWinCoefficient
                val awayTeamCoefficient = i.odds.awayTeamWinCoefficient
                val drawCoefficient = i.odds.drawCoefficient



                val match = Match(Team(homeTeam), Team(awayTeam), i.probabilities.winHomeTeam.toFloat(), i.probabilities.winAwayTeam.toFloat(), i.probabilities.draw.toFloat(), homeTeamCoefficient.toFloat(), awayTeamCoefficient.toFloat(), drawCoefficient.toFloat(), i.start_date)
                matches.add(match)

//                predictionDataResponse.find {
//                    (jw.similarity(it.home_team.toLowerCase(), homeTeam.toLowerCase()) > 0.7 && jw.similarity(it.away_team.toLowerCase(), awayTeam.toLowerCase()) > 0.7) ||
//                            (jw.similarity(it.home_team.toLowerCase(), awayTeam.toLowerCase()) > 0.7 && jw.similarity(it.away_team.toLowerCase(), homeTeam.toLowerCase()) > 0.7)
//                }?.let {

                //}

            }

       // System.err.println(matches)
            gamesLiveData.postValue(matches)
        return  matches

    }

    fun analyse(betSize : Int){
        this.betSize = betSize
        viewModelScope.launch(Dispatchers.IO){
            val matches = getGames()
            val data = analyzer.performWinLoseDrawAnalysis(matches)
            betData.postValue(data)
            val outComesData = statsGenerator.calculateOutcome(data, betSize.toFloat())
            outComes.postValue(outComesData)
            calculateStats(outComesData)
        }
    }

    private fun calculateStats(outComes : ArrayList<OutCome>){

        var totalWinPercent = 0f
        var totalLosePercent = 0f
        var maxClearWin = 0f
        var minClearWin = Int.MAX_VALUE.toFloat()
        var minLose = Int.MAX_VALUE.toFloat() * -1


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
                if(outCome.winAmount > minLose){
                    minLose = outCome.winAmount
                }
                totalLosePercent += outCome.outComePercent
            }
        }
        Log.i("ANALISIs", "WIN: $totalWinPercent")

        betStats.postValue(GeneralStats(totalWinPercent,
            totalLosePercent,
            (maxClearWin + betSize).roundToInt().toFloat(),
            (minClearWin+betSize).roundToInt().toFloat(),
            maxClearWin.roundToInt().toFloat(),
            minClearWin.roundToInt().toFloat(),
            betSize.toFloat().roundToInt().toFloat(),
            minLose.roundToInt().toFloat()))
    }


}