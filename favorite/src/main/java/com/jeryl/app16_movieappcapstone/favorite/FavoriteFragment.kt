package com.jeryl.app16_movieappcapstone.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieModel
import com.jeryl.app16_movieappcapstone.core.ui.PopularMovieAdapter
import com.jeryl.app16_movieappcapstone.favorite.databinding.FragmentFavoriteBinding
import com.jeryl.app16_movieappcapstone.favorite.di.favoriteModule
import com.jeryl.app16_movieappcapstone.feature.detail.DetailMovieActivity
import com.jeryl.app16_movieappcapstone.feature.detail.DetailMovieActivity.Companion.EXTRA_MOVIE
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.context.loadKoinModules

class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val favoriteViewModel: FavoriteViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding  = FragmentFavoriteBinding.inflate(inflater, container, false)
        return  binding.root    }

    override fun onViewCreated(views: View, savedInstanceState: Bundle?) {
        super.onViewCreated(views, savedInstanceState)
        loadKoinModules(favoriteModule)

        viewLifecycleOwnerLiveData.observe(viewLifecycleOwner) {
            binding.viewflipperFavoriteMovie.displayedChild = 2
            favoriteViewModel.favoriteMovie.observe(viewLifecycleOwner) { response ->
                if (response.isEmpty()) {
                    binding.viewflipperFavoriteMovie.displayedChild = 1
                } else {
                    binding.viewflipperFavoriteMovie.displayedChild = 0
                    setupRecyclerView(response)
                }
            }
        }
    }

    private fun setupRecyclerView(movies: List<MovieModel>){
        val layoutManager = GridLayoutManager(requireContext(), 2)
        val movieAdapter = PopularMovieAdapter(movies)
        movieAdapter.setOnItemClickCallback(object : PopularMovieAdapter.OnItemClickListener {
            override fun onItemClicked(data: MovieModel) {
                val intent = Intent(activity, DetailMovieActivity::class.java)
                intent.putExtra(EXTRA_MOVIE, data.id)
                startActivity(intent)
            }
        })
        binding.rvMoviesNowShowing.layoutManager = layoutManager
        binding.rvMoviesNowShowing.adapter = movieAdapter

    }


    override fun onDestroyView() {
        binding.viewflipperFavoriteMovie.stopFlipping()
        _binding = null
        super.onDestroyView()

    }

}