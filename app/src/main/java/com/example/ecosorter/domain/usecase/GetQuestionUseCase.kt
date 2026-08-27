package com.example.ecosorter.domain.usecase

import com.example.ecosorter.domain.entity.Question
import com.example.ecosorter.domain.repository.GameRepository

class GetQuestionUseCase(private val repository: GameRepository) {
    operator fun invoke(): Question{
        return repository.getQuestion()
    }
}