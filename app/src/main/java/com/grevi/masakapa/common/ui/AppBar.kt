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

    fun setEnableDarkMod(state : Boolean) {
        if (state) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            false
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            true
        }
    }

    fun setupSearchView(isEnable: Boolean) =
        if (isEnable) binding.llSearch.show() else binding.llSearch.hide()

}