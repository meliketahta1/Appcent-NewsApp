package com.example.appcentnewsapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appcentnewsapp.R
import com.example.appcentnewsapp.databinding.SearchItemBindingImpl
import com.example.appcentnewsapp.model.Article
import com.example.appcentnewsapp.util.TransferArticle
import com.example.appcentnewsapp.view.fragment.SearchFragmentDirections
import com.example.appcentnewsapp.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.search_item.view.*

class SearchAdapter(val articleList: ArrayList<Article>, var viewModel: SearchViewModel) : RecyclerView.Adapter<SearchAdapter.ArticleViewHolder>(),ItemClickListener{


    private var dataSet = articleList

    class ArticleViewHolder(var view: SearchItemBindingImpl) : RecyclerView.ViewHolder(view.root) {
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<SearchItemBindingImpl>(inflater, R.layout.search_item, parent, false)
        return ArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        holder.view.article = dataSet[position]
        holder.view.listener=this
        holder.view.position=position.toString()
        holder.setIsRecyclable(false);
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setData(newArticleList: List<Article>) {
        val diffUtil = DiffUtil(dataSet, newArticleList)
        val diffResults = DiffUtil.calculateDiff(diffUtil)
        dataSet.clear()
        dataSet.addAll(newArticleList)
        diffResults.dispatchUpdatesTo(this)

    }
    override fun onSearchItemCLicked(v: View) {
        TransferArticle.article =dataSet[v.index.text.toString().toInt()]
        val action = SearchFragmentDirections.actionSearchFragmentToDetailFragment()
        Navigation.findNavController(v).navigate(action)
    }


}