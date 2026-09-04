package com.example.ecosorter.domain.usecase

import com.example.ecosorter.domain.entity.Level
import com.example.ecosorter.domain.repository.GameRepository

class SaveCurrentGlobalStreakUseCase(private val repository: GameRepository) {
    operator fun invoke(level: Level, currentStreak: Int){
        repository.saveCurrentGlobalStreak(level, currentStreak)
    }
}