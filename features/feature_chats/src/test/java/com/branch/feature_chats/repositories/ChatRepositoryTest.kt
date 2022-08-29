package com.branch.feature_chats.repositories

import com.branch.core_database.data.dao.MessagesDao
import com.branch.core_network.data.ResponseState
import com.branch.core_utils.utils.AppDispatchers
import com.branch.feature_chats.TestUtiDispatchers
import com.branch.feature_chats.data.api.MessageApi
import com.branch.feature_chats.data.dto.MessagesResponseDto
import com.branch.feature_chats.data.mappers.toMessage
import com.branch.feature_chats.data.mappers.toMessageEntity
import com.branch.feature_chats.data.reposotories.ChatRepositoryImpl
import com.branch.feature_chats.domain.repositories.ChatRepository
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatRepositoryTest {
    private lateinit var msgApi: MessageApi
    private lateinit var messagesDao: MessagesDao
    private lateinit var appDispatchers: AppDispatchers
    private lateinit var chatRepository: ChatRepository

    // given
    private val messagesResponseDto = MessagesResponseDto(
        agentId = "1",
        body = "message",
        id = 1,
        threadId = 9,
        timestamp = "2017-02-01T16:08:21.000Z",
        userId = "3"
    )
    private val messagesResponseDto2 =
        messagesResponseDto.copy(id = 2, timestamp = "2018-02-03T14:23:45.000Z", threadId = 1)
    private val messagesResponseDto3 =
        messagesResponseDto.copy(id = 4, timestamp = "2019-02-03T14:23:45.000Z")

    @Before
    fun setUp() {
        msgApi = mockk()
        messagesDao = mockk()
        appDispatchers = TestUtiDispatchers()

        chatRepository = ChatRepositoryImpl(msgApi, messagesDao, appDispatchers)
    }

    @Test
    fun `should return a success given request to get chst is a success and stored locally`() =
        runTest {
            // given
            val messagesResponseDto = MessagesResponseDto(
                agentId = "1",
                body = "message",
                id = 1,
                threadId = 9,
                timestamp = "today",
                userId = "3"
            )
            // when
            coEvery { msgApi.fetchMessages() } returns listOf(messagesResponseDto)
            coEvery { messagesDao.deleteAllMessages() } returns Unit
            coEvery { messagesDao.insertAll(listOf(messagesResponseDto).map { it.toMessageEntity() }) } returns Unit
            // act
            val response = chatRepository.getChats()

            Truth.assertThat(response).isInstanceOf(ResponseState.Success::class.java)
            Truth.assertThat((response as ResponseState.Success).data).isEqualTo("Messages fetched")
        }


    @Test
    fun `should return list of ordered chats by date given a list of messages stored locally`() =
        runTest {
            // when
            coEvery { messagesDao.fetchAllMessages() } returns flowOf(listOf(
                messagesResponseDto, messagesResponseDto2, messagesResponseDto3
            ).map { it.toMessageEntity() })

            // act
            val response = chatRepository.listenForChatsUpdates().first()

            Truth.assertThat(response.size == 2).isTrue()
            Truth.assertThat(response[0].oTimeStamp).isEqualTo("2019-02-03T14:23:45.000Z")
        }

    @Test
    fun `should return a list of messages sorted by date`() = runTest {
        // when
        coEvery { messagesDao.getMessagesByThread("9") } returns flowOf(listOf(
            messagesResponseDto, messagesResponseDto3
        ).map { it.toMessageEntity() })

        // act
        val response = chatRepository.getMessagesByThreadId("9").first()
        Truth.assertThat(response[0]).isEqualTo(messagesResponseDto.toMessageEntity().toMessage())
    }
}