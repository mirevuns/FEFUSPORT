package com.example.fefusport.ui

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.fefusport.R
import com.example.fefusport.ui.viewmodel.ActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ActivityDetailActivity : AppCompatActivity() {
    private val activityViewModel: ActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val activityId = intent.getStringExtra("activity_id")?.toIntOrNull()
        val type = intent.getStringExtra("activity_type") ?: ""
        val distance = intent.getStringExtra("activity_distance") ?: ""
        val duration = intent.getStringExtra("activity_duration") ?: ""
        val startTime = intent.getStringExtra("activity_start_time") ?: ""
        val endTime = intent.getStringExtra("activity_end_time") ?: ""
        val isMyActivity = intent.getBooleanExtra("is_my_activity", true)

        findViewById<TextView>(R.id.activityType).text = type
        findViewById<TextView>(R.id.activityDistance).text = distance
        findViewById<TextView>(R.id.activityDuration).text = duration
        findViewById<TextView>(R.id.activityTime).text = "Старт $startTime — Финиш $endTime"

        findViewById<ImageButton>(R.id.backButton).setOnClickListener {
            finish()
        }

        val deleteButton = findViewById<ImageButton>(R.id.deleteButton)
        if (!isMyActivity) {
            deleteButton.visibility = android.view.View.GONE
        } else {
            deleteButton.setOnClickListener {
                if (activityId != null) {
                    CoroutineScope(Dispatchers.Main).launch {
                        val userActivity = withContext(Dispatchers.IO) {
                            activityViewModel.repository.getActivityById(activityId)
                        }
                        if (userActivity != null) {
                            activityViewModel.deleteActivity(userActivity)
                            Toast.makeText(this@ActivityDetailActivity, "Активность удалена", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@ActivityDetailActivity, "Не удалось найти активность", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Некорректный id активности", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
