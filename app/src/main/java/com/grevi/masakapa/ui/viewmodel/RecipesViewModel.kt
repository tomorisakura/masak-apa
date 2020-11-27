package com.grevi.masakapa.ui.viewmodel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.masakapa.network.response.DetailResponse
import com.grevi.masakapa.network.response.RecipesResponse
import com.grevi.masakapa.repos.Remote
import com.grevi.masakapa.util.Resource
import com.grevi.masakapa.util.ResponseException
import com.grevi.masakapa.util.ResultException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RecipesViewModel @ViewModelInject constructor(private val remote: Remote) : ViewModel() {

    private val _recipesData = MutableLiveData<Resource<RecipesResponse>>()
    private val _recipeDetail = MutableLiveData<Resource<DetailResponse>>()

    val recipes : MutableLiveData<Resource<RecipesResponse>>
    get() = _recipesData

    init {
        getRecipes()
    }

    private fun getRecipes(){
        viewModelScope.launch {
            val data = remote.getRecipes()
            try {
                _recipeDetail.postValue(Resource.loading(null, "Load"))
                _recipesData.postValue(Resource.success(data))
            } catch (e : ResultException) {
                _recipesData.postValue(Resource.error(null, e.message))
            } catch (e : ResponseException) {
                _recipeDetail.postValue(Resource.error(null, e.message))
            }
        }
    }

    fun getDetail(key : String) : MutableLiveData<Resource<DetailResponse>> {
        viewModelScope.launch {
            delay(1000L)
            val data = remote.getDetailRecipes(key)
            try {
                _recipeDetail.postValue(Resource.loading(null, "Load"))
                _recipeDetail.postValue(Resource.success(data))
            } catch (e : ResultException) {
                _recipeDetail.postValue(Resource.error(null, e.message))
            } catch (e : ResponseException) {
                _recipeDetail.postValue(Resource.error(null, e.message))
            }
        }

        return _recipeDetail
    }
}