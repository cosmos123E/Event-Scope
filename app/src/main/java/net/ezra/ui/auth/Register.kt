package net.ezra.ui.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import net.ezra.R
import net.ezra.navigation.ROUTE_DASHBOARD
import net.ezra.navigation.ROUTE_LOGIN
import net.ezra.navigation.ROUTE_REGISTER
import net.ezra.ui.about.AboutFont
import net.ezra.ui.dashboard.myCustomFontFamily




@Composable
fun SignUpScreen(navController: NavController, onSignUpSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff87ceeb))
    ){


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color(0xff87ceeb)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            AuthHeader()

            Text(
                "Sign Up",
                style = MaterialTheme.typography.h3.copy(
                    fontFamily = AboutFont,
                    fontWeight = FontWeight.Bold,

                    color = Color.Black),

            )

            Spacer(modifier = Modifier.height(16.dp))


            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(
                        "Email",
                        style = TextStyle(
                            fontFamily = AboutFont,
                            fontWeight = FontWeight.Bold,
                            color = Color.White),

                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
//                    .border(1.dp, Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        "Password",
                        style = TextStyle(
                            fontFamily = AboutFont,
                            fontWeight = FontWeight.Bold,
                            color = Color.White),
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
//                    .border(1.dp, Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = {
                    Text(
                        "Confirm Password",
                        style = TextStyle(
                            fontFamily = AboutFont,
                            fontWeight = FontWeight.Bold,
                            color = Color.White),
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
//                    .border(1.dp, Color.White)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(
                    color = Color(0xff26619c),
                    modifier = Modifier.size(48.dp)
                )
            } else {
                Button(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        backgroundColor = Color(0xff26619c)
                    ),
                    onClick = {
                        if
                                (email.isBlank()) {
                            error = "Email is required"
                        } else if (password.isBlank()) {
                            error = "Password is required"
                        } else if (confirmPassword.isBlank()) {
                            error = "Password Confirmation required"
                        } else if (password != confirmPassword) {
                            error = "Passwords do not match"
                        } else {
                            isLoading = true
                            signUp(email, password, {
                                isLoading = false
                                Toast.makeText(
                                    context,
                                    "Sign-up successful!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                onSignUpSuccess()
                                navController.navigate(ROUTE_DASHBOARD)
                            }) { errorMessage ->
                                isLoading = false
                                error = errorMessage
                            }
                        }

                    },

                    ) {
                    Text("Sign Up",
                        style = TextStyle(
                            fontFamily = AboutFont,
                            fontWeight = FontWeight.Normal,
                            color = Color.White
                        )
                    )
                }


                androidx.compose.material3.Text(
                    modifier = Modifier

                        .clickable {
                            navController.navigate(ROUTE_LOGIN) {
                                popUpTo(ROUTE_REGISTER) { inclusive = true }
                            }
                        },
                    text = "go to login",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontFamily = AboutFont,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                )

            }

            error?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}
private fun signUp(email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
    FirebaseAuth.getInstance().fetchSignInMethodsForEmail(email)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val signInMethods = task.result?.signInMethods ?: emptyList()
                if (signInMethods.isNotEmpty()) {
                    onError("Email is already registered")
                } else {
                    // Email is not registered, proceed with sign-up
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { signUpTask ->
                            if (signUpTask.isSuccessful) {
                                onSuccess()
                            } else {
                                onError(signUpTask.exception?.message ?: "Sign-up failed")
                            }
                        }
                }
            } else {
                onError(task.exception?.message ?: "Failed to check email availability")
            }
        }
}


