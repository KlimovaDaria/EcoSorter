package com.example.ecosorter.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
enum class Level( val minCountOfRightAnswers: Int,
                  val minPercentOfRightAnswers: Int,
                  val gameTimeInSeconds: Int): Parcelable {
    EASY(20, 60, 60),
    NORMAL(30, 75, 60),
    HARD(40, 90, 60)
}