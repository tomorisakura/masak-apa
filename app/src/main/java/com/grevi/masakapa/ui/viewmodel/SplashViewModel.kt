package com.grevi.masakapa.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.grevi.masakapa.repository.Repository
import javax.inject.Inject

class SplashViewModel @Inject constructor(private val repository: Repository) : ViewModel()