package com.example.fefusport.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.fefusport.R
import com.example.fefusport.model.ActivityItem

class ActivityAdapter(
    private var items: List<ActivityItem>,
    private val onClick: (ActivityItem.Activity) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_ACTIVITY = 1
    }

    fun updateItems(newItems: List<ActivityItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return VIEW_TYPE_ACTIVITY
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun getItemCount(): Int = items.size

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (item is ActivityItem.Activity) {
            (holder as ActivityViewHolder).bind(item, onClick)
        }
    }

    class ActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val tvDistance: TextView = view.findViewById(R.id.tvDistance)
        private val tvDuration: TextView = view.findViewById(R.id.tvDuration)
        private val tvType: TextView = view.findViewById(R.id.tvType)
        private val tvTimeAgo: TextView = view.findViewById(R.id.tvTimeAgo)

        @RequiresApi(Build.VERSION_CODES.M)
        fun bind(item: ActivityItem.Activity, onClick: (ActivityItem.Activity) -> Unit) {
            tvDistance.text = item.distance
            tvDuration.text = item.duration
            tvType.text = item.type
            tvTimeAgo.text = item.timeAgo

            val textColor = if (item.isMyActivity) {
                itemView.context.getColor(R.color.purple_700)
            } else {
                itemView.context.getColor(R.color.black)
            }
            tvDistance.setTextColor(textColor)
            tvType.setTextColor(textColor)
            
            itemView.setOnClickListener { onClick(item) }
        }
    }
}
