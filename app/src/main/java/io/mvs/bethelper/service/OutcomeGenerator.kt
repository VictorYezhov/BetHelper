package io.mvs.bethelper.service

import android.util.Log
import io.mvs.bethelper.data.BetData
import io.mvs.bethelper.data.BetType
import java.util.*
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.collections.ArrayList

class OutcomeGenerator {




    fun calculateOutcome(betData: ArrayList<BetData>, betSize : Float) : ArrayList<OutCome>{

        val possibleResults = bool(betData.size)

        val outcomes = ArrayList<OutCome>()
        var index = 0
        possibleResults.forEach {
            outcomes.add(OutCome(possibleResults = possibleResults[index].toList(), betData))
            index++
        }

        calculatePossibleOutComes(outcomes, betSize)
        return outcomes;
    }


    private fun calculatePossibleOutComes(outcomes : ArrayList<OutCome>, betSize: Float){


        outcomes.forEach { outcome->

            var percent = 1f
            var winningAmount = 0f;
            outcome.possibleResults.forEachIndexed { index, b ->
                if(b){
                    percent *= when(outcome.games[index].betType){
                        BetType.WINNER -> outcome.games[index].winningPercentage
                        BetType.WINNER_OR_DRAW -> (outcome.games[index].winningPercentage + outcome.games[index].targetMatch.drawPercent )
                    }
                    val betPart = outcome.games[index].proposedBetPart * betSize
                    winningAmount += (betPart * outcome.games[index].coefficient)
//                    println("Bet Part: $betPart")
//                    println("winning: ${(betPart * outcome.games[index].coefficient)}")
//                    println("Total: $winningAmount")
                }else{
                    percent *= when(outcome.games[index].betType){
                        BetType.WINNER -> (1-outcome.games[index].winningPercentage)
                        BetType.WINNER_OR_DRAW ->(1 - (outcome.games[index].winningPercentage + outcome.games[index].targetMatch.drawPercent ))
                    }
                }
            }
            outcome.winAmount = (winningAmount - betSize)
            outcome.outComePercent = percent
         //   printOutCome(outcome)

        }

    }

    fun printOutCome(outCome: OutCome) {
        println(" ")
        outCome.games.forEachIndexed { index, betData ->
            println("${betData.winningTeam.name} ${betData.winningPercentage * 100}%  ${if (outCome.possibleResults[index]) "win" else "lose"} ")
        }
        println("Total win: ${outCome.winAmount}")
        println("OutCome percent: ${outCome.outComePercent * 100}%")
    }

    private fun bitSetToArray(bs: BitSet, width: Int): BooleanArray {
        val result = BooleanArray(width) // all false
        bs.stream().forEach { i -> result[i] = true }
        return result
    }

    private fun bool(n: Int): List<BooleanArray> {
        return IntStream.range(0, Math.pow(2.0, n.toDouble()).toInt())
            .mapToObj { i -> bitSetToArray(BitSet.valueOf(longArrayOf(i.toLong())), n) }
            .collect(Collectors.toList())!!
    }
}

data class OutCome(
    val possibleResults : List<Boolean>,
    val games : ArrayList<BetData>,
    var outComePercent : Float = 0f,
    var winAmount : Float = 0f
)

data class GeneralStats(
    val totalWinPercent : Float,
    val totalLosePercent : Float,
    val maxWinAmount : Float,
    val minWinAmount : Float,
    val maxClearWinAmount : Float,
    val minClearWinAmount : Float,
    val maxLoseAmount : Float,
    val minLoseAmount: Float
)