package com.samsad.mvvvmnewsapp.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//RequestType - Type we get from the REST api
//We are passing different functions which have different job
inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,//Database Request
    crossinline fetch: suspend () -> RequestType,//fetch new data from the rest api
    crossinline saveFetchResult: suspend (RequestType) -> Unit,//Storing the data from rest api to database
    crossinline shouldFetch: (ResultType) -> Boolean = { true }//
) = channelFlow {
    val data = query().first()

    if (shouldFetch(data)) {
        val loading = launch {
            query().collect {
                send(Resource.Loading(it))
            }
        }

        try {
            kotlinx.coroutines.delay(2000)
            saveFetchResult(fetch())
            loading.cancel()
            query().collect {
                send(Resource.Success(it))
            }
        } catch (t: Throwable) {
            loading.cancel()
            query().collect {
                send(Resource.Error(t, it))
            }
        }
    } else {
        query().collect {
            send(Resource.Success(it))
        }
    }
}