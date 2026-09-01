package com.example.ecosorter.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecosorter.domain.entity.GameResult

class GameResultFragmentViewModelFactory(val application: Application, val gameResult: GameResult):
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameResultFragmentViewModel::class.java)){
            return GameResultFragmentViewModel(application, gameResult) as T
        }
        throw RuntimeException("unknown view model $modelClass")
    }
}