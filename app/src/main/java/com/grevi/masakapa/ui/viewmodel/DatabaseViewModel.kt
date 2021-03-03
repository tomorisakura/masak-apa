package com.grevi.masakapa.ui.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.grevi.masakapa.db.entity.RecipesTable
import com.grevi.masakapa.model.Detail
import com.grevi.masakapa.repository.RepositoryImpl
import com.grevi.masakapa.repository.mapper.MapperImpl
import com.grevi.masakapa.util.HandlerListener
import com.grevi.masakapa.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl) : ViewModel() {

    var handlerListener : HandlerListener? = null
    private val _listMarkRecipes = MutableStateFlow<State<MutableList<RecipesTable>>>(State.Data)
    private val _state = MutableLiveData<Boolean>()
    private val _isExist = MutableLiveData<Boolean>()

    init {
        getMarkRecipes()
    }
    val listMark : MutableStateFlow<State<MutableList<RecipesTable>>> get() = _listMarkRecipes

    val state : MutableLiveData<Boolean> get() = _state

    fun insertRecipes(detail : Detail, key : String, thumb : String) {
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

    fun keyChecker(key : String) : LiveData<Boolean> {
        viewModelScope.launch {
            _isExist.postValue(repositoryImpl.isExistRecipes(key))
        }
        return _isExist
    }

    private fun getMarkRecipes(){
        viewModelScope.launch {
            val data = repositoryImpl.getMarkedRecipes()
            if (!data.isNullOrEmpty()) {
                _listMarkRecipes.value = State.Loading()
                try {
                    _listMarkRecipes.value = State.Success(data)
                }catch (e : Exception) {
                    _listMarkRecipes.value = State.Error(e.toString())
                }
            }
        }
    }

    fun deleteRecipes(recipesTable: RecipesTable) {
        viewModelScope.launch {
            Log.v("DELETE_RECIPES", "Delete : ${recipesTable.name}")
            repositoryImpl.deleteRecipes(recipesTable)
            val data = repositoryImpl.getMarkedRecipes()
            _state.value = data.size == 0
        }
    }

}