package com.example.mycodingapp.ui.language

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mycodingapp.databinding.ItemLanguageBinding
import com.example.mycodingapp.model.Language

class LanguageRecyclerViewAdapter (
    private val languages: MutableList<Language>,
    private val onDeleteClickListener: (Language) -> Unit,
    private val onEditLongClickListener: (Language) -> Unit

) : RecyclerView.Adapter<LanguageRecyclerViewAdapter.LanguageViewHolder>() {

    inner class LanguageViewHolder(val binding: ItemLanguageBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LanguageViewHolder {
        val binding = ItemLanguageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LanguageViewHolder(binding)
    }

    override fun getItemCount(): Int = languages.size

    override fun onBindViewHolder(holder: LanguageViewHolder, position: Int) {
        val language = languages[position]
        holder.binding.tvLanguage.text = language.nombre
        holder.binding.btnDeleteLanguage.setOnClickListener {
            onDeleteClickListener(language)
        }
        holder.binding.root.setOnLongClickListener {
            onEditLongClickListener(language)
            true
        }
    }

    fun removeLanguage(language: Language) {
        val index = languages.indexOf(language)
        if (index != -1) {
            languages.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}