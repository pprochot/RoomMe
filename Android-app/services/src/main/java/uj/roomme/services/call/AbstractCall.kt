package uj.roomme.services.call

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor

abstract class AbstractCall<R>(private val call: Call<R>) : BackendCall<R> {

    override fun processSync(responseHandler: (Int?, R?, Throwable?) -> Unit) {
        try {
            val response = call.execute()
            handleResponse(response, responseHandler)
        } catch (t: IOException) {
            responseHandler(null, null, t)
        }
    }

    override fun processAsync(responseHandler: (Int?, R?, Throwable?) -> Unit) {

        val callback = object : Callback<R> {
            override fun onResponse(call: Call<R>?, r: Response<R>?) =
                handleResponse(r, responseHandler)

            override fun onFailure(call: Call<R>?, t: Throwable?) =
                responseHandler(null, null, t)
        }
        call.enqueue(callback)
    }

    protected abstract fun handleResponse(response: Response<R>?, handler: (Int?, R?, Throwable?) -> Unit)
}