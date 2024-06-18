package com.bangkit.anemai.ui.history

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
import com.bangkit.anemai.data.adapter.HistoryAdapter
import com.bangkit.anemai.data.model.DetectionResponse
import com.bangkit.anemai.databinding.FragmentHistoryBinding
import com.bangkit.anemai.ui.ViewModelFactory
import com.bangkit.anemai.ui.main.MainViewModel
import com.bangkit.anemai.ui.welcome.WelcomeActivity
import com.bangkit.anemai.utils.Result


class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var menuProvider: MenuProvider
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(requireContext(), requireActivity().application)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHistoryBinding.inflate(layoutInflater)
        sharedElementEnterTransition = ChangeBounds().apply { duration = 750 }
        sharedElementReturnTransition = ChangeBounds().apply { duration = 750 }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getHistory().observe(viewLifecycleOwner) { result ->
            if (result != null) {
                when(result) {
                    is Result.Loading -> showLoading(true)
                    is Result.Success -> {
                        showLoading(false)

                        setupHistoryData(result.data, view)
                    }
                    is Result.Error -> {
                        showLoading(false)

                        AlertDialog.Builder(context).apply {
                            setMessage(result.error)
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

    private fun setupHistoryData(detectionResponse: List<DetectionResponse>, view: View) {
        val extras = FragmentNavigatorExtras(
            binding.bgLayout to "bg_layout"
        )

        val adapter = HistoryAdapter { detection ->
            val toDetailFragment = HistoryFragmentDirections.actionHistoryFragmentToHistoryDetailFragment(detection)
            view.findNavController().navigate(toDetailFragment, extras)
        }
        binding.rvHistory.adapter = adapter
        adapter.submitList(detectionResponse)
    }

    private fun setupActionbar() {
        (activity as AppCompatActivity).supportActionBar?.apply {
            title = getString(R.string.history_detection)
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
            (activity as? WelcomeActivity)?.showLoading(true)
        } else {
            (activity as? WelcomeActivity)?.showLoading(false)
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().removeMenuProvider(menuProvider)
    }
}
