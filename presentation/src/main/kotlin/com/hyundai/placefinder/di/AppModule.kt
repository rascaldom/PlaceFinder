package com.hyundai.placefinder.di

import com.hyundai.placefinder.ui.group.GroupListAdapter
import com.hyundai.placefinder.ui.main.PoiResultListAdapter
import com.hyundai.placefinder.ui.place.PlaceListAdapter
import org.koin.dsl.module

val appModule = module {

    factory {
        PoiResultListAdapter()
    }

    factory {
        GroupListAdapter().apply { setHasStableIds(true) }
    }

    factory {
        PlaceListAdapter()
    }

}