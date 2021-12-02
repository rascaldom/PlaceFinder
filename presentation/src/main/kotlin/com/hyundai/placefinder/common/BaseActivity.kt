package com.hyundai.placefinder.common

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

abstract class BaseActivity : AppCompatActivity() {

    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes layoutId: Int): Lazy<T> =
        lazy { DataBindingUtil.setContentView(this, layoutId) }

}