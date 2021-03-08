package io.mvs.bethelper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import info.debatty.java.stringsimilarity.JaroWinkler
import io.mvs.bethelper.data.Match
import io.mvs.bethelper.data.Team
import io.mvs.bethelper.networking.managers.game.GameDataManager
import io.mvs.bethelper.networking.managers.prediction.PredictionDataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class GamesViewModel : ViewModel(){

    private val gameDataManager : GameDataManager by inject()
    private val predictionDataManager : PredictionDataManager by inject()

    val gamesLiveData = MutableLiveData<ArrayList<Match>>()

    init {
       // getGames()
    }

    private fun getGames(){
        viewModelScope.launch(Dispatchers.IO){
            val gameDataResponse =  gameDataManager.getGameData()?.data

            val predictionDataResponse = predictionDataManager.getGameDataPrediction().data

            val matches = ArrayList<Match>()

            for (i in gameDataResponse ?: ArrayList()){

                val homeTeam = i.home_team
                val awayTeam = if(i.teams[0] == homeTeam) i.teams[1] else i.teams[0]

                val winCoefficient = i.sites[0].odds.h2h[0]
                val loseCoefficient = i.sites[0].odds.h2h[1]
                val drawCoefficient = if (i.sites[0].odds.h2h.size == 3) i.sites[0].odds.h2h[2] else 0f

                val jw = JaroWinkler()

                predictionDataResponse.find {
                            (jw.similarity(it.home_team.toLowerCase(), homeTeam.toLowerCase()) > 0.7 && jw.similarity(it.away_team.toLowerCase(), awayTeam.toLowerCase()) > 0.7) ||
                            (jw.similarity(it.home_team.toLowerCase(), awayTeam.toLowerCase()) > 0.7 && jw.similarity(it.away_team.toLowerCase(), homeTeam.toLowerCase()) > 0.7)
                }?.let {
//                    val match = Match(Team(homeTeam), Team(awayTeam), it.probabilities.winHomeTeam.toFloat(), it.probabilities.winAwayTeam.toFloat(), it.probabilities.draw.toFloat(), winCoefficient, loseCoefficient, drawCoefficient)
//                    matches.add(match)
                }

            }

            gamesLiveData.postValue(matches)

        }
    }

}