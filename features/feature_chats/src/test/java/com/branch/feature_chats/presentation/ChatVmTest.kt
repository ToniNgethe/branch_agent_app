package com.branch.feature_chats.presentation

import app.cash.turbine.test
import com.branch.core_network.data.ResponseState
import com.branch.feature_chats.CustomCoroutineRule
import com.branch.feature_chats.domain.models.Chat
import com.branch.feature_chats.domain.repositories.ChatRepository
import com.branch.feature_chats.presentation.chats.ChatVm
import com.google.common.truth.Truth
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatVmTest {
    private lateinit var chatRepository: ChatRepository
    private lateinit var chatVm: ChatVm

    @get:Rule
    val customCoroutineRule = CustomCoroutineRule(StandardTestDispatcher())

    @Before
    fun setUp() {
        chatRepository = mockk()
        chatVm = ChatVm(chatRepository)
    }

    @Test
    fun `should update ui with list of chats`() = runTest {
        coEvery { chatRepository.listenForChatsUpdates() } returns flowOf(
            listOf(
                Chat(
                    id = 1,
                    userId = "1",
                    latestMessage = "test message",
                    agentId = "12",
                    timeStamp = "today",
                    oTimeStamp = "today",
                    threadId = null
                )
            )
        )
        coEvery { chatRepository.getChats() } returns ResponseState.Success("Chats found")
        chatVm.chatUiState.test {
            awaitItem()
            awaitItem()
            val messages = awaitItem()
            Truth.assertThat(messages.chats).isNotEmpty()
            Truth.assertThat(messages.chats.size == 1).isTrue()

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `should return success when fetching chats`() = runTest {
        coEvery { chatRepository.listenForChatsUpdates() } returns flowOf(
            listOf(
                Chat(
                    id = 1,
                    userId = "1",
                    latestMessage = "test message",
                    agentId = "12",
                    timeStamp = "today",
                    oTimeStamp = "today",
                    threadId = null
                )
            )
        )
        coEvery { chatRepository.getChats() } returns ResponseState.Success("Chats found")
        chatVm.chatUiState.test {
            cancelAndConsumeRemainingEvents()
        }
    }
}