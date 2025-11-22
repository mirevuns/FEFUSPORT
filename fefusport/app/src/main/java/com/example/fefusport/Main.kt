package com.example.fefusport

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class Main : AppCompatActivity() {
    private lateinit var tabLayout: TabLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main)

        tabLayout = findViewById(R.id.tabLayout)
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(bottomNav, navController)

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_activity -> {
                    tabLayout.visibility = View.VISIBLE
                    val bundle = Bundle().apply {
                        putBoolean("is_my_activities", true)
                    }
                    if (navController.currentDestination?.id != R.id.activityListFragment) {
                        navController.navigate(R.id.activityListFragment, bundle)
                    }
                    true
                }
                R.id.nav_profile -> {
                    tabLayout.visibility = View.GONE
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
                R.id.newActivityFragment -> tabLayout.visibility = View.GONE
                R.id.activityListFragment -> {
                    tabLayout.visibility = View.VISIBLE
                    bottomNav.selectedItemId = R.id.nav_activity
                }
                R.id.profileFragment -> {
                    tabLayout.visibility = View.GONE
                    bottomNav.selectedItemId = R.id.nav_profile
                }
            }
        }

        bottomNav.selectedItemId = R.id.nav_activity
    }
}
