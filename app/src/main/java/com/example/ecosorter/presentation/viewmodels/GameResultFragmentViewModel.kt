package com.example.ecosorter.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecosorter.data.GameRepositoryImpl
import com.example.ecosorter.domain.entity.GameResult
import com.example.ecosorter.domain.usecase.CheckAndSaveHighScoreUseCase
import com.example.ecosorter.domain.usecase.GetMaxGlobalStreakUseCase

class GameResultFragmentViewModel(application: Application, val gameResult: GameResult) : ViewModel() {
    private val repository = GameRepositoryImpl(application)
    private val checkAndSaveHighScoreUseCase = CheckAndSaveHighScoreUseCase(repository)
    private val getMaxGlobalStreakUseCase = GetMaxGlobalStreakUseCase(repository)

    private val _highScore = MutableLiveData<Int>()
    val highScore: LiveData<Int>
        get() = _highScore

    private val _maxGlobalStreak = MutableLiveData<Int>()
    val maxGlobalStreak: LiveData<Int>
        get()=_maxGlobalStreak

    private var levelName: String = gameResult.level.name

    init {
        setHighScore()
        val maxStreak = getMaxGlobalStreakUseCase(gameResult.level)
        _maxGlobalStreak.value = maxStreak
    }

    private fun setHighScore(){
        val highScore = checkAndSaveHighScoreUseCase(levelName, gameResult.countOfRightAnswers)
        _highScore.value = highScore
    }
}