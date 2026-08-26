package com.example.ecosorter.domain.entity

data class Question(
    val trashItem: TrashItem,
    val options: List<TrashCategory>
)