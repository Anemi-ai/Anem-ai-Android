package com.bangkit.anemai.data.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bangkit.anemai.R
import com.bangkit.anemai.data.model.AnemiaDetection
import com.bangkit.anemai.databinding.CardHistoryBinding

class HistoryAdapter(private val onItemClick: (AnemiaDetection) -> Unit): ListAdapter<AnemiaDetection, HistoryAdapter.ViewHolder>(DIFF_CALLBACK) {
    class ViewHolder(private val binding: CardHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AnemiaDetection) {
            binding.apply {
                cardTitle.text = item.createdAt
                cardResult.text = item.result
                mainLayout.background = if (item.result == "anemia")
                    ColorDrawable(ContextCompat.getColor(itemView.context, R.color.light_red))
                else
                    ColorDrawable(ContextCompat.getColor(itemView.context, R.color.light_blue))
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val detection = getItem(position)
        holder.bind(detection)

        holder.itemView.setOnClickListener { onItemClick(detection) }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AnemiaDetection>() {
            override fun areItemsTheSame(oldItem: AnemiaDetection, newItem: AnemiaDetection): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: AnemiaDetection, newItem: AnemiaDetection): Boolean {
                return oldItem == newItem
            }

        }
    }
}