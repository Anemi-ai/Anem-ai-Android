package com.bangkit.anemai.ui.article

import android.app.AlertDialog
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
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.ChangeBounds
import com.bangkit.anemai.R
import com.bangkit.anemai.data.adapter.ArticleAdapter
import com.bangkit.anemai.data.model.ArticleItem
import com.bangkit.anemai.databinding.FragmentArticleBinding
import com.bangkit.anemai.ui.ViewModelFactory
import com.bangkit.anemai.ui.main.MainActivity
import com.bangkit.anemai.ui.main.MainViewModel
import com.bangkit.anemai.utils.Result


class ArticleFragment : Fragment() {
    private lateinit var binding: FragmentArticleBinding
    private lateinit var menuProvider: MenuProvider
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleBinding.inflate(layoutInflater)
        sharedElementEnterTransition = ChangeBounds().apply { duration = 500 }
        sharedElementReturnTransition = ChangeBounds().apply { duration = 500 }
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvArticle.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getArticles().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when(result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)

                        setupArticle(result.data, view)
                    }
                    is Result.Error -> {
                        showLoading(false)

                        AlertDialog.Builder(context).apply {
                            setMessage(getString(R.string.error_try_again))
                            setPositiveButton(getString(R.string.ok)) { _, _ ->
                            }

                            create()
                            show()
                        }
                    }
                }
            }
        }

        setupActionbar()
    }

    private fun setupActionbar() {
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.article)
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            setHomeButtonEnabled(true)
        }

        menuProvider = object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                when (item.itemId) {
                    android.R.id.home -> {
                        requireActivity().supportFragmentManager.popBackStack()
                        return true
                    }
                }

                return false
            }

        }

        requireActivity().addMenuProvider(menuProvider)
    }

    private fun setupArticle(articleList: List<ArticleItem>, view: View) {
        val extras = FragmentNavigatorExtras(
            binding.bgLayout to "bg_layout"
        )

        val adapter = ArticleAdapter { article ->
            val toArticleDetail = ArticleFragmentDirections.actionArticleFragmentToArticleDetailFragment()
            toArticleDetail.articleId = article.id.toString()

            view.findNavController().navigate(toArticleDetail, extras)
        }

        binding.rvArticle.adapter = adapter
        adapter.submitList(articleList)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            (activity as? MainActivity)?.showLoading(true)
        } else {
            (activity as? MainActivity)?.showLoading(false)
        }
    }

}