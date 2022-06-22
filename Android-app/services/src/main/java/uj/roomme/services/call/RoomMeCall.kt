package uj.roomme.services.call

import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor

class RoomMeCall<R>(private val call: Call<R>, private val executor: Executor) {

    fun processSync(responseHandler: (Int?, R?, Throwable?) -> Unit) {
        try {
            val response = call.execute()
            handleResponse(response, responseHandler)
        } catch (t: IOException) {
            responseHandler(null, null, t)
        }
    }

    fun processAsync(responseHandler: (Int?, R?, Throwable?) -> Unit) {
        val callback = object : Callback<R> {
            override fun onResponse(call: Call<R>?, r: Response<R>?) =
                handleResponse(r, responseHandler)

            override fun onFailure(call: Call<R>?, t: Throwable?) {
                executor.execute { responseHandler(null, null, t) }
            }
        }
        call.enqueue(callback)
    }

    fun handleResponse(response: Response<R>?, handler: (Int?, R?, Throwable?) -> Unit) {
        executor.execute {
            when (val code = response?.code()) {
                200 -> handler(code, response.body(), null)
                in 400..511 -> handler(code, null, HttpException(response!!))
                else -> handler(code, response?.body(), null)
            }
        }
    }
}
