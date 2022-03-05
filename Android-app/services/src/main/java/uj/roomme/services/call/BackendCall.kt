package uj.roomme.services.call

interface BackendCall<R> {

    fun processSync(responseHandler: (Int?, R?, Throwable?) -> Unit)

    fun processAsync(responseHandler: (Int?, R?, Throwable?) -> Unit)
}