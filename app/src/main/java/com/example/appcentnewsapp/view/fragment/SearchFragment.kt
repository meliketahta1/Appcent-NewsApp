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
import android.widget.AbsListView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.appcentnewsapp.databinding.FragmentSearchBindingImpl
import com.example.appcentnewsapp.util.Resource


class SearchFragment : Fragment() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var adapter: SearchAdapter
    private lateinit var dataBinding: FragmentSearchBindingImpl

    private var page = 1
    private var pageLimit = 10

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


        //I have tried to make pagination, but when the data set is refreshed
        //with the new coming articles the items are overlaps
        //I could not handle that problem that's why i made in command of pagination codes.
/*

        searchResultRV.apply {
            addOnScrollListener(this@SearchFragment.scrollListener)
        }

 */


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


    var isLoading = false
    var isLastPage = false
    var isScrolling = false
    var scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            var visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount
            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= 10

            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotBeginning &&
                    isTotalMoreThanVisible && isScrolling

            if (shouldPaginate) {
                viewModel.searchNews(searchText.text.toString())
                //  recyclerView.visibility=View.GONE
                isScrolling = true
            }

        }

        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }
    }


    fun onClickListener() {
        searchButton.setOnClickListener {
            viewModel.searchNews(searchText.text.toString())
            it.closeKeyboard()

        }
    }


    fun observeLiveData() {
        viewModel.searchNews.observe(viewLifecycleOwner, {
            when(it){
                is Resource.Succes->{
                    it.data?.let { response ->
                        adapter.setData(response.articles)
                        val totalPages = response.totalResults / 10 + 2
                        isLastPage = viewModel.searchNewsPage == totalPages
                        progressBar.visibility = View.GONE
                        searchResultRV.visibility = View.VISIBLE
                        isLoading = false

                    }

                }
                is Resource.Error->{
                    Toast.makeText(requireContext(),"Error",Toast.LENGTH_LONG).show()
                    progressBar.visibility=View.GONE

                }
                is Resource.Loading->{
                    progressBar.visibility=View.VISIBLE

                }

            }

        })
        viewModel.progressBar.observe(viewLifecycleOwner, {
            if (it) {
                progressBar.visibility = View.VISIBLE
                isLoading = true

            } else {
                progressBar.visibility = View.GONE
                isLoading = false
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