package com.example.ecosorter.domain.entity

data class GameResult(
    val winner: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestions: Int,
    val percentOfRightAnswers: Double,
    val level: Level
)