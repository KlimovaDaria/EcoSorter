package com.example.ecosorter.domain.usecase

import com.example.ecosorter.domain.repository.GameRepository

class CheckAndSaveHighScoreUseCase(private val repository: GameRepository) {
    operator fun invoke(levelName: String, score: Int): Int{
        return repository.checkAndSaveHighScore(levelName, score)
    }
}