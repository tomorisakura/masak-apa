package com.grevi.masakapa.common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import com.grevi.masakapa.databinding.AppBarBinding
import com.grevi.masakapa.util.hide
import com.grevi.masakapa.util.show

class AppBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
): CardView(context, attrs) {

    private val binding by lazy {
        AppBarBinding.inflate(LayoutInflater.from(context), this, true)
    }

    init {
        setupView()
    }

    private fun setupView() = with(binding) {
        root
    }

    fun setSearchViewClicked(listener: () -> Unit) = with(binding) {
        llSearch.setOnClickListener { listener() }
    }

    fun onFavoriteClickListener(listener: () -> Unit) = with(binding) {
        icFavorite.setOnClickListener { listener() }
    }

    fun onDayNightClickListener(listener: () -> Unit) = with(binding) {
        icDayLight.setOnClickListener { listener() }
    }

    fun onBackClickListener(listener: () -> Unit) = with(binding) {
        btnBack.setOnClickListener { listener() }
    }

    fun setupSearchView(isEnable: Boolean) = with(binding) {
        if (isEnable) {
            llSearch.show()
            icLogo.show()
            btnBack.hide()
        } else {
            llSearch.hide()
            icLogo.hide()
            btnBack.show()
        }
    }

}