package com.example.ecosorter.domain.entity

enum class Level( val minCountOfRightAnswers: Int,
                  val minPercentOfRightAnswers: Int,
                  val gameTimeInSeconds: Int) {
    EASY(20, 60, 60),
    NORMAL(30, 75, 60),
    HARD(40, 90, 60)
}