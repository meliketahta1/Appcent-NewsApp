package com.example.appcentnewsapp.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appcentnewsapp.model.Article

@Database(entities = arrayOf(Article::class),version = 1 ,exportSchema = false)
@TypeConverters(Converters::class)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articlelDao(): ArticleDao

    companion object{
        @Volatile private var instance: ArticleDatabase?=null
        private val lock=Any()
        operator fun invoke(context: Context)= instance ?: synchronized(lock){
            instance ?: createDatabase(context).also {
                instance =it
            }
        }

        private fun createDatabase(context: Context)= Room.databaseBuilder(
            context.applicationContext, ArticleDatabase::class.java,"article_db"
        )
            .allowMainThreadQueries()
            .build()
    }


}
