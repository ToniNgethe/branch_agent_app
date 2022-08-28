package com.branch.customerservice.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.branch.core_resources.R
import com.branch.core_utils.designs.Ascent
import com.branch.core_utils.designs.BranchCustomerAppTheme
import com.branch.core_utils.designs.GrayA
import com.branch.core_utils.navigation.Routes
import com.branch.core_utils.navigation.UiEvent
import com.branch.feature_auth.presentation.LoginVm

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun LoginPage(
    modifier: Modifier = Modifier,
    viewModel: LoginVm = hiltViewModel(),
    onNavigate: (UiEvent) -> Unit
) {
    BranchCustomerAppTheme {
        val uiState = viewModel.loginUiState.collectAsState()
        val scaffoldState = rememberScaffoldState()
        val focusManager = LocalFocusManager.current

        if (uiState.value.onError != null) {
            focusManager.clearFocus()
            LaunchedEffect(uiState.value.onError) {
                when (scaffoldState.snackbarHostState.showSnackbar(
                    uiState.value.onError!!,
                )) {
                    SnackbarResult.Dismissed -> {
                    }
                    else -> {}
                }
            }
        }

        if (uiState.value.onSuccess != null) {
            onNavigate.invoke(UiEvent.OnNavigate(Routes.chatsPage))
        }

        Scaffold(scaffoldState = scaffoldState, content = {
            Box(
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = modifier.fillMaxWidth()

                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Image(
                        painter = painterResource(id = R.drawable.login_illus),
                        modifier = Modifier
                            .height(150.dp)
                            .width(140.dp),
                        contentDescription = "login_illus"
                    )
                    Text(
                        text = stringResource(id = R.string.login_helper_text),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.h3.copy(
                            fontSize = 14.sp, color = GrayA
                        )
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    var userName by remember { mutableStateOf("") }

                    Box(modifier = Modifier.padding(start = 30.dp, end = 30.dp)) {
                        OutlinedTextField(modifier = Modifier.fillMaxWidth(), trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = null
                            )
                        },
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Ascent, focusedLabelColor = Ascent
                            ), value = userName, singleLine = true, label = {
                                Text(
                                    text = stringResource(id = R.string.user_name),
                                    style = MaterialTheme.typography.h3.copy(fontSize = 14.sp)
                                )
                            }, textStyle = TextStyle(color = Ascent), onValueChange = {
                                userName = it
                            })
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    var password by rememberSaveable { mutableStateOf("moc.liamg@ehtegnynotna") }
                    var passwordVisible by rememberSaveable { mutableStateOf(false) }

                    Box(modifier = Modifier.padding(start = 30.dp, end = 30.dp)) {
                        OutlinedTextField(modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Ascent, focusedLabelColor = Ascent
                            ),
                            value = password,
                            singleLine = true,
                            label = {
                                Text(
                                    text = stringResource(id = R.string.password),
                                    style = MaterialTheme.typography.h3.copy(fontSize = 14.sp)
                                )
                            },
                            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            textStyle = TextStyle(color = Ascent),
                            onValueChange = {
                                password = it
                            },
                            trailingIcon = {
                                val image = if (passwordVisible) R.drawable.visibility
                                else R.drawable.visibility_off

                                // Please provide localized description for accessibility services
                                val description =
                                    if (passwordVisible) "Hide password" else "Show password"

                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(painterResource(id = image), description)
                                }
                            })
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(modifier = Modifier.padding(start = 30.dp, end = 30.dp)) {
                        Button(
                            modifier = Modifier.fillMaxWidth(), onClick = {
                                focusManager.clearFocus()
                                viewModel.loginUser(userName, password)
                            }, colors = ButtonDefaults.buttonColors(backgroundColor = Ascent)
                        ) {
                            if (uiState.value.isLoading) CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier
                                    .height(20.dp)
                                    .width(20.dp)
                            )
                            else Text(
                                text = stringResource(id = R.string.submit),
                                style = MaterialTheme.typography.h3.copy(
                                    fontSize = 14.sp, color = Color.White
                                )
                            )
                        }
                    }
                }
            }
        })

    }
}

@Preview
@Composable
fun PreviewLoginPage() {
    LoginPage() {

    }
}

