package com.github.dannful.uopoomsae.presentation.login_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.github.dannful.uopoomsae.core.Route
import com.github.dannful.uopoomsae.ui.theme.LocalSpacing

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel = hiltViewModel(),
    onLogin: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(LocalSpacing.current.small)
        ) {
            val focusRequester = remember {
                FocusRequester()
            }
            val username by loginViewModel.username.collectAsState()
            val password by loginViewModel.password.collectAsState()
            OutlinedTextField(
                maxLines = 1,
                value = username,
                onValueChange = loginViewModel::onUsernameChanged,
                label = {
                    Text(text = "Usuário")
                },
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    focusRequester.requestFocus()
                })
            )
            OutlinedTextField(
                maxLines = 1,
                value = password,
                onValueChange = loginViewModel::onPasswordChanged,
                label = {
                    Text(text = "Senha")
                }, modifier = Modifier
                    .focusRequester(focusRequester),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    loginViewModel.submit()
                    onLogin()
                })
            )
            Button(onClick = {
                loginViewModel.submit()
                onLogin()
            }) {
                Text(text = "ENTRAR")
            }
        }
    }
}

fun NavGraphBuilder.loginRoute(
    controller: NavController
) {
    composable(Route.Login.toString()) {
        LoginScreen {
            controller.navigate(Route.ModeSelect.toString())
        }
    }
}