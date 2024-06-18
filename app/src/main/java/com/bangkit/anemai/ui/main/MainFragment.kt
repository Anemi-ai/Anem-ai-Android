package com.bangkit.anemai.ui.main

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager
import com.bangkit.anemai.R
import com.bangkit.anemai.data.adapter.ArticleAdapter
import com.bangkit.anemai.data.model.ArticleItem
import com.bangkit.anemai.data.pref.UserPreference
import com.bangkit.anemai.data.pref.dataStore
import com.bangkit.anemai.databinding.FragmentMainBinding
import com.bangkit.anemai.ui.ViewModelFactory
import com.bangkit.anemai.ui.welcome.WelcomeActivity
import com.bangkit.anemai.utils.Result
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var menuProvider: MenuProvider
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext(), requireActivity().application)
    }

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

        setupAction(view)
        setupActionBar()
        setupName()

    }

    private fun setupName() {
        lifecycleScope.launch {
            val userPreference = UserPreference.getInstance(requireContext().dataStore)
            val user = userPreference.getSession().first()
            val idUser = user.id

            viewModel.getDetailUser(idUser)

            viewModel.userDetail.observe(viewLifecycleOwner) { userDetail ->
                binding.tvGreeetingName.text = userDetail.userResult!!.name
            }
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().removeMenuProvider(menuProvider)
    }

    private fun setupAction(view: View) {
        val extras = FragmentNavigatorExtras(
            binding.bgLayout to "bg_layout"
        )
        binding.btnHistory.setOnClickListener {
            view.findNavController().navigate(R.id.action_mainFragment_to_historyFragment, null, null, extras)
        }
        binding.btnCheckup.setOnClickListener {
            if (checkPermission(PERMISSION_CAMERA)) {
                view.findNavController().navigate(R.id.action_mainFragment_to_detectionFragment, null, null, extras)
            } else {
                requestCameraPermissionLauncher.launch(PERMISSION_CAMERA)
            }
        }
        binding.btnMore.setOnClickListener {
            val toArticleList = MainFragmentDirections.actionMainFragmentToArticleFragment()
            view.findNavController().navigate(toArticleList, extras)
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
                        viewModel.logout()
                        val intent = Intent(requireActivity(), WelcomeActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }

        requireActivity().addMenuProvider(menuProvider)
    }

    private fun setupArticle(articleList: List<ArticleItem>, view: View) {
        val extras = FragmentNavigatorExtras(
            binding.bgLayout to "bg_layout"
        )

        val adapter = ArticleAdapter { article ->
            val toArticleDetail = MainFragmentDirections.actionMainFragmentToArticleDetailFragment()
            toArticleDetail.articleId = article.id.toString()

            view.findNavController().navigate(toArticleDetail, extras)
        }

        binding.rvArticle.adapter = adapter
        adapter.submitList(articleList.subList(0,3))
    }

    private fun checkPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }

    private val requestCameraPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(requireContext(), "Permission granted", Toast.LENGTH_SHORT).show()
            }
        }

    private fun showLoading(state: Boolean) {
        if (state) {
            (activity as? MainActivity)?.showLoading(true)
        } else {
            (activity as? MainActivity)?.showLoading(false)
        }
    }

    companion object {
        private const val PERMISSION_CAMERA = Manifest.permission.CAMERA
    }
}