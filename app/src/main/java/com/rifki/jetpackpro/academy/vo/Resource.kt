package com.rifki.jetpackpro.academy.vo

import com.rifki.jetpackpro.academy.vo.Status.SUCCESS
import com.rifki.jetpackpro.academy.vo.Status.ERROR
import com.rifki.jetpackpro.academy.vo.Status.LOADING

//Kelas Resource berfungsi sebagai pembungkus data yang akan dikelola dan ditampilkan
data class Resource<T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Resource<T> = Resource(SUCCESS, data, null)

        fun <T> error(msg: String?, data: T?): Resource<T> = Resource(ERROR, data, msg)

        fun <T> loading(data: T?): Resource<T> = Resource(LOADING, data, null)
    }
}