package com.example.ecosorter.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
enum class Level( val minCountOfRightAnswers: Int,
                  val minPercentOfRightAnswers: Int,
                  val gameTimeInSeconds: Int): Parcelable {
    EASY(5, 70, 20),
    NORMAL(20, 85, 60),
    HARD(50, 90, 90)
}