package com.grevi.masakapa.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.grevi.masakapa.db.entity.RecipesTable
import com.grevi.masakapa.model.Detail
import com.grevi.masakapa.repository.RepositoryImpl
import com.grevi.masakapa.util.HandlerListener
import com.grevi.masakapa.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl) : ViewModel() {

    var handlerListener : HandlerListener? = null
    private var _listMarkRecipes = MutableLiveData<Resource<MutableList<RecipesTable>>>()
    private lateinit var listFlow : Flow<List<RecipesTable>>
    private var _state = MutableLiveData<Boolean>()

    val listMark : MutableLiveData<Resource<MutableList<RecipesTable>>> get() = _listMarkRecipes
    val state : MutableLiveData<Boolean> get() = _state

    init {
        getMarkRecipes()
        getMarkFLow()
    }

    private fun insertRecipes(detail : Detail, key : String, thumb : String) {
        viewModelScope.launch {
            val recipes = RecipesTable(
                key = key,
                name = detail.name,
                dificulty = detail.dificulty,
                imageThumb = thumb,
                portion = detail.servings,
                times = detail.times
            )
            repositoryImpl.insertRecipes(recipes)
        }
    }

    fun keyChecker(detail : Detail, key : String, thumb : String) {
        viewModelScope.launch {
            val data = repositoryImpl.isExistRecipes(key)
            if (!data) {
                insertRecipes(detail, key, thumb)
                handlerListener?.message("Resep Berhasil Disimpan", data)
            } else {
                Log.v("STATE", "EXISTS $data")
                handlerListener?.message("Resep ${detail.name} Telah Ada Dalam List", data)
            }
        }
    }

    private fun getMarkRecipes() : LiveData<Resource<MutableList<RecipesTable>>> {
        viewModelScope.launch {
            val data = repositoryImpl.getMarkedRecipes()

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

    fun deleteRecipes(recipesTable: RecipesTable) {
        viewModelScope.launch {
            Log.v("DELETE_RECIPES", "Delete : ${recipesTable.name}")

            repositoryImpl.deleteRecipes(recipesTable)
            val data = repositoryImpl.getMarkedRecipes()
            Log.v("DELETE_RECIPES_SIZE", data.size.toString())
            _state.value = data.size == 0
            Log.v("DELETE_RECIPES_STATUS", _state.value.toString())
            delay(1000L)
        }
    }

    private fun getMarkFLow() : Flow<List<RecipesTable>> {
        viewModelScope.launch {
            listFlow = repositoryImpl.getFlowRecipes()
            listFlow.collect { data ->
                Log.v("LIST_FLOW", data.toString())
            }

        }

        return listFlow
    }

}