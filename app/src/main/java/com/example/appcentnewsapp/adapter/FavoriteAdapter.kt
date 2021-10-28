package com.example.appcentnewsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.appcentnewsapp.R
import com.example.appcentnewsapp.databinding.FavoriteArticleItemBindingImpl
import com.example.appcentnewsapp.databinding.SearchItemBindingImpl
import com.example.appcentnewsapp.model.Article
import com.example.appcentnewsapp.util.TransferArticle
import com.example.appcentnewsapp.view.fragment.FavoriteFragmentDirections
import com.example.appcentnewsapp.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.search_item.view.*

class FavoriteAdapter(val articleList: ArrayList<Article>, var viewModel: SearchViewModel) :
    RecyclerView.Adapter<FavoriteAdapter.FavoriteArticleViewHolder>(), ItemClickListener {


    private var dataSet = articleList

    class FavoriteArticleViewHolder(var view: FavoriteArticleItemBindingImpl) :
        RecyclerView.ViewHolder(view.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteArticleViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<FavoriteArticleItemBindingImpl>(
            inflater,
            R.layout.favorite_article_item,
            parent,
            false
        )
        return FavoriteArticleViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteArticleViewHolder, position: Int) {
        holder.view.article = dataSet[position]
        holder.view.listener = this
        holder.view.position = position.toString()
        holder.setIsRecyclable(false);
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    fun setData(newArticleList: List<Article>) {
        val diffUtil = DiffUtil(dataSet, newArticleList)
        val diffResults = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtil)
        dataSet.clear()
        dataSet.addAll(newArticleList)
        diffResults.dispatchUpdatesTo(this)
    }

    override fun onSearchItemCLicked(v: View) {
        TransferArticle.article = dataSet[v.index.text.toString().toInt()]
        val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment()
        Navigation.findNavController(v).navigate(action)
    }


}