package com.hyundai.placefinder.di

import com.hyundai.data.datasource.LocalDataSource
import com.hyundai.data.datasource.LocalDataSourceImpl
import com.hyundai.data.local.AppDatabase
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val localModule = module {

    single {
        AppDatabase.buildDatabase(androidApplication())
    }

    single {
        get<AppDatabase>().placeDao()
    }

    single<LocalDataSource> {
        LocalDataSourceImpl(placeDao = get())
    }

}