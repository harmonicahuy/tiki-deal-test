package vn.tiki.android.androidhometest.adapter

import android.content.Context
import android.net.Uri
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import vn.tiki.android.androidhometest.R
import vn.tiki.android.androidhometest.data.api.response.Deal
import kotlinx.android.synthetic.main.item_deal.view.*
import rx.subjects.PublishSubject
import vn.tiki.android.androidhometest.util.Utils
import java.util.*

/**
 * Created by Harmony on 31/07/2018.
 */
class DealAdapter(val list: MutableList<Deal>?, val context: Context) : RecyclerView.Adapter<DealAdapter.DealViewHolder>() {

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        val deal = list?.get(position)
        Glide.with(context).load(Uri.parse(deal?.productThumbnail)).into(holder.imgThumbnail)
        holder.tvProductName.text = deal?.productName
        holder.tvProductPrice.text = deal?.productPrice.toString()
        updateTimeRemaining(holder, deal!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        return DealViewHolder(LayoutInflater.from(context).inflate(R.layout.item_deal, parent, false))
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    /**
     * Calculate time remain of deal
     * Deal valid: update time remain on UI
     * Deal expired: remove deal
     * */
    fun updateTimeRemaining(holder: DealViewHolder, deal: Deal) {
        val timeDiff = deal.endDate.time - System.currentTimeMillis()
        if (timeDiff > 0) {
            val seconds = (timeDiff / 1000).toInt() % 60
            val minutes = (timeDiff / (1000 * 60) % 60).toInt()
            val hours = (timeDiff / (1000 * 60 * 60) % 24).toInt()

            val hh = Utils.formatNumber(hours)
            val mm = Utils.formatNumber(minutes)
            val ss = Utils.formatNumber(seconds)
            val timeRemain = "$hh:$mm:$ss"
            holder.tvDealEnd.setText(timeRemain)
        } else {
            list?.remove(deal)
        }
    }

    class DealViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgThumbnail = itemView.imgThumbnail
        val tvProductName = itemView.tvProductName
        val tvProductPrice = itemView.tvProductPrice
        val tvDealEnd = itemView.tvDealEnd
    }
}