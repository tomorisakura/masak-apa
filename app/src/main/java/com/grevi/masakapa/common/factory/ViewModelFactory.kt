package com.grevi.masakapa.common.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.grevi.masakapa.repository.Repository
import javax.inject.Inject

class ViewModelFactory @Inject constructor(private val repository: Repository) :ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(Repository::class.java).newInstance(repository)
    }

}