package com.example.ecosorter.domain.usecase

import com.example.ecosorter.domain.repository.GameRepository

class GetHighScoreForCurrentLevelUseCase(private val repository: GameRepository) {
    operator fun invoke(levelName: String): Int{
        return repository.getHighScoreForCurrentLevel(levelName)
    }
}