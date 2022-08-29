package com.branch.core_database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.branch.core_database.CustomCoroutineRule
import com.branch.core_database.data.dao.MessagesDao
import com.branch.core_database.data.database.AppDatabase
import com.branch.core_database.data.entities.MessageEntity
import com.google.common.truth.Truth
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@OptIn(ExperimentalCoroutinesApi::class)
class MessagesDaoTest {

    private lateinit var messagegeDao: MessagesDao
    private lateinit var database: AppDatabase
    private val testContext: Context = ApplicationProvider.getApplicationContext()
    val sampleMessage = MessageEntity(
        id = 1,
        threadId = 1,
        body = "Halo there",
        agentId = "1",
        timeStamp = "2017-02-03T08:57:16.000Z",
        userId = "1"
    )

    @get:Rule
    val coroutineRule = CustomCoroutineRule()

    @Before
    fun setUp() {

        database = Room.inMemoryDatabaseBuilder(testContext, AppDatabase::class.java)
            .allowMainThreadQueries().build()
        messagegeDao = database.provideMessagesDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun insertAll_savesAllMessages() = runTest {
        messagegeDao.insertAll(listOf(sampleMessage))
        val query = messagegeDao.fetchAllMessages().first()

        Truth.assertThat(query).isNotEmpty()
        Truth.assertThat(query).contains(sampleMessage)
    }

    @Test
    fun shouldInsertSingleMessage() = runTest {
        messagegeDao.insert(sampleMessage)

        val query = messagegeDao.fetchAllMessages().first()
        Truth.assertThat(query).isNotEmpty()
        Truth.assertThat(query).contains(sampleMessage)
    }

    @Test
    fun shouldReturnAllInsertedMessages() = runTest {
        messagegeDao.insertAll(listOf(sampleMessage, sampleMessage.copy(id = 2)))
        val messages = messagegeDao.fetchAllMessages().first()

        Truth.assertThat(messages).containsExactly(sampleMessage, sampleMessage.copy(id = 2))
    }

    @Test
    fun shouldReturnMessagesInAThread() = runTest {
        messagegeDao.insertAll(listOf(sampleMessage, sampleMessage.copy(id = 2, threadId = 9)))
        val messages = messagegeDao.getMessagesByThread("9").first()

        Truth.assertThat(messages).isNotNull()
        Truth.assertThat(messages).containsExactly(sampleMessage.copy(id = 2, threadId = 9))
    }

    @Test
    fun shouldDeleteAllInsertedMessages() = runTest {
        messagegeDao.insertAll(listOf(sampleMessage, sampleMessage.copy(id = 2, threadId = 9)))
        messagegeDao.deleteAllMessages()

        val query = messagegeDao.fetchAllMessages().first()
        Truth.assertThat(query).isEmpty()
    }
}