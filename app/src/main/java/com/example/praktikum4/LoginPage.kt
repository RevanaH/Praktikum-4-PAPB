package com.example.praktikum4

import android.content.ContentValues
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

private lateinit var auth: FirebaseAuth

@Composable
fun LoginPage(navController: NavController, modifier: Modifier = Modifier){
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val googleSignIn = remember { GoogleSignIn(context, navController) }
    val googleSignInLauncher = googleSignIn.getGoogleSignInLauncher()
    val gradient = Brush.verticalGradient(
        0.0f to Color(0xFF6AF6C3),
        1.0f to Color(0xFF3D79F9),
        startY = 0.0f,
        endY = 2500.0f
    )

    fun signIn(email: String, password: String){
        auth = Firebase.auth
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{ task ->
                if (task.isSuccessful){
                    Log.d(
                        ContentValues.TAG,
                        "Sign in with Email:success"
                    )
                    val user = auth.currentUser?.email
                    Toast.makeText(
                        context,
                        "Successfullly signed in as " + user.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                    navController.navigate("home")
                }else{
                    Log.w(
                        ContentValues.TAG,
                        "Sign in with Email:failure", task.exception
                    )
                    Toast.makeText(
                        context,
                        "Authentication failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().background(gradient)
    ) {
        Spacer(modifier = Modifier.height(30.dp))
        Image(
            painter = painterResource(id
            = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .absolutePadding(
                    0.dp,
                    20.dp,
                    0.dp,
                    20.dp
                )
        )
        Text(
            text = "Learn Graphic and UI/UX designing in Hindi for free with live projects.",
            fontSize = 16.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .absolutePadding(
                    30.dp,
                    20.dp,
                    30.dp,
                    20.dp
                )
        )
        Column {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(
                        text = "Email Address",
                        fontSize = 15.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Normal
                    )
                },
                placeholder = {
                    Text(
                        text = "Enter Email Address",
                        fontSize = 15.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Light
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .width(210.dp)
                    .height(72.dp)
                    .absolutePadding(
                        30.dp,
                        20.dp,
                        30.dp,
                        0.dp
                    )
                    .clip(RoundedCornerShape(150.dp))
                    .background(
                        color = Color(0xFF6CC5DE),
                        shape = RoundedCornerShape(150.dp)
                    )
                    .padding(start = 15.dp),
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Normal,
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent

                ),
            )

            TextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        text = "Password",
                        color = Color.White,
                        fontSize = 15.sp,
                    )
                },
                placeholder = {
                    Text(
                        text = "Enter Password",
                        fontSize = 15.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Light
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .width(210.dp)
                    .height(72.dp)
                    .absolutePadding(
                        30.dp,
                        20.dp,
                        30.dp,
                        0.dp
                    )
                    .clip(RoundedCornerShape(150.dp))
                    .background(
                        color = Color(0xFF6CC5DE),
                        shape = RoundedCornerShape(150.dp)
                    )

                    .padding(start = 15.dp),
                textStyle = TextStyle(
                    fontSize = 15.sp,
                    color = Color.White
                ),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    errorContainerColor = Color.Transparent

                )
            )
            Button(
                onClick = {
                    if (email.isNotEmpty() && password.isNotEmpty()) {
                        signIn(email, password)
                    } else {
                        Toast.makeText(
                            context,
                            "Please fill all the required information above.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                },
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .width(210.dp)
                    .height(72.dp)
                    .absolutePadding(
                        30.dp,
                        20.dp,
                        30.dp,
                        0.dp
                    )
            )
            {
                Text(
                    text = "LOGIN",
                    fontSize = 16.sp,
                    color = Color(0xFF3F82E6),
                    fontWeight = FontWeight.Bold
                )
            }
        }
        Box(modifier = Modifier
            .absolutePadding(
                225.dp,
                20.dp,
                30.dp,
                20.dp
            )){
            Text(
                text = "Forgot Password?",
                fontSize = 14.sp,
                color = Color.White,
                textAlign = TextAlign.Right
            )
        }
        Row(modifier = Modifier.absolutePadding(
            0.dp,
            7.dp,
            0.dp,
            20.dp
        )){
            Text(
                text = "Don’t have an account? ",
                modifier = Modifier
                    .absolutePadding(
                        0.dp,
                        20.dp,
                        0.dp,
                        20.dp
                    ),
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Register now",
                modifier = Modifier
                    .clickable {
                        navController.navigate("register")
                    }
                    .absolutePadding(
                        0.dp,
                        20.dp,
                        0.dp,
                        20.dp
                    ),
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline
            )
        }

        Button(
            onClick = { googleSignIn.signInWithGoogle(googleSignInLauncher) },
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .width(110.dp)
                .height(53.dp)
                .absolutePadding(
                    30.dp,
                    0.dp,
                    30.dp,
                    0.dp
                ),

            )
        {
            Spacer(modifier = Modifier.padding(horizontal = 30.dp))
            Image(
                painter = painterResource(id = R.drawable.gmail),
                contentDescription = null,
                Modifier
                    .width(35.dp)
                    .height(40.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 12.dp))
            Text(
                text = "Continue With Google",
                fontSize = 18.sp,
                color = Color(0xFF3F82E6),
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()

            )
        }
        Row(modifier = Modifier.absolutePadding(
            0.dp,
            20.dp,
            0.dp,
            0.dp
        )){
            Text(
                text = "By signing up, you are agree with our ",
                modifier = Modifier
                    .absolutePadding(
                        0.dp,
                        20.dp,
                        0.dp,
                        10.dp
                    ),
                color = Color.White,
                fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Terms & Conditions",
                modifier = Modifier
                    .absolutePadding(
                        0.dp,
                        20.dp,
                        0.dp,
                        10.dp
                    ),
                color = Color.White,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                textDecoration = TextDecoration.Underline
            )
        }
    }
}
