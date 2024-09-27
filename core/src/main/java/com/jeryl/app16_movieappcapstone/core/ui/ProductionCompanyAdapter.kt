package com.jeryl.app16_movieappcapstone.core.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.jeryl.app16_movieappcapstone.core.databinding.ViewItemCompanyBinding
import com.jeryl.app16_movieappcapstone.core.domain.model.ProductionCompaniesItem

/**
 * Created by Jery I D Lenas on 10/09/2024.
 * Contact : jerylenas@gmail.com
 */

class ProductionCompanyAdapter(private val companies: List<ProductionCompaniesItem>) : RecyclerView.Adapter<ProductionCompanyAdapter.ListViewHolder>() {
    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickListener) {
        this.onItemClickListener = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = ViewItemCompanyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    class ListViewHolder(var binding: ViewItemCompanyBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val company = companies[position]
        with(holder.binding){
            Glide.with(holder.itemView.context).load(company.getLogoUrl())
                .into(imageViewCompanyItemLogo)
            textviewCompanyItemTitle.text = company.name
            textviewCompanyItemCountry.text = company.originCountry
        }
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClicked(company)
        }
    }

    override fun getItemCount(): Int  = companies.size

    interface OnItemClickListener {
        fun onItemClicked(data: ProductionCompaniesItem)
    }


}