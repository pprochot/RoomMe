package uj.roomme.services.factory

import retrofit2.CallAdapter
import retrofit2.Retrofit
import uj.roomme.services.adapter.RoomMeCallAdapter
import uj.roomme.services.call.RoomMeCall
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.Executors

class RoomMeCallAdapterFactory private constructor() : CallAdapter.Factory() {

    override fun get(
        returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?
    ): CallAdapter<*, *>? {
        return returnType?.let {
            return try {
                val enclosingType = (it as ParameterizedType)

                if (enclosingType.rawType != RoomMeCall::class.java)
                    null
                else {
                    val type = enclosingType.actualTypeArguments[0]
                    if (retrofit?.callbackExecutor() != null) {
                        return RoomMeCallAdapter<Any>(type, retrofit.callbackExecutor()!!)
                    }
                    RoomMeCallAdapter<Any>(type, Executors.newSingleThreadExecutor())
                }
            } catch (ex: ClassCastException) {
                null
            }
        }
    }

    companion object {
        @JvmStatic
        fun create() = RoomMeCallAdapterFactory()
    }
}