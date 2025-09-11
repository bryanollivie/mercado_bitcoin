/*
package com.mercadobitcoin.ui.features.exchange

import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercadobitcoin.core.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExchangeDetailActivity : BaseActivity<ActivityExchangeDetailBinding>() {

    private val viewModel: ExchangeDetailViewModel by viewModels()
    private lateinit var currenciesAdapter: CurrenciesAdapter

    companion object {
        const val EXTRA_EXCHANGE_ID = "exchange_id"
        const val EXTRA_EXCHANGE_NAME = "exchange_name"
    }

    override fun inflateViewBinding(): ActivityExchangeDetailBinding {
        return ActivityExchangeDetailBinding.inflate(layoutInflater)
    }

    override fun setupViews() {
        setupToolbar()
        setupRecyclerView()
        setupRetryButton()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = intent.getStringExtra(EXTRA_EXCHANGE_NAME) ?: "Exchange Detail"
        }

        binding.toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        currenciesAdapter = CurrenciesAdapter()

        binding.recyclerViewCurrencies.apply {
            layoutManager = LinearLayoutManager(this@ExchangeDetailActivity)
            adapter = currenciesAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupRetryButton() {
        binding.layoutError.buttonRetry.setOnClickListener {
            viewModel.retry()
        }
    }

    override fun observeData() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateUI(state)
            }
        }
    }

    private fun updateUI(state: ExchangeDetailUiState) {
        // Update detail section
        when {
            state.isLoadingDetail -> {
                binding.layoutDetailLoading.root.show()
                binding.layoutDetailContent.hide()
                binding.layoutError.root.hide()
            }
            state.detailError != null -> {
                binding.layoutDetailLoading.root.hide()
                binding.layoutDetailContent.hide()
                binding.layoutError.root.show()
                binding.layoutError.textError.text = state.detailError
            }
            state.exchangeDetail != null -> {
                binding.layoutDetailLoading.root.hide()
                binding.layoutDetailContent.show()
                binding.layoutError.root.hide()
                updateDetailContent(state.exchangeDetail)
            }
        }

        // Update currencies section
        when {
            state.isLoadingCurrencies -> {
                binding.progressCurrencies.show()
                binding.recyclerViewCurrencies.hide()
                binding.textNoCurrencies.hide()
            }
            state.currencies.isEmpty() -> {
                binding.progressCurrencies.hide()
                binding.recyclerViewCurrencies.hide()
                binding.textNoCurrencies.show()
            }
            else -> {
                binding.progressCurrencies.hide()
                binding.recyclerViewCurrencies.show()
                binding.textNoCurrencies.hide()
                currenciesAdapter.submitList(state.currencies)
            }
        }

        state.currenciesError?.let { error ->
            binding.root.showSnackbar(error)
        }
    }

    private fun updateDetailContent(detail: ExchangeDetail) {
        binding.apply {
            imageDetailLogo.loadImage(detail.logoUrl)
            textDetailName.text = detail.name
            textDetailId.text = "ID: ${detail.id}"
            textDetailDescription.text = detail.description ?: "No description available"
            textDetailWebsite.text = detail.websiteUrl ?: "-"
            textDetailMakerFee.text = NumberFormatter.formatPercent(detail.makerFee)
            textDetailTakerFee.text = NumberFormatter.formatPercent(detail.takerFee)
            textDetailDateLaunched.text = DateFormatter.format(detail.dateLaunched)
        }
    }
}*/
