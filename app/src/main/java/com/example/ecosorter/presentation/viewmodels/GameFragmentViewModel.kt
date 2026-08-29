package com.example.ecosorter.presentation.viewmodels

import android.annotation.SuppressLint
import android.app.Application
import android.os.CountDownTimer
import com.example.ecosorter.R
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecosorter.data.GameRepositoryImpl
import com.example.ecosorter.domain.entity.GameResult
import com.example.ecosorter.domain.entity.Level
import com.example.ecosorter.domain.entity.Question
import com.example.ecosorter.domain.entity.TrashCategory
import com.example.ecosorter.domain.usecase.GetGameResultUseCase
import com.example.ecosorter.domain.usecase.GetQuestionUseCase

private const val MILLIS_IN_SECOND = 1000L

private const val SECONDS_IN_MINUTE = 60

class GameFragmentViewModel(val application: Application, val level: Level) : ViewModel() {
    private val repository = GameRepositoryImpl()
    private val timeInSec = level.gameTimeInSeconds
    private lateinit var timer: CountDownTimer
    private val getQuestionUseCase = GetQuestionUseCase(repository)
    private val getGameResultUseCase = GetGameResultUseCase(repository)

    private var questions = 0
    private var rightAnswers = 0

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

    private val _percentOfRightAnswers = MutableLiveData<String>()
    val percentOfRightAnswers: LiveData<String>
        get() = _percentOfRightAnswers


    init {
        startGame()
    }

    private fun startGame() {
        generateQuestion()
        startTimer()
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
            questions
        )
        _gameResult.value = gr
    }

    fun chooseAnswer(trashCategory: TrashCategory) {
        checkAnswer(trashCategory)
        generateQuestion()
    }

    private fun checkAnswer(trashCategory: TrashCategory): Boolean {
        val isRight = question.value?.trashItem?.category == trashCategory
        if (isRight) {
            updateCountRightAnswers()
        }
        updateCountQuestions()
        return isRight
    }

    private fun updateCountRightAnswers() {
        rightAnswers++
        _progressAnswers.value = String.format(
            application.resources.getString(R.string.progress_answers),
            rightAnswers,
            level.minCountOfRightAnswers
        )
    }

    private fun updateCountQuestions() {
        questions++
        _countOfQuestions.value = questions.toString()
        val percent = calcPercentOfRightAnswers()
        _percentOfRightAnswers.value = String.format(
            application.resources.getString(R.string.percent_right_answers),
            percent,
            level.minPercentOfRightAnswers
        )
    }

    private fun calcPercentOfRightAnswers(): Int {
        if (questions==0) return 0
        return (rightAnswers.toDouble() / questions * 100).toInt()
    }

    override fun onCleared() {
        timer.cancel()
    }
}