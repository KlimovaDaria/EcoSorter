package com.example.ecosorter.domain.repository

import com.example.ecosorter.domain.entity.GameResult
import com.example.ecosorter.domain.entity.Level
import com.example.ecosorter.domain.entity.Question

interface GameRepository {
    fun getQuestion(): Question
    fun getGameResult(level: Level, countOfRightAnswers: Int, countOfQuestions: Int): GameResult
    fun checkAndSaveHighScore(levelName: String, score: Int): Int
}