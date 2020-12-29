package com.grevi.masakapa.ui.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.masakapa.db.entity.Category
import com.grevi.masakapa.network.response.DetailResponse
import com.grevi.masakapa.network.response.RecipesResponse
import com.grevi.masakapa.network.response.SearchResponse
import com.grevi.masakapa.repos.Remote
import com.grevi.masakapa.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class RecipesViewModel @ViewModelInject constructor(private val remote: Remote) : ViewModel() {

    private val _recipesData = MutableLiveData<Resource<RecipesResponse>>()
    private val _recipeDetail = MutableLiveData<Resource<DetailResponse>>()
    private val _recipeSearch = MutableLiveData<Resource<SearchResponse>>()

    private val categoryLiveData = MutableLiveData<Resource<MutableList<Category>>>()

    val recipes : MutableLiveData<Resource<RecipesResponse>>
    get() = _recipesData

    val categoryFlow : MutableLiveData<Resource<MutableList<Category>>>
    get() = categoryLiveData

    init {
        getRecipes()
        categorysRecipes()
        insertFlowCategory()
        showFlow()
    }

    private fun getRecipes(){
        viewModelScope.launch {
            val data = remote.getRecipes()
            _recipesData.postValue(Resource.loading(null, "Load"))
            try {
                _recipesData.postValue(Resource.success(data.data!!))
                Log.v("LDATA", "OKE")
            } catch (e : Exception) {
                _recipesData.postValue(Resource.error(null, e.toString()))
            }
        }
    }

    fun getDetail(key : String) : MutableLiveData<Resource<DetailResponse>> {
        viewModelScope.launch {
            delay(1000L)
            val data = remote.getDetailRecipes(key)
            _recipeDetail.postValue(Resource.loading(null, "Load"))
            try {
                _recipeDetail.postValue(Resource.success(data.data!!))
            } catch (e : Exception) {
                _recipeDetail.postValue(Resource.error(null, e.toString()))
            }
        }

        return _recipeDetail
    }

    fun searchRecipe(query : String) : MutableLiveData<Resource<SearchResponse>> {
        viewModelScope.launch {
            val data = remote.getSearchRecipe(query)
            _recipeSearch.postValue(Resource.loading(null, "Load"))
            try {
                _recipeSearch.postValue(Resource.success(data.data!!))
            }catch (e : Exception) {
                Log.v("LDATA", e.message.toString())
                _recipeSearch.postValue(Resource.error(null, e.toString()))
            }
        }
        return _recipeSearch
    }

    private fun insertFlowCategory() : Flow<MutableList<Category>> = flow {
        val data = remote.getCategorys()

        try {
            for (category in data.data!!.results) {
                val category = Category(
                    category = category.category,
                    key = category.key
                )
                remote.insertCategory(category)
                emit(mutableListOf(category))
            }
        }catch (e : Exception) {
            emit(mutableListOf())
        }
    }

    private fun showFlow() {
        viewModelScope.launch {
            insertFlowCategory().collect {
                Log.d("DATA_FLOW", it.toString())
            }
        }
    }

    private fun categorysRecipes() {
        viewModelScope.launch {

            val dataFLow = remote.getFlowCategory()
            dataFLow.collect{
                Log.d("DATA_FLOW_ROOM", it.toString())

                categoryLiveData.postValue(Resource.loading(null, "Load"))
                try {
                    categoryLiveData.postValue(Resource.success(it))
                }catch (e : Exception) {
                    categoryLiveData.postValue(Resource.error(null, e.toString()))
                }
            }
        }
    }

    fun categoryResult(key: String) : MutableLiveData<Resource<RecipesResponse>> {
        viewModelScope.launch {
            val data = remote.getCategoryRecipes(key)
            _recipesData.postValue(Resource.loading(null, "Load"))
            try {
                _recipesData.postValue(Resource.success(data.data!!))
            } catch (e : Exception) {
                _recipesData.postValue(Resource.error(null, e.toString()))
            }
        }
        return _recipesData
    }
}