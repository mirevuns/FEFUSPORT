package com.example.fefusport.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.fefusport.R
import com.example.fefusport.model.ActivityItem
import com.google.android.material.imageview.ShapeableImageView

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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        if (item is ActivityItem.Activity) {
            (holder as ActivityViewHolder).bind(item, onClick)
        }
    }

    class ActivityViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val ivAvatar: ShapeableImageView = view.findViewById(R.id.ivAvatar)
        private val tvOwner: TextView = view.findViewById(R.id.tvOwner)
        private val tvDistance: TextView = view.findViewById(R.id.tvDistance)
        private val tvDuration: TextView = view.findViewById(R.id.tvDuration)
        private val tvType: TextView = view.findViewById(R.id.tvType)
        private val tvTimeAgo: TextView = view.findViewById(R.id.tvTimeAgo)
        private val tvStartEnd: TextView = view.findViewById(R.id.tvStartEnd)

        fun bind(item: ActivityItem.Activity, onClick: (ActivityItem.Activity) -> Unit) {
            tvOwner.text = item.ownerName
            tvDistance.text = item.distance
            tvDuration.text = item.duration
            tvType.text = item.type
            tvTimeAgo.text = item.timeAgo
            tvStartEnd.text = itemView.context.getString(
                R.string.activity_start_finish_format,
                item.startTime,
                item.endTime.ifEmpty { "—" }
            )

            if (item.ownerAvatar.isNullOrEmpty()) {
                ivAvatar.setImageResource(R.drawable.person)
            } else {
                ivAvatar.setImageURI(Uri.parse(item.ownerAvatar))
            }

            val textColor = if (item.isMyActivity) {
                ContextCompat.getColor(itemView.context, R.color.green)
            } else {
                ContextCompat.getColor(itemView.context, R.color.dark_blue)
            }
            tvDistance.setTextColor(textColor)
            tvType.setTextColor(textColor)
            
            itemView.setOnClickListener { onClick(item) }
        }
    }
}
