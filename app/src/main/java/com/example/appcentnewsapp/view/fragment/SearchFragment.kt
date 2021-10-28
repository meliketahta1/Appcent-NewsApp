package com.example.appcentnewsapp.view.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.appcentnewsapp.R
import com.example.appcentnewsapp.adapter.SearchAdapter
import com.example.appcentnewsapp.repository.Repository
import com.example.appcentnewsapp.room.ArticleDatabase
import com.example.appcentnewsapp.util.NetworkConnectionHandler
import com.example.appcentnewsapp.util.closeKeyboard
import com.example.appcentnewsapp.viewmodel.NewsViewModelProviderFactory
import com.example.appcentnewsapp.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.fragment_search.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.appcentnewsapp.databinding.FragmentSearchBindingImpl
import com.example.appcentnewsapp.util.Resource


class SearchFragment : Fragment() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: SearchAdapter
    private lateinit var dataBinding: FragmentSearchBindingImpl


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)

        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setViewModel()
        searchButton.setOnClickListener {
            viewModel.searchNews(searchText.text.toString())
            it.closeKeyboard()

        }
        dataBinding.viewModel = viewModel

        setRecyclerView()


        onClickListener()
        observeLiveData()
        observeNetworkStatus()

        progressBar.visibility = View.GONE

    }


    fun setViewModel() {
        val db = ArticleDatabase.invoke(requireContext())
        val newsRepository = Repository(db)
        val viewModelProviderFactory = NewsViewModelProviderFactory(newsRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(SearchViewModel::class.java)
    }

    fun setRecyclerView() {
        searchResultRV.layoutManager = LinearLayoutManager(context)
        adapter = SearchAdapter(arrayListOf(), viewModel)
        searchResultRV.adapter = adapter
    }


    fun onClickListener() {
        searchButton.setOnClickListener {
            viewModel.searchNews(searchText.text.toString())
            viewModel.searchResponse = null
            searchResultRV.visibility = View.GONE
            it.closeKeyboard()

        }
    }


    fun observeLiveData() {
        viewModel.news.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Succes -> {
                    it.data?.let { response ->
                        adapter.setData(response.articles)
                        progressBar.visibility = View.GONE
                        searchResultRV.visibility = View.VISIBLE

                    }

                }
                is Resource.Error -> {

                    Toast.makeText(requireContext(), "An Error Occurred :(\n The request limit of the api key may have been exceeded\n Please change tha api key and try again :) " , Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE

                }
                is Resource.Loading -> {
                    progressBar.visibility = View.VISIBLE

                }

            }

        })
        viewModel.progressBar.observe(viewLifecycleOwner, {
            if (it) {
                progressBar.visibility = View.VISIBLE

            } else {
                progressBar.visibility = View.GONE
            }
        })


    }

    fun observeNetworkStatus() {
        NetworkConnectionHandler.getNetworkState(requireContext())
            .observe(viewLifecycleOwner) { connection ->
                if (connection) {
                    et_error.visibility = View.GONE
                    LinearLayout.visibility = View.VISIBLE

                } else {
                    et_error.visibility = View.VISIBLE
                    LinearLayout.visibility = View.GONE

                }

            }
    }


}