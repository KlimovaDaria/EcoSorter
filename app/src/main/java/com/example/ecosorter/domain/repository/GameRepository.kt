package com.example.ecosorter.domain.repository

import com.example.ecosorter.domain.entity.GameResult
import com.example.ecosorter.domain.entity.Level
import com.example.ecosorter.domain.entity.Question
import com.example.ecosorter.domain.entity.WrongAnswer

interface GameRepository {
    fun getQuestion(): Question
    fun getGameResult(
        level: Level,
        countOfRightAnswers: Int,
        countOfQuestions: Int,
        wrongAnswers: List<WrongAnswer>,
        currentGlobalStreak: Int
    ): GameResult

    fun checkAndSaveHighScore(levelName: String, score: Int): Int

    fun getCurrentGlobalStreak(level: Level): Int

    fun saveCurrentGlobalStreak(level: Level, currentStreak: Int)

    fun getMaxGlobalStreak(level: Level): Int
    fun saveMaxGlobalStreak(level: Level, maxStreak: Int)
}