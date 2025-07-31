package com.ityu.studymvvmandroid.data.remote

import com.google.gson.annotations.SerializedName

class HttpResult<T>(
    @SerializedName(value = "code", alternate = ["state"]) var code: Int,
    @SerializedName(value = "msg", alternate = ["message"]) var msg: String?
) {
    companion object {
        const val HTTP_SUCCESS: Int = 200
        const val HTTP_FAIL: Int = -1
        const val TOKEN_EXPIRED = 300
    }

    @SerializedName(value = "data", alternate = ["result"])
    var data: T? = null

    fun isOk(): Boolean {
        return code == HTTP_SUCCESS || code == 0 // Consider 0 as success too
    }

    fun isEmpty(): Boolean {
        return data == null
    }

    override fun toString(): String { // Helpful for debugging
        return "HttpResult(code=$code, msg=$msg, data=$data)"
    }
}