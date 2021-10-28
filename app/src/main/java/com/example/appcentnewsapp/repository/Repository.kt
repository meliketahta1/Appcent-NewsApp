package com.example.appcentnewsapp.repository


import com.example.appcentnewsapp.model.Article
import com.example.appcentnewsapp.room.ArticleDatabase
import com.example.appcentnewsapp.service.NewsApiService

class Repository(val roomDb: ArticleDatabase) {

    suspend fun searchNews(searchQuery: String, pageNumber: Int) =
        NewsApiService.api.searchForNews(searchQuery, pageNumber)

    fun addFavori(article: Article) = roomDb.articlelDao().insertArticle(article)

    suspend fun getFavoriteArticles() = roomDb.articlelDao().getAllArticles()

    fun getArticle(url: String) = roomDb.articlelDao().getArticle(url)

    fun deleteArticle() = roomDb.articlelDao().deleteArticle()


}