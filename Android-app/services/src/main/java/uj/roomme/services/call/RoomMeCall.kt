package uj.roomme.services.call

import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.Executor

class RoomMeCall<R>(call: Call<R>, private val executor: Executor) : AbstractCall<R>(call) {

    override fun handleResponse(response: Response<R>?, handler: (Int?, R?, Throwable?) -> Unit) {
        executor.execute {
            when (val code = response?.code()) {
                200 -> handler(code, response.body(), null)
                in 400..511 -> handler(code, null, HttpException(response!!))
                else -> handler(code, response?.body(), null)
            }
        }
    }
}
