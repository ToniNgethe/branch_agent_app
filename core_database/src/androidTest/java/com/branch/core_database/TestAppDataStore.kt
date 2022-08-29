package com.branch.core_database

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.branch.core_database.data.AppDataStoreImpl
import com.branch.core_database.domain.AppDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TestAppDataStore {
    lateinit var appDataStore: AppDataStore
    private val testContext: Context = ApplicationProvider.getApplicationContext()

    @get:Rule
    val customCoroutineRule = CustomCoroutineRule()

    @Before
    fun setUp() {
        appDataStore = AppDataStoreImpl(testContext)
    }

    @Test
    fun saveToken_returnsSavedToken() = runTest {
        appDataStore.saveToken("test_token")

        val token = appDataStore.getToken().first()
        assert(token == "test_token")
    }
}