package com.branch.feature_chats.presentation

import app.cash.turbine.test
import com.branch.core_network.data.ResponseState
import com.branch.feature_chats.CustomCoroutineRule
import com.branch.feature_chats.domain.models.Message
import com.branch.feature_chats.domain.repositories.ChatRepository
import com.branch.feature_chats.presentation.chat_messages.ChatMessageVm
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
class ChatMessageVmTest {

    private lateinit var repository: ChatRepository
    private lateinit var chatMessageVm: ChatMessageVm

    @get:Rule
    val customCoroutineRule = CustomCoroutineRule(StandardTestDispatcher())

    @Before
    fun setUp() {
        repository = mockk()
        chatMessageVm = ChatMessageVm(repository)
    }

    @Test
    fun `should update ui with list of messages given  a threadId`() = runTest {
        // given
        // when
        coEvery { repository.getMessagesByThreadId("9") } returns flowOf(
            listOf(
                Message(
                    id = 1,
                    userId = "1",
                    message = "test message",
                    agentId = "1",
                    timeStamp = "today"
                )
            )
        )

        // act
        chatMessageVm.getMessages("9")
        chatMessageVm.chatMessageUiState.test {
            awaitItem()
            val res = awaitItem()
            Truth.assertThat(res.messages).isNotEmpty()
            Truth.assertThat(res.messages.size == 1).isTrue()
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should return an error when message is empty`() = runTest {
        chatMessageVm.sendMessage("", "", "")
        chatMessageVm.chatMessageUiState.test {
            Truth.assertThat(awaitItem().error).isEqualTo("Message is required to reply")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should display loading when starting to make a request`() = runTest {

        coEvery {
            repository.createMessage(
                "",
                "test message",
                ""
            )
        } returns ResponseState.Success("Created")

        chatMessageVm.sendMessage("", "test message", "")
        chatMessageVm.chatMessageUiState.test {
            Truth.assertThat(awaitItem().isLoading).isTrue()
            cancelAndConsumeRemainingEvents()
        }
    }


    @Test
    fun `should return success when create message request is a success`() = runTest {
        coEvery {
            repository.createMessage(
                "",
                "test message",
                ""
            )
        } returns ResponseState.Success("Created")
        chatMessageVm.sendMessage("", "test message", "")
        chatMessageVm.chatMessageUiState.test {
            awaitItem()
            val result = awaitItem()

            Truth.assertThat(result.isLoading).isFalse()
            Truth.assertThat(result.success).isEqualTo("Created")

            cancelAndConsumeRemainingEvents()
        }
    }
}