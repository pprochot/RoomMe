package uj.roomme.services.adapter

import retrofit2.Call
import retrofit2.CallAdapter
import uj.roomme.services.call.RoomMeCall
import java.lang.reflect.Type
import java.util.concurrent.Executor

class RoomMeCallAdapter<R>(private val responseType: Type, private val executor: Executor) :
    CallAdapter<R, Any> {

    override fun responseType(): Type = responseType

    override fun adapt(call: Call<R>): Any = RoomMeCall(call, executor)
}