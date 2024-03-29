package com.example.newsapp.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.models.Article
import com.example.newsapp.databinding.ItemArticlePreviewBinding

class NewsAdapter : RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    val TAG = "NewsAdapter"
    inner class ArticleViewHolder(val binding: ItemArticlePreviewBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(article: Article, onItemClickListener: ((Article) -> Unit)?) {
            binding.article = article
            Glide.with(itemView.context)
                .load(article.urlToImage)
                .centerCrop()
                .into(binding.ivArticleImage)
            binding.executePendingBindings()
            itemView.setOnClickListener {
                onItemClickListener?.let {
                    it(article)
                }
            }
        }
    }
    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }
    }
    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        val binding =  ItemArticlePreviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
       return ArticleViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        holder.bind(currentItem, onItemClickListener)
    }
    override fun getItemCount(): Int {
        return differ.currentList.size

    }

    private var onItemClickListener: ((Article) -> Unit)? = null
    fun setOnItemClickListener(listener: (Article) -> Unit) {
        onItemClickListener = listener
    }
}