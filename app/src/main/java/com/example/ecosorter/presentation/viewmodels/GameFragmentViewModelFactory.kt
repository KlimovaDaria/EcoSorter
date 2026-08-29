package com.example.ecosorter.presentation.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecosorter.domain.entity.Level

class GameFragmentViewModelFactory(val application: Application, val level: Level) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GameFragmentViewModel::class.java)){
            return GameFragmentViewModel(application, level) as T
        }
        throw RuntimeException("unknown view model $modelClass")
    }
}