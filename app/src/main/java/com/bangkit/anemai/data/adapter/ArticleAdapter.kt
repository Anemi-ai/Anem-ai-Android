package com.bangkit.anemai.data.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import com.bangkit.anemai.R
import com.bangkit.anemai.BuildConfig
import com.bangkit.anemai.data.model.ArticleItem
import com.bangkit.anemai.databinding.CardArticleBinding
import com.bumptech.glide.Glide

class ArticleAdapter(private val onItemClick: (ArticleItem) -> Unit) : ListAdapter<ArticleItem, ArticleAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: CardArticleBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(article: ArticleItem) {
            binding.cardTitle.text = article.title
            Glide.with(itemView.context)
                .load(BuildConfig.BASE_URL_GENERAL + article.imageUrl)
                .placeholder(R.drawable.ic_place_holder)
                .into(binding.cardImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
        
        holder.itemView.setOnClickListener { onItemClick(article) }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ArticleItem>() {
            override fun areItemsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: ArticleItem, newItem: ArticleItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}