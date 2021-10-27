package com.example.appcentnewsapp.view.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.appcentnewsapp.R
import com.example.appcentnewsapp.repository.Repository
import com.example.appcentnewsapp.util.NetworkConnectionHandler
import com.example.appcentnewsapp.viewmodel.NewsViewModelProviderFactory
import com.example.appcentnewsapp.viewmodel.SearchViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_search.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavigationView.setupWithNavController(newsNavHostFragment.findNavController())

    }




}