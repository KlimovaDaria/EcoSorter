package com.example.ecosorter.presentation.viewmodels

import android.app.Application
import android.content.Context
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecosorter.data.GameRepositoryImpl
import com.example.ecosorter.domain.entity.GameResult
import com.example.ecosorter.domain.usecase.GetHighScoreForCurrentLevelUseCase

class GameResultFragmentViewModel(private val application: Application, val gameResult: GameResult) : ViewModel() {
    private val repository = GameRepositoryImpl(application)
    private val getHighScoreForCurrentLevelUseCase = GetHighScoreForCurrentLevelUseCase(repository)


    private val _highScore = MutableLiveData<Int>()
    val highScore: LiveData<Int>
        get() = _highScore

    private var levelName: String = gameResult.level.name

    init {
        setHighScore()
    }

    private fun checkAndSaveNewHighScore(currentScore: Int) {
        val sharedPrefs = application.getSharedPreferences(GameRepositoryImpl.PREFS_NAME,
            Context.MODE_PRIVATE)
        val key = levelName
        val currentHighScore = getHighScoreForCurrentLevelUseCase(levelName)
        if (currentScore > currentHighScore) {
            sharedPrefs.edit {
                putInt(key, currentScore)
            }
        }
    }

    private fun setHighScore(){
        checkAndSaveNewHighScore(gameResult.countOfRightAnswers)
        _highScore.value =  getHighScoreForCurrentLevelUseCase(levelName)
    }
}