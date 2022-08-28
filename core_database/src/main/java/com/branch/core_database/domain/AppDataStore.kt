package com.branch.core_database.domain

import kotlinx.coroutines.flow.Flow

interface AppDataStore {
    suspend fun saveToken( token:String )
    fun getToken( ) : Flow<String?>
}