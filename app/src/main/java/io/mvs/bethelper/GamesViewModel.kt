package io.mvs.bethelper

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.mvs.bethelper.data.Match
import io.mvs.bethelper.data.Team
import io.mvs.bethelper.networking.managers.GameDataManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GamesViewModel : ViewModel(){

    private val gameDataManager : GameDataManager by inject()

    val gamesLiveData = MutableLiveData<ArrayList<Match>>()

    init {
        getGames()
    }

    private fun getGames(){
        viewModelScope.launch(Dispatchers.IO){
             gameDataManager.getGameData()?.data?.let {
                 val matches = ArrayList<Match>()

                 for (i in it){

                     val homeTeam = i.teams[0]
                     val awayTeam = i.teams[1]

                     val winCoefficient = i.sites[0].odds.h2h[0]
                     val loseCoefficient = i.sites[0].odds.h2h[1]
                     val drawCoefficient = if (i.sites[0].odds.h2h.size == 3) i.sites[0].odds.h2h[2] else 0f

                     val match = Match(Team(homeTeam), Team(awayTeam), 0f, 0f, 0f, winCoefficient, loseCoefficient, drawCoefficient)
                     matches.add(match)
                 }


                 System.err.println("------>   " + matches)
                 gamesLiveData.postValue(matches)


             }
        }
    }

}