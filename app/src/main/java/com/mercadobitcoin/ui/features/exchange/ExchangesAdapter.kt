package com.mercadobitcoin.ui.features.exchange

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.mercadobitcoin.core.common.loadImage
import com.mercadobitcoin.databinding.ItemExchangeBinding
import com.mercadobitcoin.domain.model.Exchange
import com.mercadobitcoin.R
import com.mercadobitcoin.core.common.DateFormatter
import com.mercadobitcoin.core.common.NumberFormatter

class ExchangesAdapter(
    private val onItemClick: (Exchange) -> Unit
) : ListAdapter<Exchange, ExchangesAdapter.ExchangeViewHolder>(ExchangeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExchangeViewHolder {
        val binding = ItemExchangeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExchangeViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ExchangeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExchangeViewHolder(
        private val binding: ItemExchangeBinding,
        private val onItemClick: (Exchange) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(exchange: Exchange) {
            binding.apply {
                // Set data
                exchangeName.text = exchange.name
                spotVolume.text = "Volume: ${NumberFormatter.formatCurrency(exchange.spotVolumeUsd)}"
                dateLaunched.text = "Lançado: ${DateFormatter.format(exchange.dateLaunched)}"

                // Load image
                exchangeLogo.loadImage(url = exchange.logoUrl)

                binding.exchangeLogo.load(exchange.logoUrl) {
                    crossfade(true)
                    placeholder(R.drawable.ic_launcher_foreground)
                    error(R.drawable.ic_launcher_background)
                }

                // Set click listener
                root.setOnClickListener {
                    onItemClick(exchange)
                }
            }
        }
    }

    class ExchangeDiffCallback : DiffUtil.ItemCallback<Exchange>() {
        override fun areItemsTheSame(oldItem: Exchange, newItem: Exchange): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Exchange, newItem: Exchange): Boolean {
            return oldItem == newItem
        }
    }
}