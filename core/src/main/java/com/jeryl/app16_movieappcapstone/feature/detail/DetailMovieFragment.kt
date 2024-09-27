package com.jeryl.app16_movieappcapstone.feature.detail

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.addCallback
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
import com.jeryl.app16_movieappcapstone.core.R
import com.jeryl.app16_movieappcapstone.core.data.ResourceState
import com.jeryl.app16_movieappcapstone.core.data.source.remote.network.ApiConstant.BASE_URL_IMAGE
import com.jeryl.app16_movieappcapstone.core.databinding.FragmentDetailMovieBinding
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieDetailModel
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieModel
import com.jeryl.app16_movieappcapstone.core.domain.model.ProductionCompaniesItem
import com.jeryl.app16_movieappcapstone.core.ui.ProductionCompanyAdapter
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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            requireActivity().finish()
        }

        movie?.let {
            detailMovieViewModel.getFavoriteById(it).observe(viewLifecycleOwner) { response ->
                isFavorite = response.isFavorite
                setStatusFavorite(isFavorite)
                setIntentData(response)
            }

            detailMovieViewModel.getDetailMovie(it).observe(viewLifecycleOwner) { response ->
                when (response) {
                    is ResourceState.Loading -> displayedChildView(0)
                    is ResourceState.Success -> {
                        binding.viewFlipperMovieDetail.displayedChild = 1
                        displayedChildView(1)
                        binding.buttonFavoriteList.isEnabled = true

                        response.data?.let { detail ->
                            setupData(detail)
                            detail.productionCompanies?.let { companies ->
                                setupRecylerView(companies)
                            }
                        }
                    }

                    is ResourceState.Error -> displayedChildView(2)
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
            maxScrollSize = appBarLayout?.totalScrollRange ?: 0
        }

        val percentage = Math.abs(verticalOffset).toFloat() / maxScrollSize
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

    private fun setupData(detail: MovieDetailModel) {
        binding.textViewMovieDetailTitle.text = detail.title
        binding.textViewMovieDetailTitleToolbar.text = detail.title

        val hours = detail.runtime / 60
        val minutes = detail.runtime % 60

        binding.textViewMovieDetailRuntime.text = StringBuilder("${hours}h ${minutes}m")
        binding.textViewMovieDetailGenres.text = detail.genres.joinToString { it.name }
        binding.textViewMovieDetailGenresToolbar.text = detail.genres.joinToString { it.name }
        binding.textviewMovieDetailReleaseDate.text = detail.releaseDate

        val ratingValue = detail.voteAverage.toFloat() / 2
        binding.ratingBarMovieDetailRating.rating = ratingValue
        binding.textviewMovieDetailRating.text = getString(R.string.rating_text_label, ratingValue, 5)
        binding.textviewMovieDetailOverview.text = detail.overview

        Glide.with(requireContext()).load(detail.getPosterUrl())
            .error(R.drawable.ic_broken_image)
            .placeholder(R.drawable.ic_loading)
            .listener(glideListener(binding.ivBackdrop))
            .into(binding.ivPoster)

        Glide.with(requireContext()).load(detail.getBackdropUrl())
            .fitCenter()
            .placeholder(R.drawable.ic_loading)
            .error(R.drawable.ic_broken_image)
            .listener(glideListener(binding.ivBackdrop))
            .into(binding.ivBackdrop)
    }

    private fun glideListener(imageView: ImageView) = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            imageView.scaleType = ImageView.ScaleType.CENTER_INSIDE
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            imageView.scaleType = ImageView.ScaleType.FIT_XY
            return false
        }
    }

    private fun setupRecylerView(productionCompanies: List<ProductionCompaniesItem>) {
        val movieAdapter = ProductionCompanyAdapter(productionCompanies)
        binding.recyclerviewMovieDetailCompany.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = movieAdapter
        }
        movieAdapter.setOnItemClickCallback(object : ProductionCompanyAdapter.OnItemClickListener {
            override fun onItemClicked(data: ProductionCompaniesItem) {}
        })
    }

    private fun updateFavoriteButton() {
        binding.buttonFavoriteList.apply {
            text = if (isFavorite) getString(R.string.remove_from_favorite_label) else getString(R.string.add_to_favorite_label)
            setCompoundDrawablesWithIntrinsicBounds(if (isFavorite) R.drawable.ic_baseline_close else R.drawable.ic_baseline_add, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(requireContext(), if (isFavorite) R.color.primaryColor else R.color.successColor))
        }
    }

    private fun setStatusFavorite(isFavorite: Boolean) {
        this.isFavorite = isFavorite
        updateFavoriteButton()
        binding.buttonSame.setImageResource(if (isFavorite) R.drawable.ic_baseline_favorite else R.drawable.ic_baseline_favorite_border)
    }

    private fun setIntentData(movie: MovieModel) {
        binding.buttonFavoriteList.setOnClickListener {
            isFavorite = !isFavorite
            detailMovieViewModel.setFavoriteTourism(movie, isFavorite)
            setStatusFavorite(isFavorite)
        }
        binding.buttonSame.setOnClickListener {
            isFavorite = !isFavorite
            detailMovieViewModel.setFavoriteTourism(movie, isFavorite)
            setStatusFavorite(isFavorite)
        }
    }

    private fun displayedChildView(number: Int) {
        binding.apply {
            viewFlipperMovieDetail.displayedChild = number
            viewFlipperHeaderDetail.displayedChild = number
        }
    }

    override fun onDestroyView() {
        binding.appbar.removeOnOffsetChangedListener(this)

        Glide.with(this).clear(binding.ivPoster)
        Glide.with(this).clear(binding.ivBackdrop)

        _binding = null
        super.onDestroyView()
    }
}

fun View.applyScaleAndAlpha(scale: Float, alpha: Float) {
    this.scaleX = scale
    this.scaleY = scale
    this.alpha = alpha
}

fun MovieDetailModel.getPosterUrl(): String = BASE_URL_IMAGE + posterPath
fun MovieDetailModel.getBackdropUrl(): String = BASE_URL_IMAGE + backdropPath
