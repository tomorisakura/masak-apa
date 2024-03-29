package com.grevi.masakapa.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.masakapa.data.local.entity.Category
import com.grevi.masakapa.data.local.entity.DetailTable
import com.grevi.masakapa.data.local.entity.RecipesTable
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.data.remote.response.RecipesResponse
import com.grevi.masakapa.data.remote.response.SearchResponse
import com.grevi.masakapa.repository.Repository
import com.grevi.masakapa.util.ResponseException
import com.grevi.masakapa.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(private val repository: Repository) : ViewModel() {

    private val _recipesData = MutableLiveData<State<RecipesResponse>>()
    private val _recipeDetail = MutableLiveData<State<DetailResponse>>()
    private val _recipeSearch = MutableLiveData<State<SearchResponse>>()
    private val _category = MutableStateFlow<State<MutableList<Category>>>(State.Data)
    private val _recipes = MutableStateFlow<State<MutableList<RecipesTable>>>(State.Data)

    val recipes : MutableStateFlow<State<MutableList<RecipesTable>>> get() = _recipes
    val category : MutableStateFlow<State<MutableList<Category>>> get() = _category

    private val TAG = RecipesViewModel::class.simpleName

    init {
        getLocalRecipes()
        getCategoryLocal()

        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategory()
            repository.getRecipes()
        }
    }

    private fun getLocalRecipes() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFlowLocalRecipes().collect {
                _recipes.value = State.Loading()
                try {
                    _recipes.value = State.Success(it)
                }catch (e : ResponseException) {
                    _recipes.value = State.Error(e.toString())
                }
            }

//            val data = repository.getRecipes()
//            try {
//                _recipesData.postValue(data)
//            } catch (e : ResponseException) {
//                e.printStackTrace()
//                _recipesData.postValue(State.Error(e.toString()))
//            }
        }
    }

    fun getDetail(key : String) : LiveData<State<DetailResponse>> {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getDetailRecipes(key)
            _recipeDetail.postValue(State.Loading())
            try {
                _recipeDetail.postValue(data)
            } catch (e : ResponseException) {
                e.printStackTrace()
                _recipeDetail.postValue(State.Error(e.toString()))
            }
        }

        return _recipeDetail
    }

    fun searchRecipe(query : String) : LiveData<State<SearchResponse>> {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getSearchRecipe(query)
            _recipeSearch.postValue(State.Loading())
            try {
                _recipeSearch.postValue(data)
            }catch (e : Exception) {
                e.printStackTrace()
                _recipeSearch.postValue(State.Error(e.toString()))
            }
        }
        return _recipeSearch
    }

    private fun getCategoryLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFlowCategory().collect {
                _category.value = State.Loading()
                try {
                    _category.value = State.Success(it)
                }catch (e : ResponseException) {
                    e.printStackTrace()
                    _category.value = State.Error(e.toString())
                }
            }
        }
    }

    fun categoryResult(key: String) : LiveData<State<RecipesResponse>> {
        viewModelScope.launch(Dispatchers.IO) {
            val data = repository.getCategoryRecipes(key)
            _recipesData.postValue(State.Loading())
            try {
                _recipesData.postValue(data)
            } catch (e : ResponseException) {
                e.printStackTrace()
                _recipesData.postValue(State.Error(e.toString()))
            }
        }
        return _recipesData
    }
}