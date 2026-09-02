package com.example.ecosorter.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WrongAnswer(val trashItem: TrashItem, val wrongCategory: TrashCategory): Parcelable {
}