package com.jeryl.app16_movieappcapstone.feature.detail

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.google.android.material.appbar.AppBarLayout
import com.jeryl.app16_movieappcapstone.R
import com.jeryl.app16_movieappcapstone.core.data.ResourceState
import com.jeryl.app16_movieappcapstone.core.data.source.remote.network.ApiConstant.BASE_URL_IMAGE
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieDetailModel
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieModel
import com.jeryl.app16_movieappcapstone.core.domain.model.ProductionCompaniesItem
import com.jeryl.app16_movieappcapstone.core.ui.ProductionCompanyAdapter
import com.jeryl.app16_movieappcapstone.databinding.FragmentDetailMovieBinding
import com.jeryl.app16_movieappcapstone.feature.detail.DetailMovieActivity.Companion.EXTRA_MOVIE
import org.koin.androidx.viewmodel.ext.android.viewModel


class DetailMovieFragment : Fragment(), AppBarLayout.OnOffsetChangedListener {
    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!
    private var isFavorite = false
    private var maxScrollSize: Int = 0
    private lateinit var window: Window
    private val detailMovieViewModel: DetailMovieViewModel by viewModel()
    private var movie: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = it.getInt(EXTRA_MOVIE)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailMovieBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.appbar.addOnOffsetChangedListener(this)
        window = requireActivity().window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        updateFavoriteButton()

        movie?.let {

            detailMovieViewModel.getFavoriteById(movie ?: 0).observe(viewLifecycleOwner) { response ->
                isFavorite = response.isFavorite
                setStatusFavorite(isFavorite)
                setIntentData(response)
            }

            detailMovieViewModel
                .getDetailMovie(it).observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is ResourceState.Loading -> {
                            displayedChildView(0)

                        }

                        is ResourceState.Success -> {
                            binding.viewFlipperMovieDetail.displayedChild = 1
                            displayedChildView(1)
                            binding.buttonFavoriteList.isEnabled = true

                            if (response.data != null) {
                                setupData(response.data)
                                if (response.data?.productionCompanies != null) {
                                    setupRecylerView(response.data?.productionCompanies)
                                }
                            }
                        }

                        is ResourceState.Error -> {
                            displayedChildView(2)
                        }
                    }

                }
        }


        binding.toolbar.setNavigationOnClickListener {
            requireActivity().finish()
        }


        binding.buttonPlay.setOnClickListener {
            Toast.makeText(requireContext(), "Play button clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout?, verticalOffset: Int) {
        if (maxScrollSize == 0) {
            if (appBarLayout != null) {
                maxScrollSize = appBarLayout.totalScrollRange
            }
        }

        val percentage = Math.abs(verticalOffset).toFloat() / maxScrollSize.toFloat()

        val scale = 1f - percentage * 0.7f
        binding.ivPoster.applyScaleAndAlpha(scale, scale)

        val scaleButton = percentage * 1f
        if (scale < 0.4) {
            binding.buttonSame.applyScaleAndAlpha(scaleButton, scaleButton)
            binding.textViewMovieDetailTitleToolbar.applyScaleAndAlpha(scaleButton, scaleButton)
            binding.textViewMovieDetailGenresToolbar.applyScaleAndAlpha(scaleButton, scaleButton)
            window.statusBarColor = ResourcesCompat.getColor(resources, R.color.primaryColor, null)

        } else {
            binding.buttonSame.applyScaleAndAlpha(0f, 0f)
            binding.textViewMovieDetailTitleToolbar.applyScaleAndAlpha(0f, 0f)
            binding.textViewMovieDetailGenresToolbar.applyScaleAndAlpha(0f, 0f)

            window.statusBarColor = Color.BLACK
        }
    }

    private fun setupData(detail: MovieDetailModel?) {
        if (detail == null) return

        binding.textViewMovieDetailTitle.text = detail.title
        binding.textViewMovieDetailTitleToolbar.text = detail.title

        val hours = detail.runtime / 60
        val minutes = detail.runtime % 60
        Log.d("DetailMovieFragment", "setupData: ${detail.genres} ")
        binding.textViewMovieDetailRuntime.text = StringBuilder("${hours}h ${minutes}m")
        binding.textViewMovieDetailGenres.text = detail.genres.joinToString { it.name }
        binding.textViewMovieDetailGenresToolbar.text = detail.genres.joinToString { it.name }
        binding.textviewMovieDetailReleaseDate.text = detail.releaseDate

        val ratingValue = detail.voteAverage.toFloat() / 2
        binding.ratingBarMovieDetailRating.rating = (detail.voteAverage.toFloat() / 2)
        binding.textviewMovieDetailRating.text = getString(R.string.rating_text_label, ratingValue, 5)
        binding.textviewMovieDetailOverview.text = detail.overview

        Glide.with(requireContext()).load(detail.getPosterUrl())
            .error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.ic_loading)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    binding.ivBackdrop.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean
                ): Boolean {
                    binding.ivBackdrop.scaleType = ImageView.ScaleType.FIT_XY
                    return false
                }
            })
            .into(binding.ivPoster)
        Glide.with(requireContext()).load(detail.getBackdropUrl())
            .fitCenter()
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_broken_image)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                    binding.ivBackdrop.scaleType = ImageView.ScaleType.CENTER_INSIDE
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean
                ): Boolean {
                    binding.ivBackdrop.scaleType = ImageView.ScaleType.FIT_XY
                    return false
                }
            })
            .into(binding.ivBackdrop)
    }

    private fun setupRecylerView(productionCompanies: List<ProductionCompaniesItem>?) {
        if (productionCompanies == null) return
        val movieAdapter = ProductionCompanyAdapter(productionCompanies)
        val layoutManager = LinearLayoutManager(requireContext())
        movieAdapter.setOnItemClickCallback(object : ProductionCompanyAdapter.OnItemClickListener {
            override fun onItemClicked(data: ProductionCompaniesItem) {}
        })
        binding.recyclerviewMovieDetailCompany.layoutManager = layoutManager
        binding.recyclerviewMovieDetailCompany.adapter = movieAdapter
    }

    private fun updateFavoriteButton() {
        with(binding) {

        }
        binding.buttonFavoriteList.apply {
            if (isFavorite) {
                text = getString(R.string.remove_from_favorite_label)
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_close, 0, 0, 0)
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primaryColor))
            } else {
                text = getString(R.string.add_to_favorite_label)
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_add, 0, 0, 0)
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.successColor))
            }

        }
    }


    private fun setStatusFavorite(isFavorite: Boolean) {
        if (isFavorite) {
            binding.buttonFavoriteList.apply {
                text = getString(R.string.remove_from_favorite_label)
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_close, 0, 0, 0)
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primaryColor))
            }

            binding.buttonSame.apply {
                setImageResource(R.drawable.ic_baseline_favorite)
            }
        } else {
            binding.buttonFavoriteList.apply {
                text = getString(R.string.add_to_favorite_label)
                setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_baseline_add, 0, 0, 0)
                setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.successColor))
            }
            binding.buttonSame.apply {
                setImageResource(R.drawable.ic_baseline_favorite_border)
            }
        }
    }

    private fun setIntentData(movie: MovieModel?) {
        var statusFavorite = movie?.isFavorite == true
        binding.buttonFavoriteList.setOnClickListener {
            statusFavorite = !statusFavorite
            if (movie != null) {
                detailMovieViewModel.setFavoriteTourism(movie, statusFavorite)
            }
            setStatusFavorite(statusFavorite)
        }
        binding.buttonSame.setOnClickListener {
            statusFavorite = !statusFavorite
            if (movie != null) {
                detailMovieViewModel.setFavoriteTourism(movie, statusFavorite)
            }
            setStatusFavorite(statusFavorite)
        }
    }

    private fun displayedChildView(number: Int) {
        binding.apply {
            viewFlipperMovieDetail.displayedChild = number
            viewFlipperHeaderDetail.displayedChild = number
        }
    }
}

fun View.applyScaleAndAlpha(scale: Float, alpha: Float) {
    this.scaleX = scale
    this.scaleY = scale
    this.alpha = alpha
}

fun MovieDetailModel.getPosterUrl(): String = BASE_URL_IMAGE + posterPath
fun MovieDetailModel.getBackdropUrl(): String = BASE_URL_IMAGE + backdropPath
