package com.jeryl.app16_movieappcapstone.search

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jeryl.app16_movieappcapstone.core.data.ResourceState
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieModel
import com.jeryl.app16_movieappcapstone.core.ui.PopularMovieAdapter
import com.jeryl.app16_movieappcapstone.search.databinding.ActivitySearchBinding
import com.jeryl.app16_movieappcapstone.search.di.searchModule
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class SearchActivity : AppCompatActivity() {
    private var _binding: ActivitySearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadKoinModules(searchModule)

        binding.editTextSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                val query = binding.editTextSearch.text.toString()
                if (query.isNotEmpty()) {
                    Toast.makeText(this, "Searching for: $query", Toast.LENGTH_SHORT).show()
                    searchViewModel.searchMovie(query)
                }
                true
            } else {
                false
            }
        }


        binding.editTextSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                lifecycleScope.launch {
                    searchViewModel.searchMovie(s.toString())
                }
            }
        })


        binding.buttonSearch.setOnClickListener {
            val query = binding.editTextSearch.text.toString()
            if (query.isNotEmpty()) {
                searchViewModel.searchMovie(query)
            }
        }

        binding.buttonBack.setOnClickListener {
            finish()
        }

        searchViewModel.searchResult.observe(this) { response ->
            when (response) {
                is ResourceState.Loading -> {
                    binding.viewFlipperSearchMovie.displayedChild = 2
                }
                is ResourceState.Error -> {
                    binding.viewFlipperSearchMovie.displayedChild = 1
                    Snackbar.make(
                        binding.root,
                        getString(R.string.something_went_wrong),
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
                is ResourceState.Success -> {
                    binding.viewFlipperSearchMovie.displayedChild = 0
                    response.data?.let { setupRecyclerView(it) }
                }
            }
        }
    }

    private fun openMovieDetail(movieId: Int) {
        val deepLinkUri = Uri.parse("movieapp://movie/detail/$movieId")
        val intent = Intent(Intent.ACTION_VIEW, deepLinkUri)
        startActivity(intent)
    }

    private fun setupRecyclerView(movies: List<MovieModel>) {
        val layoutManager = GridLayoutManager(this, 2)
        val movieAdapter = PopularMovieAdapter(movies)
        binding.rvSearchMovie.layoutManager = layoutManager
        binding.rvSearchMovie.adapter = movieAdapter
        movieAdapter.setOnItemClickCallback(object : PopularMovieAdapter.OnItemClickListener {
            override fun onItemClicked(data: MovieModel) {
                openMovieDetail(data.id)
            }
        })
    }
}
