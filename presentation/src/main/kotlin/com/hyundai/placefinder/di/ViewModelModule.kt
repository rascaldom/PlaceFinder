package com.hyundai.placefinder.di

import com.hyundai.placefinder.viewmodel.GroupListViewModel
import com.hyundai.placefinder.viewmodel.MainViewModel
import com.hyundai.placefinder.viewmodel.PlaceListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        MainViewModel(groupUseCase = get(), placeUseCase = get())
    }

    viewModel {
        GroupListViewModel(groupUseCase = get())
    }

    viewModel {
        PlaceListViewModel(groupUseCase = get(), placeUseCase = get())
    }

}