package com.grevi.masakapa.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.masakapa.db.entity.RecipesTable
import com.grevi.masakapa.model.Detail
import com.grevi.masakapa.repository.RepositoryImpl
import com.grevi.masakapa.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl) : ViewModel() {
    private val _state = MutableLiveData<Boolean>()
    private val _isExist = MutableLiveData<Boolean>()
    private val _markList = MutableLiveData<State<MutableList<RecipesTable>>>()

    val listMarkData : MutableLiveData<State<MutableList<RecipesTable>>> get() = _markList
    val state : MutableLiveData<Boolean> get() = _state

    init {
        getMarkRecipesData()
    }

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

    private fun getMarkRecipesData() {
        viewModelScope.launch {
            val data = repositoryImpl.getMarkedRecipes()
            _markList.postValue(State.Loading())
            try {
                _markList.postValue(State.Success(data))
            }catch (e : Exception) {
                _markList.postValue(State.Error(e.toString()))
            }
        }
    }

    fun deleteRecipes(recipesTable: RecipesTable) {
        viewModelScope.launch {
            Log.v("DELETE_RECIPES", "Delete : ${recipesTable.name}")
            repositoryImpl.deleteRecipes(recipesTable)
        }
    }

}