package io.mvs.bethelper.service

import android.util.Log
import io.mvs.bethelper.data.BetData
import io.mvs.bethelper.data.BetType
import io.mvs.bethelper.data.Match
import kotlin.math.abs

class BetAnalyzer {

    companion object{
        private const val DIFFERENCE_MARGIN = 0.2f
        private const val BETTING_AMOUNT = 5
    }


    fun performWinLoseAnalysis(matches :  ArrayList<Match>) : ArrayList<BetData>{
        return calculateProposedPart(calculateBettingCoefficient(calculateWinningPercentCoefficient(composeBetData(filterMostProbable(matches), BetType.WINNER))))
    }

    fun performWinLoseDrawAnalysis(matches :  ArrayList<Match>) : ArrayList<BetData>{
        return calculateProposedPart(calculateBettingCoefficient(calculateWinningPercentCoefficient(composeBetData(filterMostProbable(matches), BetType.WINNER_OR_DRAW))))
    }


    private fun filterMostProbable(matches :  ArrayList<Match>) : ArrayList<Match>{
       var filtred =  matches.filter { match -> abs(match.homeWinPercent - match.awayWinPercent) > DIFFERENCE_MARGIN}
//        if(filtred.size < BETTING_AMOUNT){
//            filtred =  matches.filter { match -> abs(match.homeWinPercent - match.awayWinPercent) > DIFFERENCE_MARGIN - 0.05}
//        }
        filtred = filtred.sortedBy { match -> -1*abs(match.homeWinPercent - match.awayWinPercent)  }
        filtred.forEach { match->
            Log.i("BetAnalyzer", "Propability difference: ${abs(match.homeWinPercent - match.awayWinPercent)}")
        }
        if(filtred.size > BETTING_AMOUNT){
           filtred = filtred.subList(0, BETTING_AMOUNT)
        }
        println(filtred)
        return ArrayList(filtred)
    }

    private fun composeBetData(matches :  ArrayList<Match>, betType: BetType) :  ArrayList<BetData>{
        val betData = ArrayList<BetData>()

        matches.forEach { match ->
            Log.i("BetAnalyzer", "Home: ${match.homeTeamCoefficient} Away: ${match.awayTeamCoefficient}")
            if(match.homeWinPercent > match.awayWinPercent){
                Log.i("BetAnalyzer", "Choosing Home Team: ${match.homeTeam.name} ")
                betData.add(BetData(match, match.homeTeam, betType, match.homeWinPercent, match.homeTeamCoefficient))
            }else{
                Log.i("BetAnalyzer", "Choosing Away Team: ${match.awayTeam.name} ")
                betData.add(BetData(match, match.awayTeam, betType, match.awayWinPercent, match.awayTeamCoefficient))
            }

        }
        return betData
    }


    private fun calculateWinningPercentCoefficient(matches :  ArrayList<BetData>) :  ArrayList<BetData>{
        var winningPercentSum = 0f
        matches.forEach {
            winningPercentSum += when(it.betType){
                BetType.WINNER -> (it.winningPercentage)
                BetType.WINNER_OR_DRAW -> (it.winningPercentage + it.targetMatch.drawCoefficient)
            }
        }

        matches.forEach {
            it.winningPercentCoefficient = when(it.betType){
                BetType.WINNER -> it.winningPercentage / winningPercentSum
                BetType.WINNER_OR_DRAW -> (it.winningPercentage + it.targetMatch.drawPercent) / winningPercentSum
            }
        }
        return  matches
    }

    private fun calculateBettingCoefficient(matches :  ArrayList<BetData>) :  ArrayList<BetData>{
        var bettingSum = 0f
        matches.forEach {
            bettingSum += when(it.betType){
                BetType.WINNER -> it.coefficient
                BetType.WINNER_OR_DRAW -> (it.coefficient + it.targetMatch.drawCoefficient)
            }

        }
        matches.forEach {
            it.bettingCoefficient = when(it.betType){
            BetType.WINNER -> it.coefficient / bettingSum
            BetType.WINNER_OR_DRAW -> (it.coefficient + it.targetMatch.drawCoefficient) / bettingSum
        }
        }
        return  matches
    }

    private fun calculateProposedPart(matches :  ArrayList<BetData>) :  ArrayList<BetData>{

        var totalGeneralCoefficient = 0f
        matches.forEach {
            it.generalCoefficient = it.winningPercentCoefficient + it.bettingCoefficient
            totalGeneralCoefficient += it.generalCoefficient
        }
        matches.forEach {
            it.proposedBetPart= it.generalCoefficient / totalGeneralCoefficient
        }

        return matches
    }



}