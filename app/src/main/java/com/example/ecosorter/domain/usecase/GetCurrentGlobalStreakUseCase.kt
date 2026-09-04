package com.example.ecosorter.domain.usecase

import com.example.ecosorter.domain.entity.Level
import com.example.ecosorter.domain.repository.GameRepository

class GetCurrentGlobalStreakUseCase(private val repository: GameRepository) {
    operator fun invoke(level: Level): Int{
        return repository.getCurrentGlobalStreak(level)
    }
}