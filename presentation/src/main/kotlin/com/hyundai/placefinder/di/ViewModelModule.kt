package com.hyundai.placefinder.di

import com.hyundai.placefinder.viewmodel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainViewModel(getPlaceGroupListUseCase = get())
    }

}