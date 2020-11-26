package com.grevi.masakapa.ui.viewmodel

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.masakapa.network.response.DetailResponse
import com.grevi.masakapa.network.response.RecipesResponse
import com.grevi.masakapa.repos.Remote
import com.grevi.masakapa.util.Resource
import com.grevi.masakapa.util.ResponseException
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
                _recipesData.postValue(Resource.loading(null, "Loading"))
                data?.let {
                    _recipesData.postValue(Resource.success(it))
                }
            } catch (e : ResponseException) {
                _recipesData.postValue(Resource.error(null, e.message))
                Log.e("INET", e.message.toString())
            }
        }
    }

    fun getDetail(key : String) : LiveData<Resource<DetailResponse>> {
        viewModelScope.launch {
            val data = remote.getDetailRecipes(key)
            try {
                _recipeDetail.postValue(Resource.loading(null, "Load"))
                data?.let {
                    _recipeDetail.postValue(Resource.success(data, null))
                }
            } catch (e : ResponseException) {
                _recipeDetail.postValue(Resource.error(data, e.message))
            }
        }

        return _recipeDetail
    }
}