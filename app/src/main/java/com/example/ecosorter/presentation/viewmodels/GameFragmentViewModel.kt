package com.example.ecosorter.presentation.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecosorter.R
import com.example.ecosorter.data.GameRepositoryImpl
import com.example.ecosorter.domain.entity.GameResult
import com.example.ecosorter.domain.entity.Level
import com.example.ecosorter.domain.entity.Question
import com.example.ecosorter.domain.entity.TrashCategory
import com.example.ecosorter.domain.entity.WrongAnswer
import com.example.ecosorter.domain.usecase.GetCurrentGlobalStreakUseCase
import com.example.ecosorter.domain.usecase.GetGameResultUseCase
import com.example.ecosorter.domain.usecase.GetQuestionUseCase
import com.example.ecosorter.domain.usecase.SaveCurrentGlobalStreakUseCase

private const val MILLIS_IN_SECOND = 1000L

private const val SECONDS_IN_MINUTE = 60

class GameFragmentViewModel(val application: Application, val level: Level) : ViewModel() {
    private val repository = GameRepositoryImpl(application)
    private val timeInSec = level.gameTimeInSeconds

    private var currentGlobalStreak = 0
    private lateinit var timer: CountDownTimer
    private val getQuestionUseCase = GetQuestionUseCase(repository)
    private val getGameResultUseCase = GetGameResultUseCase(repository)
    private val getCurrentGlobalStreakUseCase = GetCurrentGlobalStreakUseCase(repository)
    private val saveCurrentGlobalStreakUseCase = SaveCurrentGlobalStreakUseCase(repository)

    private var questions = 0
    private var rightAnswers = 0
    private val wrongAnswers = mutableListOf<WrongAnswer>()

    private val _countOfQuestions = MutableLiveData<String>()
    val countOfQuestions: LiveData<String>
        get() = _countOfQuestions
    private val _progressAnswers = MutableLiveData<String>()
    val progressAnswers: LiveData<String>
        get() = _progressAnswers
    private val _question = MutableLiveData<Question>()
    val question: LiveData<Question>
        get() = _question

    private val _timerStr = MutableLiveData<String>()
    val timerStr: LiveData<String>
        get() = _timerStr

    private val _gameResult = MutableLiveData<GameResult>()
    val gameResult: LiveData<GameResult>
        get() = _gameResult

    private val _percentOfRightAnswers = MutableLiveData<Int>()
    val percentOfRightAnswers: LiveData<Int>
        get() = _percentOfRightAnswers

    private val _globalStreak = MutableLiveData<Int>()
    val globalStreak: LiveData<Int>
        get() = _globalStreak


    init {
        startGame()
    }

    private fun startGame() {
        generateQuestion()
        startTimer()
        currentGlobalStreak = getCurrentGlobalStreakUseCase(level)
        updateCurrentGlobalStreak()
    }

    private fun generateQuestion() {
        val question = getQuestionUseCase()
        _question.value = question
    }

    private fun startTimer() {
        timer = object : CountDownTimer(
            timeInSec * MILLIS_IN_SECOND,
            MILLIS_IN_SECOND
        ) {
            override fun onFinish() {
                finishGame()
            }

            override fun onTick(p0: Long) {
                _timerStr.value = formatTime(p0)
            }
        }
        timer.start()
    }

    @SuppressLint("DefaultLocale")
    private fun formatTime(p0: Long): String {
        val sec = p0 / MILLIS_IN_SECOND
        val min = sec / SECONDS_IN_MINUTE
        val leftSec = sec - (min * SECONDS_IN_MINUTE)
        return String.format("%02d:%02d", min, leftSec)
    }

    private fun finishGame() {
        generateGameResult()
    }

    private fun generateGameResult() {
        val gr = getGameResultUseCase(
            level,
            rightAnswers,
            questions,
            wrongAnswers,
            currentGlobalStreak
        )
        _gameResult.value = gr
    }

    fun chooseAnswer(trashCategory: TrashCategory) {
        checkAnswer(trashCategory)
        generateQuestion()
    }

    private fun checkAnswer(trashCategory: TrashCategory): Boolean {
        val currTrashItem = _question.value?.trashItem
        val isRight = currTrashItem?.category == trashCategory
        if (isRight) {
            updateCountRightAnswers()
            updateCurrentGlobalStreak()
        }
        else {
            currTrashItem?.let {
                val wrongAnswer = WrongAnswer(it, trashCategory)
                wrongAnswers.add(wrongAnswer)
            }
            resetCurrentGlobalStreak()
        }
        updateCountQuestions()
        return isRight
    }

    private fun resetCurrentGlobalStreak() {
        currentGlobalStreak = 0
        _globalStreak.value = currentGlobalStreak
        saveCurrentGlobalStreakUseCase(level, currentGlobalStreak)
    }

    private fun updateCurrentGlobalStreak() {
        currentGlobalStreak++
        _globalStreak.value = currentGlobalStreak
        saveCurrentGlobalStreakUseCase(level, currentGlobalStreak)
    }

    private fun updateCountRightAnswers() {
        rightAnswers++
        _progressAnswers.value = String.format(
            application.resources.getString(R.string.progress_answers_game),
            rightAnswers,
            level.minCountOfRightAnswers
        )
    }

    private fun updateCountQuestions() {
        questions++
        _countOfQuestions.value = String.format(
            application.resources.getString(R.string.question_number),
            questions
        )
        val percent = calcPercentOfRightAnswers()
        _percentOfRightAnswers.value = percent
    }

    private fun calcPercentOfRightAnswers(): Int {
        if (questions==0) return 0
        return (rightAnswers.toDouble() / questions * 100).toInt()
    }

    override fun onCleared() {
        timer.cancel()
    }
}