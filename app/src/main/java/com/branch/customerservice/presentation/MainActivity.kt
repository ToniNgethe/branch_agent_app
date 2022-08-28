package com.branch.customerservice.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.branch.core_utils.designs.BranchCustomerAppTheme
import com.branch.core_utils.designs.DarkPrimary
import com.branch.core_utils.navigation.Routes
import com.branch.core_utils.navigation.UiEvent
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
                    systemUiController.setStatusBarColor(if (userDarkIcons) Color.White else DarkPrimary)
                    systemUiController.setNavigationBarColor(if (userDarkIcons) Color.White else DarkPrimary)
                }

                val navController = rememberNavController()

                Surface(color = MaterialTheme.colors.background) {
                    NavHost(navController = navController, startDestination = Routes.splashPage) {
                        composable(Routes.splashPage) {
                            SplashPage { event ->
                                if (event is UiEvent.OnNavigate) {
                                    navController.navigate(event.route)
                                }
                            }
                        }

                        composable(Routes.authPage) {
                            LoginPage()
                        }

                        composable(Routes.chatsPage) {

                        }

                        composable(Routes.chatMessagesPage) {
                            ChatMessagePage()
                        }
                    }
                }
            }
        }
    }
}