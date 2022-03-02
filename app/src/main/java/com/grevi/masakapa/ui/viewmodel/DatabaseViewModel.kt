package com.grevi.masakapa.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.masakapa.data.local.entity.DetailTable
import com.grevi.masakapa.data.local.entity.RecipeFavorite
import com.grevi.masakapa.model.Detail
import com.grevi.masakapa.repository.Repository
import com.grevi.masakapa.common.state.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DatabaseViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _state = MutableLiveData<Boolean>()
    private val _isExist = MutableLiveData<Boolean>()
    private val _recipesFlow = MutableStateFlow<State<List<RecipeFavorite>>>(State.Data)

    private val _detail = MutableStateFlow<State<DetailTable>>(State.Data)

    val state : MutableLiveData<Boolean> get() = _state
    val recipesBucket : MutableStateFlow<State<List<RecipeFavorite>>> get() = _recipesFlow

    init {
        getMarkRecipes()
    }

    fun insertRecipes(detail : Detail, key : String, thumb : String) {
        viewModelScope.launch(Dispatchers.IO) {
            val favorite = RecipeFavorite(
                key = key,
                name = detail.name,
                dificulty = detail.dificulty,
                imageThumb = thumb,
                portion = detail.servings,
                times = detail.times
            )
            repository.insertFavorite(favorite)
        }
    }

    fun keyChecker(key : String) : LiveData<Boolean> {
        viewModelScope.launch(Dispatchers.IO) {
            _isExist.postValue(repository.isFavoriteExists(key))
        }
        return _isExist
    }

    fun deleteRecipes(favorite: RecipeFavorite) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteFavorite(favorite)
        }
    }

    private fun getMarkRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFlowFavorite().collect {
                _recipesFlow.value = State.Loading()
                try {
                    _recipesFlow.value = State.Success(it)
                }catch (e : Exception) {
                    _recipesFlow.value = State.Error(e.toString())
                }
            }
        }
    }

    fun findRecipesByDetailName(name: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.findLocalDetailRecipeByName(name).collect {
                _detail.value = State.Loading()
                try {
                    _detail.value = State.Success(it)
                }catch (e: Exception) {
                    _detail.value = State.Error(e.toString())
                }
            }
        }
    }

}