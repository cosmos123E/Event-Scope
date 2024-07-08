package net.ezra.ui.auth



import androidx.activity.compose.BackHandler
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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
fun LoginScreen(navController: NavController, onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    BackHandler {
        navController.popBackStack()

    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff87ceeb))
    ) {


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color(0xff87ceeb)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            AuthHeader()
            Text("Login",
                style = MaterialTheme.typography.h3.copy(
                    fontFamily = AboutFont,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(
                        "Email",
                        style = TextStyle(
                            fontFamily = AboutFont,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.White)
            )
            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        "Password",
                        style = TextStyle(
                            fontFamily = AboutFont,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.White)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (isLoading) {
                CircularProgressIndicator(
                    color = Color(0xff26619c),
                    modifier = Modifier.size(48.dp)
                )
            } else {


                OutlinedButton(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        backgroundColor = Color(0xff26619c)
                    ),
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            error = "Please fill in all fields"
                        } else {
                            isLoading = true
                            FirebaseAuth.getInstance()
                                .signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener { task ->
                                    isLoading = false
                                    if (task.isSuccessful) {
                                        navController.navigate(ROUTE_DASHBOARD)
                                    } else {
                                        error =
                                            task.exception?.message ?: "Login failed"
                                    }
                                }
                        }
                    },

                    ) {
                    Text("Login",
                        style = TextStyle(
                            fontFamily = AboutFont,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    )
                }

                androidx.compose.material3.Text(
                    modifier = Modifier

                        .clickable {
                            navController.navigate(ROUTE_REGISTER) {
                                popUpTo(ROUTE_LOGIN) { inclusive = true }
                            }
                        },
                    text = "go to register",
                    style = TextStyle(
                        fontFamily = AboutFont,
                        fontWeight = FontWeight.Normal,
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

