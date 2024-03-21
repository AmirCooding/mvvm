package com.amircodeing.mvvm.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.amircodeing.mvvm.R
import com.amircodeing.mvvm.data.local.model.Note
import com.amircodeing.mvvm.databinding.ItemNoteBinding

class HomeAdaptor :
    ListAdapter<Note, HomeAdaptor.HomeViewHolder>(object : DiffUtil.ItemCallback<Note>() {
        override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean = newItem == oldItem
    }) {
    inner class HomeViewHolder(private val binding: ItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Note) {
            with(binding) {
                titleTV.text = item.title
                descriptionTV.text = item.description
                dateTV.text = StringBuilder().append("Create in : ").append(item.createDate)
                imgFavorite.setImageResource(if(item.isFavorite) R.drawable.ic_favorite_fill else R.drawable.ic_favorite_empty)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder =
        HomeViewHolder(ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) =
        holder.bind(getItem(position))




}