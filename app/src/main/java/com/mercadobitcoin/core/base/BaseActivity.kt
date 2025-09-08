package com.mercadobitcoin.core.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateViewBinding()
        setContentView(binding.root)
        setupViews()
        observeData()
    }

    abstract fun inflateViewBinding(): VB
    abstract fun setupViews()
    abstract fun observeData()

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}