package com.branch.customerservice.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.branch.core_utils.designs.BranchCustomerAppTheme
import com.branch.core_utils.designs.DarkPrimary
import com.branch.core_utils.navigation.Routes
import com.branch.core_utils.navigation.UiEvent
import com.branch.feature_chats.presentation.chat_messages.ChatMessagePage
import com.branch.feature_chats.presentation.chats.ChatsPage
import com.branch.feature_splash.presentation.SplashPage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BranchCustomerAppTheme {
                val systemUiController = rememberSystemUiController()
                val userDarkIcons = MaterialTheme.colors.isLight
                val context = LocalContext.current

                SideEffect {
                    systemUiController.setSystemBarsColor(
                        color = Color.Transparent, darkIcons = userDarkIcons
                    )
                    systemUiController.setStatusBarColor(if (userDarkIcons) Color.White else Color.Black)
                    systemUiController.setNavigationBarColor(if (userDarkIcons) Color.White else Color.Black)
                }

                val navController = rememberNavController()

                Surface(color = MaterialTheme.colors.background) {
                    NavHost(navController = navController, startDestination = Routes.splashPage) {
                        composable(Routes.splashPage) {
                            SplashPage { event ->
                                if (event is UiEvent.OnNavigate) {
                                    navController.navigate(event.route) {
                                        popUpTo(0)
                                    }
                                }
                            }
                        }

                        composable(Routes.authPage) {
                            LoginPage { event ->
                                if (event is UiEvent.OnNavigate) {
                                    navController.navigate(event.route) {
                                        popUpTo(0)
                                    }
                                }
                            }
                        }

                        composable(Routes.chatsPage) {
                            ChatsPage { event ->
                                navController.navigate(event.route)
                            }
                        }

                        composable(
                            Routes.chatMessagesPage, arguments = listOf(navArgument("threadId") {
                                type = NavType.StringType
                            }, navArgument("user") {}, navArgument("agent") {})
                        ) { backSentry ->
                            ChatMessagePage(
                                navController = navController,
                                backSentry.arguments?.getString("threadId")!!,
                                backSentry.arguments?.getString("user")!!,
                                backSentry.arguments?.getString("agent")!!
                            )
                        }
                    }
                }
            }
        }
    }
}