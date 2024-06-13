package com.bangkit.anemai.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.anemai.R
import com.bangkit.anemai.data.DataDummy
import com.bangkit.anemai.data.adapter.ArticleAdapter
import com.bangkit.anemai.data.model.ArticlesResponseItem
import com.bangkit.anemai.databinding.FragmentMainBinding
import com.bangkit.anemai.ui.welcome.WelcomeActivity

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var menuProvider: MenuProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvArticle.layoutManager = LinearLayoutManager(requireContext())
        val articleList = DataDummy.generateArticleList()

        setupAction(view)
        setupActionBar()
        setupArticle(articleList, view)

    }

    private fun setupAction(view: View) {
        val extras = FragmentNavigatorExtras(
            binding.bgLayout to "bg_layout"
        )
        binding.btnHistory.setOnClickListener {
            view.findNavController().navigate(R.id.action_mainFragment_to_historyFragment, null, null, extras)
        }
    }

    private fun setupActionBar() {
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(false)
        }

        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_exit, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menu_exit -> {
                        val intent = Intent(this@MainFragment.requireActivity(), WelcomeActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        requireActivity().finish()
                        true
                    }
                    else -> false
                }
            }
        }

        requireActivity().addMenuProvider(menuProvider)
    }

    private fun setupArticle(articleList: List<ArticlesResponseItem>, view: View) {
        val adapter = ArticleAdapter { article ->

        }

        binding.rvArticle.adapter = adapter
        adapter.submitList(articleList.subList(0,3))
    }

    override fun onStop() {
        super.onStop()
        requireActivity().removeMenuProvider(menuProvider)
    }
}