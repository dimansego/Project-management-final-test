package com.example.projectmanagement

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.projectmanagement.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Use DataBinding as per slides pattern
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        
        setSupportActionBar(binding.toolbar)
        
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        // Set up ActionBar with NavController
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.projectsFragment,
                R.id.meetingsFragment,
                R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        
        // Set up BottomNavigationView with NavController
        // Only show bottom nav for main graph destinations
        navController.addOnDestinationChangedListener { _, destination, _ ->
            val mainDestinations = setOf(
                R.id.homeFragment,
                R.id.projectsFragment,
                R.id.meetingsFragment,
                R.id.profileFragment
            )
            binding.bottomNavigationView.visibility = 
                if (mainDestinations.contains(destination.id)) {
                    View.VISIBLE
                } else {
                    View.GONE
                }
            
            // Show menu only on home fragment
            invalidateOptionsMenu()
        }
        
        binding.bottomNavigationView.setupWithNavController(navController)
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val currentDestination = navHostFragment.navController.currentDestination
        
        // Only show menu on home fragment
        if (currentDestination?.id == R.id.homeFragment) {
            menuInflater.inflate(R.menu.home_action_bar, menu)
        }
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        val navController = navHostFragment.navController
        
        return when (item.itemId) {
            R.id.action_add_project -> {
                // Navigate to create project or show dialog
                // For now, just show a toast - will be implemented
                android.widget.Toast.makeText(this, "Add Project", android.widget.Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_search -> {
                // Handle search
                android.widget.Toast.makeText(this, "Search", android.widget.Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        return navHostFragment.navController.navigateUp() || super.onSupportNavigateUp()
    }
}
