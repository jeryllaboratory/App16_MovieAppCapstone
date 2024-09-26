package com.jeryl.app16_movieappcapstone.feature.detail

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.jeryl.app16_movieappcapstone.R
import com.jeryl.app16_movieappcapstone.databinding.ActivityDetailMovieBinding

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


    private fun initView(){
        val navHostFragment = supportFragmentManager.findFragmentById(
            R.id.detail_nav_host_fragment
        ) as NavHostFragment
        navController = navHostFragment.navController

        observeNavElements(binding, navController)
    }

    private fun observeNavElements(
        binding: ActivityDetailMovieBinding,
        navController: NavController
    ) {
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

    private fun passIntent(){
        val movie = intent.getIntExtra(EXTRA_MOVIE,0)
        val movieIdFromDeepLink = intent?.data?.lastPathSegment?.toIntOrNull()

        val fragment = DetailMovieFragment().apply {

            arguments = Bundle().apply {
                if(movieIdFromDeepLink != null){
                    putInt(EXTRA_MOVIE, movieIdFromDeepLink)
                }else if (movie != 0) {
                    putInt(EXTRA_MOVIE, movie)
                } else {
                    finish()
                    Toast.makeText(this@DetailMovieActivity, "Invalid movie ID", Toast.LENGTH_SHORT).show()
                }
            }
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.detail_nav_host_fragment, fragment)
            .commit()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }


    companion object {
        const val EXTRA_MOVIE = "extra_movie"
    }

}