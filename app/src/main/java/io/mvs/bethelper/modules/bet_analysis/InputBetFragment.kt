package io.mvs.bethelper.modules.bet_analysis

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.mvs.bethelper.R
import io.mvs.bethelper.modules.bet_analysis.adapter.BetDataAdapter
import kotlinx.android.synthetic.main.input_bet_fragment.*

class InputBetFragment : Fragment(R.layout.input_bet_fragment) {

    companion object {
        fun newInstance() = InputBetFragment()
    }

    private  val viewModel: InputBetViewModel by activityViewModels()

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupListeners(){
        analyse_btn.setOnClickListener {
            viewModel.analyse(bet_size_input.text.toString().toInt())
        }
        open_details_btn.setOnClickListener {
            view?.findNavController()?.navigate(InputBetFragmentDirections.actionInputBetFragmentToBetStatsFragment())
        }
    }

    private fun setupObservers(){
        viewModel.betData.observe(viewLifecycleOwner, Observer {
            betList.apply {
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = BetDataAdapter(it, viewModel.betSize)
            }
            open_details_btn.visibility = View.VISIBLE
        })
    }
}