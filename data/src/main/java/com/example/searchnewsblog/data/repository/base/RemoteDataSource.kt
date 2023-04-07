package com.example.searchnewsblog.data.repository.base

import com.example.searchnewsblog.data.exception.RemoteApiException
import com.example.searchnewsblog.data.repository.Util
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.awaitResponse
import timber.log.Timber
import java.io.IOException

@Suppress("ASSIGNED_BUT_NEVER_ACCESSED_VARIABLE")
abstract class RemoteDataSource<Param, Response, Result> : DataSource<Param, Result>() {

    //======================================================================
    // Abstract Methods
    //======================================================================

    protected abstract fun fetchApi(param: Param?): Call<Response>

    abstract fun toObject(raw: Response?): Result?

    //======================================================================
    // Override Methods
    //======================================================================

    @Suppress("BlockingMethodInNonBlockingContext")
    override fun execute(param: Param): Flow<Result> {
        return flow {
            val call = fetchApi(param).awaitResponse()
            val code = call.code()
            val isSuccessful = call.isSuccessful
            if (isSuccessful.not()) {
                throw HttpException(call)
            }
            val rawResponse = call.body()
            success(rawResponse)
            val result = toObjectInternal(rawResponse)
            Timber.w("result >> ${result != null}, code : $code, isSuccessful : $isSuccessful")
            emit(result!!)
        }.catch { e ->
            throw parserErrorCode(e)
        }.flowOn(Dispatchers.IO)
    }

    //======================================================================
    // Public Methods
    //======================================================================

    @Suppress("UNCHECKED_CAST")
    fun execute(init: Param.() -> Unit): Flow<Result> {
        val p: Param = Util.getReclusiveGenericClass(this::class.java, 0).newInstance() as Param
        p?.init()
        return execute(p)
    }

    @Throws(IOException::class)
    open fun success(response: Response?) {
        // Nothing
    }

    //======================================================================
    // Private Methods
    //======================================================================

    @Throws(IOException::class)
    private fun toObjectInternal(raw: Response?): Result? {
        @Suppress("UNREACHABLE_CODE") return try {
            return toObject(raw)
        } catch (e: Exception) {
            throw IllegalAccessException(e.message)
        }
    }

    private fun parserErrorCode(
        throwable: Throwable,
    ): Throwable {
        Timber.e("parserErrorCode >> $throwable")
        return when (throwable) {
            is HttpException -> {
                val result = throwable.response()?.let {
                    it.errorBody()?.let {
                        try {
                            Gson().fromJson(it.string(), HashMap::class.java)
                        } catch (e: Exception) {
                            emptyMap<String, String>()
                        }
                    }
                }
                RemoteApiException.HttpException(
                    throwable,
                    throwable.code(),
                    (result?.get("code") as? String).orEmpty(),
                    (result?.get("msg") as? String).orEmpty()
                )
            }
            else -> {
                RemoteApiException.OtherException(throwable)
            }
        }
    }
}