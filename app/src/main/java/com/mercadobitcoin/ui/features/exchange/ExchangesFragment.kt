package com.mercadobitcoin.ui.features.exchange

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mercadobitcoin.core.base.BaseFragment
import com.mercadobitcoin.core.common.hide
import com.mercadobitcoin.core.common.show
import com.mercadobitcoin.databinding.FragmentExchangesBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExchangesFragment : BaseFragment<FragmentExchangesBinding>() {

    private val viewModel: ExchangesViewModel by viewModels()
    private lateinit var exchangesAdapter: ExchangesAdapter

    override fun inflateViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExchangesBinding {
        return FragmentExchangesBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeData()
    }

    override fun setupViews() {
        setupRecyclerView()
        setupSwipeRefresh()
        //setupRetryButton()
    }

    override fun observeData() {

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateUI(state)
            }
        }
    }


    private fun updateUI(state: ExchangesUiState) {

      when {
          !state.isLoading && !state.exchanges.isNullOrEmpty() -> {
              binding.layoutEmpty.root.hide()
              binding.layoutError.root.hide()
              binding.layoutLoading.root.hide()
              binding.swipeRefresh.show()
              binding.recycler.show()
              exchangesAdapter.submitList(state.exchanges)
              binding.swipeRefresh.isRefreshing = false

          }

           state.isLoading -> {
               binding.layoutEmpty.root.hide()
               binding.layoutError.root.hide()
               binding.layoutLoading.root.show()
               binding.swipeRefresh.show()
               binding.recycler.hide()
           }

           state.error != null -> {
               binding.layoutEmpty.root.hide()
               binding.layoutError.root.show()
               binding.layoutLoading.root.hide()
               binding.swipeRefresh.hide()
               binding.recycler.hide()
           }


       }
    }

    private fun setupRecyclerView() {

        exchangesAdapter = ExchangesAdapter { exchange ->

            Log.e("ExchangeDetailFragment","Open")
            /*supportFragmentManager.beginTransaction()
                .replace(containerId, ExchangeDetailFragment.newInstance(id))
                .addToBackStack(null)
                .commit()*/
        }

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = exchangesAdapter
            setHasFixedSize(true)
        }
    }

    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
        }
    }

    companion object {
        fun newInstance(): ExchangesFragment {
            return ExchangesFragment()
        }
    }

    /*private fun setupRetryButton() {
        binding.layoutError.buttonRetry.setOnClickListener {
            viewModel.refresh()
        }
    }

    override fun observeData() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                updateUI(state)
            }
        }
    }

    private fun updateUI(state: ExchangesUiState) {
        binding.swipeRefresh.isRefreshing = state.isLoading && state.exchanges.isNotEmpty()

        when {
            state.isLoading && state.exchanges.isEmpty() -> {
                showLoading()
            }
            state.error != null && state.exchanges.isEmpty() -> {
                showError(state.error)
            }
            state.exchanges.isEmpty() -> {
                showEmpty()
            }
            else -> {
                showExchanges(state)
            }
        }
    }*/

    /*private fun showLoading() {
        binding.layoutLoading.root.show()
        binding.layoutError.root.hide()
        binding.layoutEmpty.root.hide()
        binding.recyclerViewExchanges.hide()
    }

    private fun showError(error: String) {
        binding.layoutLoading.root.hide()
        binding.layoutError.root.show()
        binding.layoutError.textError.text = error
        binding.layoutEmpty.root.hide()
        binding.recyclerViewExchanges.hide()
    }

    private fun showEmpty() {
        binding.layoutLoading.root.hide()
        binding.layoutError.root.hide()
        binding.layoutEmpty.root.show()
        binding.recyclerViewExchanges.hide()
    }

    private fun showExchanges(state: ExchangesUiState) {
        binding.layoutLoading.root.hide()
        binding.layoutError.root.hide()
        binding.layoutEmpty.root.hide()
        binding.recyclerViewExchanges.show()

        exchangesAdapter.submitList(state.exchanges)

        state.error?.let { error ->
            binding.root.showSnackbar(error)
        }
    }*/

}