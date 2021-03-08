package io.mvs.bethelper.service

import android.util.Log
import io.mvs.bethelper.data.BetData
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
                    percent *= outcome.games[index].winningPercentage
                    winningAmount += ((outcome.games[index].proposedBetPart * betSize) * outcome.games[index].coefficient)
                }else{
                    percent *= (1-outcome.games[index].winningPercentage)
                }
            }
            outcome.winAmount = (winningAmount - betSize)
            outcome.outComePercent = percent
        }

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