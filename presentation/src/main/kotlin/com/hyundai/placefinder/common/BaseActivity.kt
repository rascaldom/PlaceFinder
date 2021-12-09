package com.hyundai.placefinder.common

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.gson.Gson

abstract class BaseActivity : AppCompatActivity() {

    /*
    inline : 해당 함수를 호출한 식을 모두 함수 본문으로 변경, 인라인 함수의 본문을 구현한 바이트코드를
             그 함수가 호출되는 모든 지점에 삽입한다.
             함수가 람다를 인자로 사용하는 경우 그 함수를 인라인 함수로 만들면
             람다 코드도 함께 인라이닝되고 그에 따라 무명 클래스와 객체가 생성되지 않아서 성능이 더 좋아진다

     */
    // reified : 타입 실체화
    /*
    // inline reified : 제네릭 T 를 파라미터로 넘겨줄 필요 없이 런타임에 타입 T 에 접근 가능
                        컴파일러는 실체화한 타입 인자를 사용해 인라인 함수를 호출하는 각 부분의 정확한 타입 인자를 알 수 있다.
                        따라서 함수 본문에는 명시적으로 타입을 전달하지 않아도 된다
     */
    // reified 타입 파라미터로 작성된 인라인 함수는 자바 코드에서 호출 할 수 없다 (코틀린과 인라인 방식이 다르다)
    protected inline fun <reified T : ViewDataBinding> binding(@LayoutRes layoutId: Int): Lazy<T> =
        lazy { DataBindingUtil.setContentView(this, layoutId) }

    inline fun <reified T : Any> T.json(): String = Gson().toJson(this, T::class.java)

}