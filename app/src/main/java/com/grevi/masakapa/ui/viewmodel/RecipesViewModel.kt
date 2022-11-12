package com.grevi.masakapa.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.grevi.masakapa.common.base.BaseViewModel
import com.grevi.masakapa.common.shared.SharedPref
import com.grevi.masakapa.common.state.State
import com.grevi.masakapa.data.local.entity.Category
import com.grevi.masakapa.data.remote.response.CategoryResponse
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.data.remote.response.RecipesResponse
import com.grevi.masakapa.data.remote.response.SearchResponse
import com.grevi.masakapa.repository.Repository
import com.grevi.masakapa.util.Constant.PERMISSIONS_STORAGE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class RecipesViewModel @Inject constructor(
    private val repository: Repository,
    private val sharedPref: SharedPref
) : BaseViewModel() {

    private val _recipesData = MutableLiveData<State<RecipesResponse>>()
    val recipesData: LiveData<State<RecipesResponse>> = _recipesData

    private val _recipeDetail = MutableLiveData<State<DetailResponse>>()
    val recipesDetail: LiveData<State<DetailResponse>> = _recipeDetail

    private val _recipeSearch = MutableLiveData<State<SearchResponse>>()
    val recipesSearch: LiveData<State<SearchResponse>> = _recipeSearch

    private val _category = MutableLiveData<State<CategoryResponse>>(State.Data)
    val category: LiveData<State<CategoryResponse>> = _category


    private val _categoryLocal = MutableLiveData<MutableList<Category>>()
    val categoryLocal: LiveData<MutableList<Category>> = _categoryLocal

    private val _recipesLimit = MutableLiveData<State<RecipesResponse>>()
    val recipesLimit: LiveData<State<RecipesResponse>> = _recipesLimit

    suspend fun getRecipes() = collectFlow(repository.getRecipes(), _recipesLimit)

    suspend fun getDetail(key: String) = collectFlow(repository.getDetailRecipes(key), _recipeDetail)

    suspend fun getCategory() = collectFlow(repository.getCategory(), _category)

    suspend fun searchRecipe(query: String) =
        collectFlow(repository.getSearchRecipe(query), _recipeSearch)

    suspend fun categoryResult(key: String) =
        collectFlow(repository.getCategoryRecipes(key), _recipesData)

    fun getStoragePermission() = sharedPref.getStoragePermission(PERMISSIONS_STORAGE)
}