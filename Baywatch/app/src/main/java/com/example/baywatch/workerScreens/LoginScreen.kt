package com.example.baywatch.workerScreens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.baywatch.data.Beach
import com.example.baywatch.ui.theme.BaywatchTheme

@Composable
fun LoginScreen(
    modifier : Modifier = Modifier,
    login: (String,String)->Unit
){
    var email : String by remember{ mutableStateOf("") }
    var password : String by remember{ mutableStateOf("") }
    var emailError: Boolean by remember{ mutableStateOf(false) }
    var passwordError: Boolean by remember{ mutableStateOf(false) }

    Column(modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text("INICIO DE SESIÓN", fontSize = 30.sp, fontWeight = FontWeight.ExtraBold)
        Spacer(modifier  = Modifier.padding(20.dp))
        OutlinedTextField(
            value = email,
            onValueChange ={
               email = it
            } ,
            label={
                Text("Email")
            },
            isError = emailError,
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email
            )
        )
        Spacer(modifier  = Modifier.padding(20.dp))
        OutlinedTextField(

            value = password,
            onValueChange ={
                password  =it
            },
            label={
                Text("Contraseña")
            },
            visualTransformation = PasswordVisualTransformation(),
            isError= passwordError,
            singleLine = true,


        )

        Spacer(modifier  = Modifier.padding(20.dp))

        Button(onClick = {
            passwordError = password.isEmpty()
            emailError = email.isEmpty()

            if(!emailError && !passwordError)
                login(email,password)
        }) {
            Text("ENTRAR")
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    BaywatchTheme {
        LoginScreen(modifier = Modifier.fillMaxSize(),
            login={a, b->

            })
    }
}