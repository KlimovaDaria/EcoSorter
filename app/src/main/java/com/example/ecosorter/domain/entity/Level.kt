package com.example.ecosorter.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
enum class Level( val minCountOfRightAnswers: Int,
                  val minPercentOfRightAnswers: Int,
                  val gameTimeInSeconds: Int): Parcelable {
    EASY(5, 60, 20),
    NORMAL(30, 75, 60),
    HARD(40, 90, 60)
}