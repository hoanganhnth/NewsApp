package com.example.newsapp.ui.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.databinding.FragmentBreakingNewsBinding
import com.example.newsapp.databinding.FragmentSavedNewsBinding
import com.example.newsapp.ui.NewsActivity
import com.example.newsapp.ui.NewsViewModel
import com.google.android.material.snackbar.Snackbar


class SavedNewsFragment : Fragment() {

    lateinit var viewModel: NewsViewModel
    lateinit var newsAdapter: NewsAdapter
    lateinit var binding: FragmentSavedNewsBinding
    val TAG = "Save New Fragment"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Log.d(TAG,"onCreateView")
        binding = FragmentSavedNewsBinding.inflate(inflater, container, false)
        newsAdapter = NewsAdapter()
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as NewsActivity).viewModel
        setupRecyclerView()

        newsAdapter.setOnItemClickListener {
            try {
                val action = SavedNewsFragmentDirections.actionSavedNewsFragmentToArticleFragment(it)
                findNavController().navigate(action)
            } catch (e: NullPointerException) {
                Log.e(TAG, "Caught NullPointerException: ${e.message}")
                // Xử lý ngoại lệ ở đây nếu cần thiết
            }
        }
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition
                // TODO: Thực hiện thay đổi vị trí của item trong danh sách dữ liệu
                newsAdapter.differ.submitList(newsAdapter.differ.currentList.toMutableList().apply {
                    // Thực hiện swap
                    val item = removeAt(fromPosition)
                    add(toPosition, item)
                })
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = newsAdapter.differ.currentList[position]
                viewModel.deleteArticle(article)
                Snackbar.make(view, "Successfully deleted article", Snackbar.LENGTH_LONG).apply {
                    setAction("Undo") {
                        viewModel.saveArticle(article)
                    }
                    show()
                }
            }
        }
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvSavedNews)
        }

        viewModel.getArticle().observe(viewLifecycleOwner, Observer {
            newsAdapter.differ.submitList(it)
        })
    }
    private fun setupRecyclerView() {
        binding.rvSavedNews.adapter = newsAdapter
        binding.rvSavedNews.layoutManager = LinearLayoutManager(activity)
    }
}