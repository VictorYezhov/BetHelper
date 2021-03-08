package io.mvs.bethelper.modules.bet_stats

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import io.mvs.bethelper.R
import io.mvs.bethelper.modules.bet_analysis.InputBetViewModel
import kotlinx.android.synthetic.main.bet_stats_fragment.*

class BetStatsFragment : Fragment(R.layout.bet_stats_fragment) {

    companion object {
        fun newInstance() = BetStatsFragment()
    }

    private  val viewModel: InputBetViewModel by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setData()
    }

    private fun setData(){
        viewModel.betStats.observe(viewLifecycleOwner, Observer {
            win_percent_value.text = "${Math.round(it.totalWinPercent*100)}%"
            lose_percent_value.text = "${Math.round(it.totalLosePercent*100)}%"
            max_total_win.text ="${it.maxWinAmount}"
            min_total_win.text = "${it.minWinAmount}"
            max_win_value.text = "${it.maxClearWinAmount}"
            min_win_value.text = "${it.minClearWinAmount}"
            max_lose_value.text = "${it.maxLoseAmount}"
            min_lose_value.text="${it.minLoseAmount}"
        })
    }

}