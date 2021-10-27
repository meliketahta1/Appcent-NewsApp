package com.example.appcentnewsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.appcentnewsapp.repository.Repository

class NewsViewModelProviderFactory(val newsRepository: Repository): ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SearchViewModel(newsRepository) as T
    }
}