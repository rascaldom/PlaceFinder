package com.hyundai.placefinder.common

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson

abstract class BaseActivity : AppCompatActivity() {

    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes layoutId: Int): Lazy<T> =
        lazy { DataBindingUtil.setContentView(this, layoutId) }

    inline fun <reified T : Any> T.json(): String = Gson().toJson(this, T::class.java)

}