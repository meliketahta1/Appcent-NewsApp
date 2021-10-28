package com.example.appcentnewsapp.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.Navigation
import com.example.appcentnewsapp.model.Article
import com.example.appcentnewsapp.model.NewsResponse
import com.example.appcentnewsapp.repository.Repository
import com.example.appcentnewsapp.util.Resource
import com.example.appcentnewsapp.view.fragment.FavoriteFragmentDirections

import kotlinx.coroutines.launch
import retrofit2.Response

class SearchViewModel(val repository: Repository) : ViewModel() {

    val news: MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val favoriteArticles: MutableLiveData<List<Article>> = MutableLiveData()

    var page = 1
    var searchResponse: NewsResponse? = null
    var progressBar: MutableLiveData<Boolean> = MutableLiveData()


    fun searchNews(searchQuery: String) = viewModelScope.launch {
        news.postValue(Resource.Loading())
        progressBar.value = true
        val response = repository.searchNews(searchQuery, page)
        news.postValue(handleSearchNewsResponse(response))

    }


    fun getArticles() = favoriteArticles?.value?.size

    fun getFavoriteArticles() {
        viewModelScope.launch {
            favoriteArticles.value = repository.getFavoriteArticles()

        }

    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>): Resource<NewsResponse> {
        if (response.isSuccessful) {
            response.body()?.let { Response ->
                if (searchResponse == null) {
                    searchResponse = Response
                }
                progressBar.value = false

                return Resource.Succes(searchResponse ?: Response)
            }
        }
        return Resource.Error(response.message())
    }

    fun onEmptyIconClick(view: View) {
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToSearchFragment()
        Navigation.findNavController(view).navigate(action)
    }


}