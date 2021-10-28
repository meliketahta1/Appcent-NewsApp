package com.example.appcentnewsapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.appcentnewsapp.R
import com.example.appcentnewsapp.databinding.FragmentDetailBindingImpl
import com.example.appcentnewsapp.databinding.FragmentSourceBindingImpl
import com.example.appcentnewsapp.repository.Repository
import com.example.appcentnewsapp.room.ArticleDatabase
import com.example.appcentnewsapp.util.TransferArticle
import com.example.appcentnewsapp.view.activity.MainActivity
import com.example.appcentnewsapp.viewmodel.NewsViewModelProviderFactory
import com.example.appcentnewsapp.viewmodel.SearchViewModel


class SourceFragment : Fragment() {

    private lateinit var viewModel: SearchViewModel
    private lateinit var dataBinding: FragmentSourceBindingImpl
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_source, container, false)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val db = ArticleDatabase.invoke(requireContext())
        val newsRepository = Repository(db)
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(SearchViewModel::class.java)

        dataBinding.article = TransferArticle.article


    }

}