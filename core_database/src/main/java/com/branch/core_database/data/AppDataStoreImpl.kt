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
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "branch_customer_service")

class AppDataStoreImpl @Inject constructor(
    val context: Context, val appDispatchers: AppDispatchers
) : AppDataStore {

    private val TOKEN_KEY = stringPreferencesKey("token")

    override var token: String?
        get() {
            var getToken: String? = null
            val job = CoroutineScope(appDispatchers.io()).launch {
                getToken = context.dataStore.data.map { prefs ->
                    prefs[TOKEN_KEY]
                }.first()
            }
            job.cancel()
            return getToken
        }
        set(value) {
            val job = CoroutineScope(appDispatchers.io()).launch {
                context.dataStore.edit { prefs ->
                    prefs[TOKEN_KEY] = value!!
                }
            }
            job.cancel()
        }
}