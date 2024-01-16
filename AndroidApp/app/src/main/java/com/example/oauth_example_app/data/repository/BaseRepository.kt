package com.example.oauth_example_app.data.repository

import retrofit2.HttpException
import com.example.oauth_example_app.data.network.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class BaseRepository {
    open suspend fun <T> safeApiCall(
        apiCall: suspend () -> T
    ) : Resource<T> {
        return withContext(Dispatchers.IO){
            try{
                Resource.Success(apiCall.invoke())

            } catch (throwable: Throwable){
                when(throwable){
                    is HttpException -> {
                        Resource.Failure(false, throwable.code(), throwable.response()?.errorBody())
                    }
                    else -> {
                        Resource.Failure(true, null, null)
                    }
                }
            }
        }
    }


}