package com.example.ecosorter.domain.entity

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class TrashItem(
    val name: String,
    val category: TrashCategory,
    val imageResId: Int
): Parcelable
