package com.hyundai.placefinder.di

import com.hyundai.placefinder.ui.main.PoiResultListAdapter
import org.koin.dsl.module

val appModule = module {

    factory {
        PoiResultListAdapter()
    }

}