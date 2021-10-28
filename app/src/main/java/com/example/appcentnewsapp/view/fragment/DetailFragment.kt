package com.example.appcentnewsapp.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import com.example.appcentnewsapp.R
import com.example.appcentnewsapp.databinding.FragmentDetailBindingImpl
import com.example.appcentnewsapp.model.Article
import com.example.appcentnewsapp.repository.Repository
import com.example.appcentnewsapp.room.ArticleDatabase
import com.example.appcentnewsapp.util.TransferArticle
import com.example.appcentnewsapp.util.sendInvite
import com.example.appcentnewsapp.view.activity.MainActivity
import com.example.appcentnewsapp.viewmodel.NewsViewModelProviderFactory
import com.example.appcentnewsapp.viewmodel.SearchViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_detail.*
import java.lang.StringBuilder


class DetailFragment : Fragment() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var dataBinding: FragmentDetailBindingImpl
    private lateinit var articleDetail: Article
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)
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
        iv_share.setOnClickListener {
            it.context.sendInvite(TransferArticle.article?.url.toString())
        }
        bt_goWeb.setOnClickListener {
            val action = DetailFragmentDirections.actionDetailFragmentToSourceFragment()
            Navigation.findNavController(it).navigate(action)
        }

        iv_favorite.setOnClickListener {
            if (viewModel.repository.getArticle(TransferArticle.article?.url.toString()) != null) {
                Toast.makeText(
                    requireContext(),
                    "You have already add this article to your favorite list..",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                TransferArticle.article?.let { it1 ->
                    viewModel.repository.addFavori(it1)
                    val snack = Snackbar.make(
                        it,
                        "You have added the article to the your favorite list.",
                        Snackbar.LENGTH_LONG
                    )
                    snack.show()

                }
            }

        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.support_action_bar_menu, menu)
    }


}