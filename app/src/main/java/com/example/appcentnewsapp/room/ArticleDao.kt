package com.example.appcentnewsapp.room

import android.content.Context
import androidx.room.*
import com.example.appcentnewsapp.model.Article


@Dao
@TypeConverters(Converters::class)
interface ArticleDao {


    @Insert
    fun insertArticle(article: Article)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): List<Article>

    @Query("SELECT * FROM articles WHERE url = :urlArticle")
    fun getArticle(urlArticle:String): Article


    @Query("DELETE  FROM articles ")
    fun deleteArticle()

}
