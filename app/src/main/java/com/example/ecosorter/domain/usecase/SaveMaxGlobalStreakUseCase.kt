package com.example.ecosorter.domain.usecase

import com.example.ecosorter.domain.entity.Level
import com.example.ecosorter.domain.repository.GameRepository

class SaveMaxGlobalStreakUseCase(private val repository: GameRepository) {
    operator fun invoke(level: Level, maxStreak: Int){
        repository.saveMaxGlobalStreak(level, maxStreak)
    }
}