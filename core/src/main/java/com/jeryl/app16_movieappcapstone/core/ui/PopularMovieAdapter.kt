package com.jeryl.app16_movieappcapstone.core.ui

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jeryl.app16_movieappcapstone.core.R
import com.jeryl.app16_movieappcapstone.core.databinding.ViewItemMovieBinding
import com.jeryl.app16_movieappcapstone.core.domain.model.MovieModel


/**
 * Created by Jery I D Lenas on 10/09/2024.
 * Contact : jerylenas@gmail.com
 */

class PopularMovieAdapter(private val popularMovies: List<MovieModel>) : RecyclerView.Adapter<PopularMovieAdapter.ListViewHolder>() {
    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickListener) {
        this.onItemClickListener = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ViewItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    inner class ListViewHolder(private var binding: ViewItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: MovieModel) {
            val rating = movie.voteAverage / 2
            with(binding){
               Glide.with(itemView.context).load(movie.getPosterUrl())
                    .error(R.drawable.ic_broken_image)
                    .placeholder(R.drawable.ic_loading)
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }
                        override fun onResourceReady(
                            resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean
                        ): Boolean {
                            imageviewItemmovieImage.scaleType = android.widget.ImageView.ScaleType.FIT_XY
                            return false
                        }

                    })
                    .into(imageviewItemmovieImage)
                textviewItemmovieTitle.text = movie.title
                textViewItemmovieDescription.text = itemView.context.getString(
                    R.string.movie_item_description,
                    movie.originalLanguage, movie.releaseDate)
                ratingbarItemmovieRating.rating = rating.toFloat()
                textViewItemmovieLanguage.text = movie.originalLanguage
                textViewItemmovieLanguage.text = movie.originalLanguage.uppercase()
            }
        }
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val movie = popularMovies[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClicked(movie)
        }
    }

    override fun getItemCount(): Int  = popularMovies.size

    interface OnItemClickListener {
        fun onItemClicked(data: MovieModel)
    }
    private fun MovieModel.getPosterUrl(): String {
        return "https://image.tmdb.org/t/p/w500$posterPath"
    }
}

