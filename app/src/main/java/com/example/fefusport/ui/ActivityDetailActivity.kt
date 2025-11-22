package com.example.fefusport.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.fefusport.R
import com.example.fefusport.adapter.ActivityFeedbackAdapter
import com.example.fefusport.data.preferences.SessionManager
import com.example.fefusport.data.repository.CommunityFeedbackRepository
import com.example.fefusport.model.UiFeedback
import com.example.fefusport.ui.viewmodel.ActivityViewModel
import com.example.fefusport.ui.viewmodel.UserViewModel
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityDetailActivity : AppCompatActivity() {
    private val activityViewModel: ActivityViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var sessionManager: SessionManager
    private var currentUserName: String = "Вы"
    private var currentUserAvatar: String? = null

    private val feedbackAdapter = ActivityFeedbackAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        sessionManager = SessionManager(this)

        val activityIdString = intent.getStringExtra("activity_id") ?: ""
        val activityId = activityIdString.toLongOrNull()
        var type = intent.getStringExtra("activity_type") ?: ""
        val distance = intent.getStringExtra("activity_distance") ?: ""
        val duration = intent.getStringExtra("activity_duration") ?: ""
        val startTime = intent.getStringExtra("activity_start_time") ?: ""
        val endTime = intent.getStringExtra("activity_end_time") ?: ""
        val ownerName = intent.getStringExtra("activity_owner") ?: "Участник ДВФУ"
        val ownerAvatarUri = intent.getStringExtra("activity_owner_avatar")
        val isMyActivity = intent.getBooleanExtra("is_my_activity", true)
        val isCommunity = intent.getBooleanExtra("is_community_activity", false)

        val activityTypeHeader = findViewById<TextView>(R.id.activityTypeHeader)
        val activityDistanceView = findViewById<TextView>(R.id.activityDistance)
        val activityDurationView = findViewById<TextView>(R.id.activityDuration)
        val activityTimeView = findViewById<TextView>(R.id.activityTime)
        val ownerNameView = findViewById<TextView>(R.id.ownerName)
        val ownerAvatarView = findViewById<ShapeableImageView>(R.id.ownerAvatar)
        val averageRatingText = findViewById<TextView>(R.id.averageRatingText)
        val averageRatingBar = findViewById<RatingBar>(R.id.averageRatingBar)
        val feedbackCount = findViewById<TextView>(R.id.feedbackCount)
        val commentInput = findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.commentInput).editText as? TextInputEditText
        commentInput?.text?.clear()
        val ratingBar = findViewById<RatingBar>(R.id.userRatingBar)
        val sendButton = findViewById<MaterialButton>(R.id.sendFeedbackButton)
        sendButton.setTextColor(ContextCompat.getColor(this, R.color.white))
        val feedbackRecycler = findViewById<RecyclerView>(R.id.feedbackRecyclerView)
        feedbackRecycler.layoutManager = LinearLayoutManager(this)
        feedbackRecycler.adapter = feedbackAdapter

        // Устанавливаем тип тренировки в заголовке рядом с аватаркой
        activityTypeHeader?.text = type
        
        // Устанавливаем тип тренировки в основном контенте (если есть)
        val activityTypeView = findViewById<TextView>(R.id.activityType)
        activityTypeView?.text = type
        activityDistanceView.text = distance
        activityDistanceView.setTextColor(android.graphics.Color.WHITE)
        activityDistanceView.setTypeface(null, android.graphics.Typeface.BOLD)
        activityDurationView.text = duration
        
        // Вычисляем endTime, если он пустой, используя duration
        val finalEndTime = if (endTime.isNotEmpty()) {
            endTime
        } else if (startTime.isNotEmpty() && duration.isNotEmpty()) {
            // Парсим startTime и duration, чтобы вычислить endTime
            try {
                val startTimeParts = startTime.split(":")
                if (startTimeParts.size == 2) {
                    val startHour = startTimeParts[0].toInt()
                    val startMin = startTimeParts[1].toInt()
                    
                    // Парсим duration (например, "1 ч 42 мин" или "45 мин")
                    val durationMinutes = when {
                        duration.contains("ч") -> {
                            val parts = duration.split("ч", "мин")
                            val hours = parts[0].trim().toIntOrNull() ?: 0
                            val mins = if (parts.size > 1) parts[1].trim().toIntOrNull() ?: 0 else 0
                            hours * 60 + mins
                        }
                        duration.contains("мин") -> {
                            duration.replace("мин", "").trim().toIntOrNull() ?: 0
                        }
                        else -> 0
                    }
                    
                    val totalMinutes = startHour * 60 + startMin + durationMinutes
                    val endHour = (totalMinutes / 60) % 24
                    val endMin = totalMinutes % 60
                    String.format("%02d:%02d", endHour, endMin)
                } else {
                    "—"
                }
            } catch (e: Exception) {
                "—"
            }
        } else {
            "—"
        }
        
        activityTimeView.text = getString(R.string.activity_start_finish_format, startTime, finalEndTime)
        ownerNameView.text = ownerName
        if (ownerAvatarUri.isNullOrEmpty()) {
            ownerAvatarView.setImageResource(R.drawable.person)
        } else {
            ownerAvatarView.setImageURI(Uri.parse(ownerAvatarUri))
        }

        findViewById<ImageButton>(R.id.backButton).setOnClickListener { finish() }
        findViewById<ImageButton>(R.id.shareButton).setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(
                    Intent.EXTRA_TEXT,
                    "Я закончил тренировку $type в FefuSport: $distance · $duration · старт $startTime."
                )
            }
            startActivity(Intent.createChooser(shareIntent, "Поделиться тренировкой"))
        }

        val deleteButton = findViewById<ImageButton>(R.id.deleteButton)
        if (!isMyActivity || activityId == null) {
            deleteButton.visibility = View.GONE
        } else {
            deleteButton.setOnClickListener {
                lifecycleScope.launch {
                    val userActivity = withContext(Dispatchers.IO) {
                        activityViewModel.repository.getActivityById(activityId)?.activity
                    }
                    if (userActivity != null) {
                        activityViewModel.deleteActivity(userActivity)
                        Toast.makeText(this@ActivityDetailActivity, "Тренировка удалена", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(this@ActivityDetailActivity, "Не удалось найти тренировку", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        sessionManager.getCurrentUserId()?.let { userId ->
            userViewModel.observeUser(userId).observe(this) { user ->
                currentUserName = user?.nickname ?: currentUserName
                currentUserAvatar = user?.avatarUri
            }
        }

        val updateSummary: (List<UiFeedback>) -> Unit = { feedbacks ->
            val average = if (feedbacks.isEmpty()) 0f else feedbacks.map { it.rating }.average().toFloat()
            averageRatingText.text = String.format("%.1f", average)
            averageRatingBar.rating = average
            feedbackCount.text = resources.getQuantityString(
                R.plurals.feedback_count,
                feedbacks.size,
                feedbacks.size
            )
        }

        fun updateFeedbackUI(feedbacks: List<UiFeedback>, showFeedbackHint: Boolean = false) {
            val hasUserFeedback = feedbacks.any { it.authorName == currentUserName }
            
            if (hasUserFeedback && showFeedbackHint) {
                commentInput?.isEnabled = false
                findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.commentInput)?.hint = "Вы уже оставили отзыв"
                ratingBar.isEnabled = false
                sendButton.isEnabled = false
                sendButton.text = "Отзыв оставлен"
            } else if (hasUserFeedback) {
                commentInput?.isEnabled = false
                ratingBar.isEnabled = false
                sendButton.isEnabled = false
                sendButton.text = "Отзыв оставлен"
            } else {
                commentInput?.isEnabled = true
                findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.commentInput)?.hint = "Комментарий"
                ratingBar.isEnabled = true
                sendButton.isEnabled = true
                sendButton.text = "Оставить отзыв"
            }
        }

        if (activityId != null) {
            activityViewModel.observeFeedback(activityId).observe(this) { feedbacks ->
                val ui = feedbacks.map {
                    UiFeedback(
                        authorName = it.authorName,
                        authorAvatar = it.authorAvatar,
                        rating = it.rating,
                        comment = it.comment,
                        createdAt = it.createdAt
                    )
                }
                feedbackAdapter.submitList(ui)
                updateSummary(ui)
                updateFeedbackUI(ui, showFeedbackHint = false)
            }
        } else {
            CommunityFeedbackRepository.observeFeedback(activityIdString).observe(this) { list ->
                feedbackAdapter.submitList(list)
                updateSummary(list)
                updateFeedbackUI(list, showFeedbackHint = false)
            }
        }

        sendButton.setOnClickListener {
            val comment = commentInput?.text?.toString()?.trim().orEmpty()
            val rating = ratingBar.rating.toInt()
            
            // Проверка на существующий отзыв
            lifecycleScope.launch {
                val hasFeedback = if (activityId != null) {
                    activityViewModel.hasFeedbackFromAuthor(activityId, currentUserName)
                } else {
                    CommunityFeedbackRepository.hasFeedbackFromAuthor(activityIdString, currentUserName)
                }
                
                if (hasFeedback) {
                    Toast.makeText(this@ActivityDetailActivity, "Вы уже оставили отзыв на эту тренировку", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                
                if (rating == 0) {
                    Toast.makeText(this@ActivityDetailActivity, "Поставьте оценку от 1 до 5", Toast.LENGTH_SHORT).show()
                    return@launch
                }
                if (comment.isEmpty()) {
                    Toast.makeText(this@ActivityDetailActivity, "Напишите короткий отзыв", Toast.LENGTH_SHORT).show()
                    return@launch
                }

                if (activityId != null) {
                    activityViewModel.addFeedback(activityId, currentUserName, currentUserAvatar, rating, comment)
                } else {
                    CommunityFeedbackRepository.addFeedback(
                        activityIdString,
                        UiFeedback(
                            authorName = currentUserName,
                            authorAvatar = currentUserAvatar,
                            rating = rating,
                            comment = comment,
                            createdAt = System.currentTimeMillis()
                        )
                    )
                }

                commentInput?.text?.clear()
                ratingBar.rating = 0f
                
                // Сразу обновляем UI с показом hint после отправки
                findViewById<com.google.android.material.textfield.TextInputLayout>(R.id.commentInput)?.hint = "Вы уже оставили отзыв"
                commentInput?.isEnabled = false
                ratingBar.isEnabled = false
                sendButton.isEnabled = false
                sendButton.text = "Отзыв оставлен"
                
                Toast.makeText(this@ActivityDetailActivity, "Отзыв отправлен", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
