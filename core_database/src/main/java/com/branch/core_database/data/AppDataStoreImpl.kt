package com.branch.core_database.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.branch.core_database.domain.AppDataStore
import com.branch.core_utils.utils.AppDispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "branch_customer_service")

class AppDataStoreImpl @Inject constructor(
    val context: Context
) : AppDataStore {

    private val TOKEN_KEY = stringPreferencesKey("token")

    override suspend fun saveToken(token: String) {
        context.dataStore.edit { prefs ->
            prefs[TOKEN_KEY] = token
        }
    }

    override fun getToken(): Flow<String?>  = context.dataStore.data.map { prefs ->
        prefs[TOKEN_KEY]
    }
}