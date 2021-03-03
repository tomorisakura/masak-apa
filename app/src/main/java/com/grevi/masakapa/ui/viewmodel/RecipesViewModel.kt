package com.grevi.masakapa.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grevi.masakapa.db.entity.Category
import com.grevi.masakapa.network.response.DetailResponse
import com.grevi.masakapa.network.response.RecipesResponse
import com.grevi.masakapa.network.response.SearchResponse
import com.grevi.masakapa.repository.RepositoryImpl
import com.grevi.masakapa.util.NetworkUtils
import com.grevi.masakapa.util.ResponseException
import com.grevi.masakapa.util.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipesViewModel @Inject constructor(private val repositoryImpl: RepositoryImpl, private val networkUtils: NetworkUtils) : ViewModel() {

    private val _recipesData = MutableLiveData<State<RecipesResponse>>()
    private val _recipeDetail = MutableLiveData<State<DetailResponse>>()
    private val _recipeSearch = MutableLiveData<State<SearchResponse>>()
    private val _category = MutableStateFlow<State<MutableList<Category>>>(State.Data)

    val recipes : MutableLiveData<State<RecipesResponse>> get() = _recipesData
    val category : MutableStateFlow<State<MutableList<Category>>> get() = _category

    private val netWorkObserver = networkUtils.networkDataStatus

    init {
        getRecipes()
        getCategoryLocal()
    }

    private fun getRecipes() {
        viewModelScope.launch {
            val data = repositoryImpl.getRecipes()
            if (netWorkObserver.value == true) {
                _recipesData.postValue(data)
            }else {
                _recipesData.postValue(State.Error("No Internet Connection"))
            }
        }
    }

    fun getDetail(key : String) : LiveData<State<DetailResponse>> {
        viewModelScope.launch {
            delay(1000L)
            val data = repositoryImpl.getDetailRecipes(key)
            _recipeDetail.postValue(State.Loading())
            try {
                _recipeDetail.postValue(data)
            } catch (e : Exception) {
                e.printStackTrace()
                _recipeDetail.postValue(State.Error(e.toString()))
            }catch (e : ResponseException) {
                e.printStackTrace()
                _recipeDetail.postValue(State.Error(e.toString()))
            }
        }

        return _recipeDetail
    }

    fun searchRecipe(query : String) : LiveData<State<SearchResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getSearchRecipe(query)
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
        viewModelScope.launch {
            repositoryImpl.getFlowCategory().collect {
                _category.value = State.Loading()
                try {
                    _category.value = State.Success(it)
                }catch (e : Exception) {
                    e.printStackTrace()
                    _category.value = State.Error(e.toString())
                }catch (e : ResponseException) {
                    e.printStackTrace()
                    _category.value = State.Error(e.toString())
                }
            }
        }
    }

    fun categoryResult(key: String) : LiveData<State<RecipesResponse>> {
        viewModelScope.launch {
            val data = repositoryImpl.getCategoryRecipes(key)
            _recipesData.postValue(State.Loading())
            try {
                _recipesData.postValue(data)
            } catch (e : Exception) {
                e.printStackTrace()
                _recipesData.postValue(State.Error(e.toString()))
            }
        }
        return _recipesData
    }
}