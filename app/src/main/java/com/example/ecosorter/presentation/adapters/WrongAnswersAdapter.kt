package com.example.ecosorter.presentation.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ecosorter.R
import com.example.ecosorter.databinding.ItemWrongAnswerBinding
import com.example.ecosorter.domain.entity.WrongAnswer


class WrongAnswersAdapter(private val wrongAnswers: List<WrongAnswer>, private val context: Context) :
    RecyclerView.Adapter<WrongAnswersAdapter.WrongAnswerViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WrongAnswerViewHolder {
        val binding = ItemWrongAnswerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return WrongAnswerViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: WrongAnswerViewHolder,
        position: Int
    ) {
        val wrongAnswer = wrongAnswers[position]
        holder.binding.tvWrongItemName.text = wrongAnswer.trashItem.name
        holder.binding.tvUserChoice.text = context.getString(
            R.string.your_choice_for_category,
            wrongAnswer.wrongCategory.russianName
        )
        holder.binding.tvRightChoice.text = context.getString(
            R.string.right_choice_for_category,
            wrongAnswer.trashItem.category.russianName
        )
    }

    override fun getItemCount(): Int {
        return wrongAnswers.size
    }

    class WrongAnswerViewHolder(val binding: ItemWrongAnswerBinding) :
        RecyclerView.ViewHolder(binding.root)
}