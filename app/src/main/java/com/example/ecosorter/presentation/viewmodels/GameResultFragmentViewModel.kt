package com.example.ecosorter.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecosorter.data.GameRepositoryImpl
import com.example.ecosorter.domain.entity.GameResult
import com.example.ecosorter.domain.usecase.CheckAndSaveHighScoreUseCase

class GameResultFragmentViewModel(application: Application, val gameResult: GameResult) : ViewModel() {
    private val repository = GameRepositoryImpl(application)
    private val checkAndSaveHighScoreUseCase = CheckAndSaveHighScoreUseCase(repository)

    private val _highScore = MutableLiveData<Int>()
    val highScore: LiveData<Int>
        get() = _highScore

    private var levelName: String = gameResult.level.name

    init {
        setHighScore()
    }

    private fun setHighScore(){
        val highScore = checkAndSaveHighScoreUseCase(levelName, gameResult.countOfRightAnswers)
        _highScore.value = highScore
    }
}