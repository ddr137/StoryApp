package com.ddr1.storyapp.utils

sealed class BaseResult <out T : Any> {
    data class Success <T: Any>(val data : T) : BaseResult<T>()
    data class Error <T : Any>(val rawResponse: T) : BaseResult<T>()
}