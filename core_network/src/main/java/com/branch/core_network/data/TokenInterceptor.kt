package com.branch.core_network.data

import com.branch.core_database.domain.AppDataStore
import com.branch.core_utils.utils.AppDispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    val appDisPatcher: AppDispatchers, private val appDataStore: AppDataStore
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = runBlocking {
            appDataStore.getToken().first()
        }
        return if (token != null) {
            val newRequest =
                chain.request().newBuilder().addHeader("X-Branch-Auth-Token", token).build()
            chain.proceed(newRequest)
        } else {
            chain.proceed(chain.request())
        }

    }
}