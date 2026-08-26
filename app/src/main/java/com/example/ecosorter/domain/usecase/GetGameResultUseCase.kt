package com.example.ecosorter.domain.usecase

import com.example.ecosorter.domain.entity.GameResult
import com.example.ecosorter.domain.entity.Level
import com.example.ecosorter.domain.repository.GameRepository

class GetGameResultUseCase(private val repository: GameRepository) {
    operator fun invoke(level: Level, countOfRightAnswers: Int, countOfQuestions: Int): GameResult{
        return repository.getGameResult(level, countOfRightAnswers, countOfQuestions)
    }
}