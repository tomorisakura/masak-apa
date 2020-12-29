package com.grevi.masakapa.ui.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.grevi.masakapa.db.entity.Recipes
import com.grevi.masakapa.model.Detail
import com.grevi.masakapa.repos.Remote
import com.grevi.masakapa.util.HandlerListener
import com.grevi.masakapa.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class DatabaseViewModel @ViewModelInject constructor(private val remote: Remote) : ViewModel() {

    var handlerListener : HandlerListener? = null
    private var _listMarkRecipes = MutableLiveData<Resource<MutableList<Recipes>>>()

    private lateinit var listFlow : Flow<List<Recipes>>

    private var _state = MutableLiveData<Boolean>()

    val listMark : MutableLiveData<Resource<MutableList<Recipes>>>
    get() = _listMarkRecipes

    val state : MutableLiveData<Boolean>
    get() = _state

    init {
        getMarkRecipes()
        getMarkFLow()
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

    private fun getMarkRecipes() : LiveData<Resource<MutableList<Recipes>>> {
        viewModelScope.launch {
            val data = remote.getMarkedRecipes()

            if (!data.isNullOrEmpty()) {
                _listMarkRecipes.postValue(Resource.loading(null, "Load"))
                try {
                    _listMarkRecipes.postValue(Resource.success(data))
                    delay(1000L)
                }catch (e : Exception) {
                    _listMarkRecipes.postValue(Resource.error(null, e.message))
                }
            } else {
                Log.v("STATE_DATA", "empty list")
            }
        }

        return _listMarkRecipes
    }

    fun deleteRecipes(recipes: Recipes) {
        viewModelScope.launch {
            Log.v("DELETE_RECIPES", "Delete : ${recipes.name}")

            remote.deleteRecipes(recipes)
            val data = remote.getMarkedRecipes()
            Log.v("DELETE_RECIPES_SIZE", data.size.toString())
            _state.value = data.size == 0
            Log.v("DELETE_RECIPES_STATUS", _state.value.toString())
            delay(1000L)
        }
    }

    private fun getMarkFLow() : Flow<List<Recipes>> {
        viewModelScope.launch {
            listFlow = remote.getFlowRecipes()
            listFlow.collect { data ->
                Log.v("LIST_FLOW", data.toString())
            }

        }

        return listFlow
    }

}