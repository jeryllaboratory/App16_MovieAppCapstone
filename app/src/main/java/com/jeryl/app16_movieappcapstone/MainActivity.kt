package com.jeryl.app16_movieappcapstone

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.jeryl.app16_movieappcapstone.core.data.source.remote.network.ApiConstant
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), REQUEST_NOTIFICATION_PERMISSION)
            }
        }
        checkMissingConfig()

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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_NOTIFICATION_PERMISSION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Snackbar.make(binding.root, getString(R.string.notification_permission_granted), Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.root, getString(R.string.notification_permission_denied), Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun checkMissingConfig() {
        val missingProperties = mutableListOf<String>()

        if (ApiConstant.ACCESS_TOKEN == "default_access_token") {
            missingProperties.add("ACCESS_TOKEN")
        }
        if (ApiConstant.BASE_URL == "https://default.api.com/") {
            missingProperties.add("BASE_URL")
        }
        if (ApiConstant.BASE_URL_IMAGE == "https://default.image.com/") {
            missingProperties.add("BASE_URL_IMAGE")
        }

        if (missingProperties.isNotEmpty()) {
            showMissingConfigDialog(missingProperties)
        }
    }

    private fun showMissingConfigDialog(missingProperties: List<String>) {
        val message = "The following configuration properties are missing: ${missingProperties.joinToString(", ")}"
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.missing_configuration))
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> finish() }
            .show()
    }

    companion object {
        private const val REQUEST_NOTIFICATION_PERMISSION = 1
    }

}