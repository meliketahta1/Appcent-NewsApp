package com.example.appcentnewsapp.view.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcentnewsapp.R
import com.example.appcentnewsapp.adapter.FavoriteAdapter
import com.example.appcentnewsapp.databinding.FragmentFavoriteBindingImpl
import com.example.appcentnewsapp.repository.Repository
import com.example.appcentnewsapp.room.ArticleDatabase
import com.example.appcentnewsapp.viewmodel.NewsViewModelProviderFactory
import com.example.appcentnewsapp.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_favorite.*


class FavoriteFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var dataBinding: FragmentFavoriteBindingImpl
    private lateinit var adapter: FavoriteAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_favorite, container, false)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setViewModel()
        setRecyclerView()
        viewModel.getFavoriteArticles()
        dataBinding.viewModel = viewModel
        deleteIV.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Sure?")
            builder.setMessage("You're about to delete all articles in your favorite list. Are you sure ?")


            builder.setPositiveButton("YES") { dialog, which ->
                viewModel.repository.deleteArticle()
                favoriteArticleIV.visibility = View.GONE
                progressBarFV.visibility = View.VISIBLE
                observeLiveData()
            }

            builder.setNegativeButton("NO") { dialog, which ->

            }


            builder.show()
        }
        observeLiveData()


    }

    fun setViewModel() {
        val db = ArticleDatabase.invoke(requireContext())
        val newsRepository = Repository(db)
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(SearchViewModel::class.java)
    }

    fun setRecyclerView() {
        favoriteArticleIV.layoutManager = LinearLayoutManager(context)
        adapter = FavoriteAdapter(arrayListOf(), viewModel)
        favoriteArticleIV.adapter = adapter
    }


    fun observeLiveData() {
        viewModel.favoriteArticles.observe(viewLifecycleOwner, { articles ->
            articles?.let { response ->
                progressBarFV.visibility = View.GONE
                adapter.setData(response)
            }
        })

    }


}