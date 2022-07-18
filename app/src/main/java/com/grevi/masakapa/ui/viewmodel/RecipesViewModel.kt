package com.grevi.masakapa.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.masakapa.common.shared.SharedPref
import com.grevi.masakapa.common.state.State
import com.grevi.masakapa.data.local.entity.Category
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.data.remote.response.RecipesResponse
import com.grevi.masakapa.data.remote.response.SearchResponse
import com.grevi.masakapa.repository.Repository
import com.grevi.masakapa.util.Constant.PERMISSIONS_STORAGE
import com.grevi.masakapa.util.ResponseException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipesViewModel @Inject constructor(
    private val repository: Repository,
    private val sharedPref: SharedPref
) : ViewModel() {

    private val _recipesData = MutableLiveData<State<RecipesResponse>>()
    private val _recipeDetail = MutableLiveData<State<DetailResponse>>()
    private val _recipeSearch = MutableLiveData<State<SearchResponse>>()

    private val _category = MutableStateFlow<State<MutableList<Category>>>(State.Data)
    val category: MutableStateFlow<State<MutableList<Category>>> = _category

    private val _recipesLimit = MutableStateFlow<State<RecipesResponse>>(State.Data)
    val recipesLimit: MutableStateFlow<State<RecipesResponse>> = _recipesLimit

    init {
        getCategoryLocal()
        getRecipes()
        viewModelScope.launch(Dispatchers.IO) {
            repository.getCategory()
        }
    }

    private fun getRecipes() {
        viewModelScope.launch {
            repository.getRecipes().collect { _recipesLimit.value = it }
        }
    }

    fun getDetail(key: String): LiveData<State<DetailResponse>> {
        viewModelScope.launch(Dispatchers.IO) {
            _recipeDetail.postValue(repository.getDetailRecipes(key))
        }
        return _recipeDetail
    }

    fun searchRecipe(query: String): LiveData<State<SearchResponse>> {
        viewModelScope.launch(Dispatchers.IO) {
            _recipeSearch.postValue(repository.getSearchRecipe(query))
        }
        return _recipeSearch
    }

    private fun getCategoryLocal() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getFlowCategory().collect { _category.value = State.Success(it) }
        }
    }

    fun categoryResult(key: String): LiveData<State<RecipesResponse>> {
        viewModelScope.launch(Dispatchers.IO) {
            _recipesData.postValue(repository.getCategoryRecipes(key))
        }
        return _recipesData
    }

    fun getStoragePermission() = sharedPref.getStoragePermission(PERMISSIONS_STORAGE)
}