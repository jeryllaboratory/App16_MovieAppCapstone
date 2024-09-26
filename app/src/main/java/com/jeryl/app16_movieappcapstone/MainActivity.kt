package com.jeryl.app16_movieappcapstone

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.jeryl.app16_movieappcapstone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomTabView()

    }

    private fun initBottomTabView() {
        setSupportActionBar(binding.viewContent.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.nav_host_fragment
        ) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView: BottomNavigationView = binding.mainBottomTabView
        bottomNavigationView.setupWithNavController(navController)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment,
                R.id.favoriteModule,
                R.id.accountFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        observeNavElements(navController)
        bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.favoriteModule -> {
                    loadDynamicFeatureAndNavigate()
                    true
                }

                else -> {
                    navController.navigate(it.itemId)
                    true
                }
            }
        }
    }

    private fun observeNavElements(
        navController: NavController
    ) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> {
                    supportActionBar?.show()
                }

                R.id.favoriteModule -> {
                    supportActionBar?.show()
                }

                R.id.accountFragment -> {
                    supportActionBar?.show()
                }

                else -> {
                    supportActionBar?.hide()

                }
            }
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    private fun loadDynamicFeatureAndNavigate() {
        val splitInstallManager = SplitInstallManagerFactory.create(this)
        val moduleFavorite = "favorite"
        if (splitInstallManager.installedModules.contains(moduleFavorite)) {
            navController.navigate(R.id.favoriteModule)
            return
        } else {
            Snackbar.make(binding.root, getString(R.string.please_install_the_module_first), Snackbar.LENGTH_SHORT).show()
            navController.navigateUp()
        }

    }

}