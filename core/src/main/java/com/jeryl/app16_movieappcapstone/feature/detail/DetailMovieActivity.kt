package com.jeryl.app16_movieappcapstone.feature.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.jeryl.app16_movieappcapstone.core.R
import com.jeryl.app16_movieappcapstone.core.databinding.ActivityDetailMovieBinding

class DetailMovieActivity : AppCompatActivity() {
    private var _binding: ActivityDetailMovieBinding? = null
    private val binding get() = _binding!!
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        passIntent()
    }

    private fun initView() {
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.detail_nav_host_fragment
        ) as NavHostFragment
        navController = navHostFragment.navController

        observeNavElements(navController)
    }

    private fun observeNavElements(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.detailMovieFragment -> {
                    supportActionBar?.hide()
                }
                else -> {
                    supportActionBar?.hide()
                }
            }
        }
    }

    private fun passIntent() {
        val movie = intent.getIntExtra(EXTRA_MOVIE, 0)
        val movieIdFromDeepLink = intent?.data?.lastPathSegment?.toIntOrNull()

        val movieId = movieIdFromDeepLink ?: movie
        if (movieId != 0) {
            val bundle = Bundle().apply {
                putInt(EXTRA_MOVIE, movieId)
            }
            // Use NavController to navigate to the fragment with the movie ID as an argument
            navController.navigate(R.id.detailMovieFragment, bundle)
        } else {
            finish()
            Toast.makeText(this, "Invalid movie ID", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onDestroy() {
        super.onDestroy()

        // Cleanup NavController and binding
        _binding = null

        // Remove NavHostFragment manually to avoid memory leak
        supportFragmentManager.findFragmentById(R.id.detail_nav_host_fragment)?.let {
            supportFragmentManager.beginTransaction().remove(it).commitAllowingStateLoss()
        }
    }

    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }
}
