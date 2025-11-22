package com.example.fefusport.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fefusport.R
import com.example.fefusport.model.UiFeedback
import com.google.android.material.imageview.ShapeableImageView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ActivityFeedbackAdapter :
    ListAdapter<UiFeedback, ActivityFeedbackAdapter.FeedbackViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feedback, parent, false)
        return FeedbackViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedbackViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class FeedbackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val avatarView: ShapeableImageView = itemView.findViewById(R.id.feedbackAvatar)
        private val authorView: TextView = itemView.findViewById(R.id.feedbackAuthor)
        private val commentView: TextView = itemView.findViewById(R.id.feedbackComment)
        private val dateView: TextView = itemView.findViewById(R.id.feedbackDate)
        private val ratingBar: RatingBar = itemView.findViewById(R.id.feedbackRating)
        private val formatter = SimpleDateFormat("dd.MM HH:mm", Locale.getDefault())

        fun bind(feedback: UiFeedback) {
            authorView.text = feedback.authorName
            commentView.text = feedback.comment
            dateView.text = formatter.format(Date(feedback.createdAt))
            ratingBar.rating = feedback.rating.toFloat()

            // Загружаем аватар пользователя
            if (feedback.authorAvatar.isNullOrEmpty()) {
                avatarView.setImageResource(R.drawable.person)
            } else {
                try {
                    val avatarUri = Uri.parse(feedback.authorAvatar)
                    avatarView.setImageURI(avatarUri)
                    // Если URI не загрузился, показываем дефолтную картинку
                    avatarView.tag = avatarUri
                } catch (e: Exception) {
                    avatarView.setImageResource(R.drawable.person)
                }
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<UiFeedback>() {
        override fun areItemsTheSame(oldItem: UiFeedback, newItem: UiFeedback): Boolean =
            oldItem.createdAt == newItem.createdAt && oldItem.authorName == newItem.authorName

        override fun areContentsTheSame(oldItem: UiFeedback, newItem: UiFeedback): Boolean =
            oldItem == newItem
    }
}

