package com.grevi.masakapa.ui.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.masakapa.db.entity.Recipes
import com.grevi.masakapa.model.Detail
import com.grevi.masakapa.repos.Remote
import com.grevi.masakapa.util.HandlerListener
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class DatabaseViewModel @ViewModelInject constructor(private val remote: Remote) : ViewModel() {

    var handlerListener : HandlerListener? = null

    private var _listMarkRecipes = MutableLiveData<List<Recipes>>()

    val lisMark : MutableLiveData<List<Recipes>>
    get() = _listMarkRecipes

    init {
        getMarkRecipes()
    }

    private fun insertRecipes(detail : Detail, key : String, thumb : String) {
        viewModelScope.launch {
            val recipes = Recipes(
                key = key,
                name = detail.name,
                dificulty = detail.dificulty,
                imageThumb = thumb,
                portion = detail.servings,
                times = detail.times
            )
            Log.v("INSERT", recipes.key)
            remote.insertRecipes(recipes)
        }
    }

    fun keyChecker(detail : Detail, key : String, thumb : String) {
        viewModelScope.launch {
            val data = remote.isExistRecipes(key)
            if (!data) {
                insertRecipes(detail, key, thumb)
                handlerListener?.message("Resep Berhasil Disimpan", data)
            } else {
                Log.v("STATE", "EXISTS $data")
                handlerListener?.message("Resep ${detail.name} Telah Ada Dalam List", data)
            }
        }
    }

    private fun getMarkRecipes() : LiveData<List<Recipes>> {
        viewModelScope.launch {
            val data = remote.getMarkedRecipes()
            _listMarkRecipes.postValue(data)
        }
        return _listMarkRecipes
    }
}