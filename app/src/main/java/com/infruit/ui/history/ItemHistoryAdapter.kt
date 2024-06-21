package com.infruit.ui.history

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.infruit.data.model.history.DataItem
import com.infruit.databinding.ItemHistoryBinding
import com.infruit.ui.scan.ScanResultActivity

class ItemHistoryAdapter : ListAdapter<DataItem, ItemHistoryAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val history = getItem(position)
        holder.bind(history)
    }

    class MyViewHolder(val binding: ItemHistoryBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(history: DataItem) {
            Glide.with(binding.resultImage).load(history.image).into(binding.resultImage)
            if (history.label?.contains("Class") == true) {
                binding.gradeTextView.text = "Grade: ${history.label}"
            } else {
            }
            if (history.label?.contains("fresh") == true) {
                binding.gradeTextView.visibility = View.GONE
                val fruit = history.label.replace("fresh", "").trim().replaceFirstChar { it.uppercase() }
                val condition = "Fresh"
                binding.conditionTextView.text = "Kondisi: $condition"
                binding.fruitTextView.text = "Buah: $fruit"
            } else if (history.label?.contains("rotten") == true) {
                binding.gradeTextView.visibility = View.GONE
                val fruit = history.label.replace("rotten", "").trim().replaceFirstChar { it.uppercase() }
                val condition = "Rotten"
                binding.conditionTextView.text = "Kondisi: $condition"
                binding.fruitTextView.text = "Buah: $fruit"
            }
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, ScanResultActivity::class.java)
                intent.putExtra(ScanResultActivity.EXTRA_IMAGE, history.image)
                intent.putExtra(ScanResultActivity.EXTRA_SCORE, history.score)
                intent.putExtra(ScanResultActivity.EXTRA_LABEL, history.label)
                intent.putExtra(ScanResultActivity.EXTRA_RECOMMENDATION, history.recommendation)
                binding.root.context.startActivity(intent)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}