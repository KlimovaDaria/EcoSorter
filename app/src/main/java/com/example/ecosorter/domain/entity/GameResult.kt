package com.example.ecosorter.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class GameResult(
    val winner: Boolean,
    val countOfRightAnswers: Int,
    val countOfQuestions: Int,
    val percentOfRightAnswers: Int,
    val level: Level,
    val wrongAnswersList: List<WrongAnswer>,
    val currentGlobalStreak: Int
): Parcelable