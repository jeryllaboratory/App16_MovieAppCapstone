package com.jeryl.app16_movieappcapstone.feature.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.jeryl.app16_movieappcapstone.R
import com.jeryl.app16_movieappcapstone.core.data.ResourceState
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieModel
import com.jeryl.app16_movieappcapstone.core.ui.PopularMovieAdapter
import com.jeryl.app16_movieappcapstone.databinding.FragmentHomeBinding
import com.jeryl.app16_movieappcapstone.feature.detail.DetailMovieActivity
import com.jeryl.app16_movieappcapstone.feature.detail.DetailMovieActivity.Companion.EXTRA_MOVIE
import org.koin.androidx.viewmodel.ext.android.viewModel


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(views: View, savedInstanceState: Bundle?) {
        super.onViewCreated(views, savedInstanceState)
        handleMenu()
        viewLifecycleOwnerLiveData.observe(viewLifecycleOwner) {
            binding.viewFlipperPopularMovie.displayedChild = 2
            homeViewModel.movie.observe(viewLifecycleOwner) { response ->
                if (response != null) {
                    when (response) {
                        is ResourceState.Loading -> {
                            binding.viewFlipperPopularMovie.displayedChild = 2
                        }

                        is ResourceState.Success -> {
                            val movies = response.data
                            if (movies != null) {
                                binding.viewFlipperPopularMovie.displayedChild = 0
                                setupRecyclerView(movies)
                            }
                        }

                        is ResourceState.Error -> {
                            Snackbar.make(requireView(),
                                          getString(R.string.something_went_wrong),
                                          Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecyclerView(movies: List<MovieModel>) {
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

    private fun handleMenu() {
        requireActivity().addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.search_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.fragmentSearch -> {
                       loadDynamicFeatureAndNavigate()
                        return true
                    }
                    else -> return false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun loadDynamicFeatureAndNavigate() {
        val splitInstallManager = SplitInstallManagerFactory.create(requireContext())
        val moduleFavorite = "search"
        if (splitInstallManager.installedModules.contains(moduleFavorite)) {
            val uri = Uri.parse("movieapp://search")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
            return
        } else {
            Snackbar.make(binding.root, getString(R.string.please_install_the_module_first), Snackbar.LENGTH_SHORT).show()
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}