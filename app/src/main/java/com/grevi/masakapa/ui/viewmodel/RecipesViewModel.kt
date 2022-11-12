package com.grevi.masakapa.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.grevi.masakapa.common.base.BaseViewModel
import com.grevi.masakapa.common.shared.SharedPref
import com.grevi.masakapa.common.state.State
import com.grevi.masakapa.data.local.entity.RecipeFavorite
import com.grevi.masakapa.data.remote.response.CategoryResponse
import com.grevi.masakapa.data.remote.response.DetailResponse
import com.grevi.masakapa.data.remote.response.RecipesResponse
import com.grevi.masakapa.data.remote.response.SearchResponse
import com.grevi.masakapa.model.Detail
import com.grevi.masakapa.repository.Repository
import com.grevi.masakapa.util.Constant.PERMISSIONS_STORAGE
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

    private val _recipesLimit = MutableLiveData<State<RecipesResponse>>()
    val recipesLimit: LiveData<State<RecipesResponse>> = _recipesLimit

    private val _isExist = MutableLiveData<Boolean>()
    val isExistFromDb: LiveData<Boolean> = _isExist

    private val _favoriteRecipes = MutableLiveData<List<RecipeFavorite>>()
    val favoriteRecipes: LiveData<List<RecipeFavorite>> = _favoriteRecipes

    suspend fun getRecipes() = collectFlow(repository.getRecipes(), _recipesLimit)

    suspend fun getDetail(key: String) =
        collectFlow(repository.getDetailRecipes(key), _recipeDetail)

    suspend fun getCategory() = collectFlow(repository.getCategory(), _category)

    suspend fun searchRecipe(query: String) =
        collectFlow(repository.getSearchRecipe(query), _recipeSearch)

    suspend fun categoryResult(key: String) =
        collectFlow(repository.getCategoryRecipes(key), _recipesData)

    suspend fun insertRecipes(detail: Detail, key: String, thumb: String) {
        repository.insertFavorite(
            RecipeFavorite(
                key = key,
                name = detail.name,
                difficulty = detail.difficulty,
                imageThumb = thumb,
                portion = detail.servings,
                times = detail.times
            )
        )
    }

    suspend fun keyChecker(key: String) =
        collectFlow(repository.isFavoriteExists(key), _isExist)

    suspend fun deleteRecipes(favorite: RecipeFavorite) = repository.deleteFavorite(favorite)

    suspend fun getFavoriteRecipes() = collectFlow(repository.getFlowFavorite(), _favoriteRecipes)

    fun setStoragePermission(isGrant: Boolean) = sharedPref.setStoragePermission(isGrant)
}