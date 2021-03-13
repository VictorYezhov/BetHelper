package io.mvs.bethelper.modules.bet_analysis.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import io.mvs.bethelper.R
import io.mvs.bethelper.data.BetData
import kotlinx.android.synthetic.main.bet_item_layout.view.*

class BetDataAdapter(private val items : ArrayList<BetData>, private  val betSize : Int) : RecyclerView.Adapter<BetDataAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return  ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.bet_item_layout, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = items[position]


        holder.itemView.match_value.text = "${data.targetMatch.homeTeam.name} vs ${data.targetMatch.awayTeam.name}"
        holder.itemView.date_value.text = data.targetMatch.date
        holder.itemView.betPart.text = "${Math.round(betSize * data.proposedBetPart)}"
        holder.itemView.win_team_name.text = "${data.winningTeam.name}"
        holder.itemView.coef_value.text = "${data.coefficient}"
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}