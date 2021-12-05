package com.hyundai.placefinder.di

import com.hyundai.data.repository.RepositoryImpl
import com.hyundai.domain.repository.Repository
import org.koin.dsl.module

val repositoryModule = module {

    single<Repository> {
        RepositoryImpl(localDataSource = get())
    }

}