package com.example.fefusport

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.fefusport.data.preferences.SessionManager
import com.example.fefusport.ui.welcomes.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class Main : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout
    private var selectedTabPosition: Int = 0 // Сохраняем состояние выбранной вкладки

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sessionManager = SessionManager(this)
        if (sessionManager.getCurrentUserId() == null) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        setContentView(R.layout.main)

        tabLayout = findViewById(R.id.tabLayout)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(bottomNav, navController)

        // Инициализируем tabLayout как скрытый, он будет показан после загрузки фрагмента
        tabLayout.visibility = View.GONE

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_activity -> {
                    // Не показываем tabLayout сразу, он будет показан после загрузки фрагмента
                    tabLayout.visibility = View.GONE
                    val bundle = Bundle().apply {
                        putBoolean("is_my_activities", selectedTabPosition == 0)
                    }
                    if (navController.currentDestination?.id != R.id.activityListFragment) {
                        navController.navigate(R.id.activityListFragment, bundle)
                    } else {
                        // Если уже на этом фрагменте, обновляем аргументы через navigate
                        navController.navigate(R.id.activityListFragment, bundle)
                    }
                    true
                }
                R.id.nav_profile -> {
                    // Не скрываем tabLayout сразу, он будет скрыт после загрузки фрагмента
                    if (navController.currentDestination?.id != R.id.profileFragment) {
                        navController.navigate(R.id.profileFragment)
                    }
                    true
                }
                else -> false
            }
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    selectedTabPosition = it.position // Сохраняем выбранную позицию
                    val bundle = Bundle().apply {
                        putBoolean("is_my_activities", it.position == 0)
                    }
                    navController.navigate(R.id.activityListFragment, bundle)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.newActivityFragment -> {
                    tabLayout.visibility = View.GONE
                }
                R.id.activityListFragment -> {
                    // Показываем tabLayout только после загрузки фрагмента
                    // Используем post, чтобы показать tabLayout после того, как фрагмент полностью загрузится
                    tabLayout.post {
                        tabLayout.visibility = View.VISIBLE
                        // Восстанавливаем выбранную вкладку
                        try {
                            if (tabLayout.tabCount > selectedTabPosition && tabLayout.selectedTabPosition != selectedTabPosition) {
                                tabLayout.getTabAt(selectedTabPosition)?.select()
                            }
                        } catch (e: Exception) {
                            // Игнорируем ошибки при восстановлении вкладки
                        }
                    }
                }
                R.id.profileFragment -> {
                    // Скрываем tabLayout только после загрузки фрагмента профиля
                    // Используем post, чтобы скрыть tabLayout после того, как фрагмент полностью загрузится
                    tabLayout.post {
                        tabLayout.visibility = View.GONE
                    }
                }
            }
        }

    }
}
