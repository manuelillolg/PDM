package com.example.baywatchworkeredition

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.baywatchworkeredition.theme.BaywatchTheme
import com.example.baywatchworkeredition.workerScreens.LoginScreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class BaywatchWorkerActivity : ComponentActivity() {
    var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaywatchTheme {
                LoginScreen(
                    modifier = Modifier .fillMaxSize(),
                    login = {email,password->
                        login(email,password)
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        auth.signOut()
    }

    private fun login(email:String, password:String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) {task->
                if(task.isSuccessful){
                    updateUI(auth.currentUser)
                }else{
                    Toast.makeText(
                        baseContext,
                        "Error de inicio de sesión",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }

    private fun updateUI(user : FirebaseUser?){
        setContent{
            BaywatchTheme {
                BaywatchWorkerScreen(
                    context = LocalContext.current
                )
            }
        }
    }




}


