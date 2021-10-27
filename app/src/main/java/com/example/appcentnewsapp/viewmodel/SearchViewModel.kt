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

class SearchViewModel(val repository: Repository): ViewModel() {

    val searchNews : MutableLiveData<Resource<NewsResponse>> = MutableLiveData()
    val favoriteArticles : MutableLiveData<List<Article>> = MutableLiveData()
    var searchNewsPage = 1
    var searchNewsResponse: NewsResponse? = null
    var progressBar: MutableLiveData<Boolean> = MutableLiveData()





    fun searchNews(searchQuery: String) = viewModelScope.launch {
        searchNews.postValue(Resource.Loading())
        progressBar.value=true
        val response = repository.searchNews(searchQuery,searchNewsPage)
        searchNews.postValue(handleSearchNewsResponse(response))

    }



    fun getArticles()= favoriteArticles?.value?.size

    fun getFavoriteArticles(){
        viewModelScope.launch{
            favoriteArticles.value=repository.getFavoriteArticles()
        }

    }


    private fun handleSearchNewsResponse(response: Response<NewsResponse>) : Resource<NewsResponse>{
        if(response.isSuccessful){
            response.body()?.let {resultResponse ->
                searchNewsPage++
                if(searchNewsResponse == null){
                    searchNewsResponse = resultResponse
                }else{
                    val oldArticles = resultResponse?.articles
                    val newArticles = resultResponse.articles
                    oldArticles?.addAll(newArticles)
                }
                progressBar.value=false

                return Resource.Succes(searchNewsResponse ?: resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    fun onEmptyIconClick(view: View){
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToSearchFragment()
        Navigation.findNavController(view).navigate(action)
    }


}