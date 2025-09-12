/*
package com.mercadobitcoin.ui.features


import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.mercadobitcoin.ui.features.exchange.ExchangesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NavHostActivity : AppCompatActivity() {

    private var containerId: Int = View.generateViewId()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val container = FrameLayout(this).apply { id = containerId }
        setContentView(container, ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))

        if (savedInstanceState == null) {

            supportFragmentManager.beginTransaction()
                .replace(containerId, ExchangesFragment.newInstance())
                .commit()
        }
    }

    fun openExchangeDetail(id: String) {
       */
/* supportFragmentManager.beginTransaction()
            .replace(containerId, ExchangeDetailFragment.newInstance(id))
            .addToBackStack(null)
            .commit()*//*

    }
}*/
