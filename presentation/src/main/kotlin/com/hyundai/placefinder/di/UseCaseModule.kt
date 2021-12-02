package com.hyundai.placefinder.di

import com.hyundai.domain.usecase.GetPlaceGroupListUseCase
import org.koin.dsl.module

val useCaseModule = module {

    single {
        GetPlaceGroupListUseCase(repository = get())
    }

}