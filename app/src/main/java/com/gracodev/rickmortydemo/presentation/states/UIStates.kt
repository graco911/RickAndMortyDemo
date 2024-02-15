package com.gracodev.rickmortydemo.presentation.states

open class UIStates<out T: Any> {
    object Init : UIStates<Nothing>()
    object Loading : UIStates<Nothing>()
    class Success<T: Any>(val value: T?) : UIStates<T>()
    class Error(val message: String) : UIStates<Nothing>()
}