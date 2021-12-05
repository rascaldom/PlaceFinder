package com.hyundai.placefinder.di

import com.hyundai.domain.usecase.GroupUseCase
import com.hyundai.domain.usecase.PlaceUseCase
import org.koin.dsl.module

val useCaseModule = module {

    single {
        GroupUseCase(repository = get())
    }

    single {
        PlaceUseCase(repository = get())
    }

}