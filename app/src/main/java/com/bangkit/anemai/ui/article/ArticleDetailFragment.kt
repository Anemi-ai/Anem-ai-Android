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
import androidx.transition.ChangeBounds
import com.bangkit.anemai.BuildConfig
import com.bangkit.anemai.R
import com.bangkit.anemai.data.model.ArticlesResponseItem
import com.bangkit.anemai.databinding.FragmentArticleDetailBinding
import com.bangkit.anemai.ui.ViewModelFactory
import com.bangkit.anemai.ui.main.MainActivity
import com.bangkit.anemai.ui.main.MainViewModel
import com.bangkit.anemai.utils.Result
import com.bumptech.glide.Glide

class ArticleDetailFragment : Fragment() {
    private lateinit var binding: FragmentArticleDetailBinding
    private lateinit var menuProvider: MenuProvider
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentArticleDetailBinding.inflate(layoutInflater)
        sharedElementEnterTransition = ChangeBounds().apply { duration = 500 }
        sharedElementReturnTransition = ChangeBounds().apply { duration = 500 }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val articleId = ArticleDetailFragmentArgs.fromBundle(arguments as Bundle).articleId

        viewModel.getArticleById(articleId).observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when(result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)

                        setupData(result.data)
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

    override fun onStop() {
        super.onStop()
        requireActivity().removeMenuProvider(menuProvider)
    }

    private fun setupData(article: ArticlesResponseItem) {
        binding.apply {
            tvTitle.text = article.title
            tvContent.text = article.content
            tvDate.text = article.createdAt
            if (!article.imageUrl.isNullOrEmpty()) {
                Glide.with(requireContext())
                    .load(article.imageUrl)
                    .into(image)
            } else {
                image.visibility = View.GONE
            }
        }
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

    private fun showLoading(state: Boolean) {
        if (state) {
            (activity as? MainActivity)?.showLoading(true)
        } else {
            (activity as? MainActivity)?.showLoading(false)
        }
    }
}