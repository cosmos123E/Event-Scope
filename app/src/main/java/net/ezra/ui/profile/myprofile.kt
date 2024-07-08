package net.ezra.ui.profile

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import net.ezra.R
import net.ezra.navigation.ROUTE_REGISTER
import net.ezra.ui.about.AboutFont
import net.ezra.ui.dashboard.myCustomFontFamily

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(navController: NavHostController) {
    val currentUser = Firebase.auth.currentUser

    // State to hold user profile
    var userProfile by remember { mutableStateOf(UserProfile("", "")) }
    var profilePictureUrl by remember { mutableStateOf<String?>(null) }

    // Effect to fetch user profile once
    LaunchedEffect(key1 = currentUser?.uid) {
        currentUser?.let { user ->
            // Fetch user profile from Firebase Authentication
            userProfile = UserProfile(user.displayName ?: "", user.email ?: "")
            profilePictureUrl = user.photoUrl?.toString()
        }
    }

    val context = LocalContext.current as Activity
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference
            val profilePicRef = storageRef.child("profile_pictures/${it.lastPathSegment}")

            profilePicRef.putFile(it)
                .addOnSuccessListener {
                    profilePicRef.downloadUrl.addOnSuccessListener { url ->
                        profilePictureUrl = url.toString()
                        // Update user's profile picture URL in Firebase Authentication
                        currentUser?.updateProfile(userProfileChangeRequest {
                            photoUri = url
                        })?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Profile picture updated successfully
                            } else {
                                // Handle the error
                            }
                        }
                    }
                }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xffffffff))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Profile Picture
        Text(text = "My Profile",
            style = MaterialTheme.typography.h3.copy(
                fontFamily = myCustomFontFamily,
                textAlign = TextAlign.Start
            ),
            modifier = Modifier
                .padding(bottom = 55.dp)
        )

        Box(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape)
                .background(Color.Gray)
                .clickable {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            context,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            1
                        )
                    } else {
                        launcher.launch("image/*")
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            if (profilePictureUrl != null) {
                Image(
                    painter = rememberImagePainter(profilePictureUrl),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.ic_person),
                    contentDescription = "Default Profile Picture",
                    modifier = Modifier
                        .size(128.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    colorFilter = ColorFilter.tint(Color(0xffffffff), BlendMode.Difference)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))



        // Email
        Text(
            text = userProfile.email,
            style = TextStyle(
                fontFamily = AboutFont,
                fontSize = 20.sp,
                color = Color.Black),

        )

        Spacer(modifier = Modifier.height(32.dp))

        // Placeholder events grid
        EventsGrid(events = sampleEvents)

        Spacer(modifier = Modifier.height(32.dp))

        // Logout Button
        Button(
            onClick = {
                Firebase.auth.signOut()
                navController.navigate(ROUTE_REGISTER)
            },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff3A7CA5))
        ) {
            Text(
                text = "Logout",
                style = TextStyle(
                    fontFamily = AboutFont,
                    color = Color.White,
                    fontSize = 16.sp
                )
            )
        }
    }
}

@Composable
fun EventsGrid(events: List<Event>) {
    // Your implementation for displaying events grid
}

// Placeholder user profile data class
data class UserProfile(
    val displayName: String,
    val email: String
)

// Placeholder events data
data class Event(val name: String, val date: String)
val sampleEvents = listOf(
    Event("Event 1", "Date 1"),
    Event("Event 2", "Date 2")
    // Add more events as needed
)






//
//
//
//
//
//
//
//
//
//
//
//
//
//
//package net.ezra.ui.profile
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.app.Activity
//import android.content.pm.PackageManager
//import android.net.Uri
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.core.app.ActivityCompat
//import androidx.navigation.NavHostController
//import coil.compose.rememberImagePainter
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
//import com.google.firebase.storage.FirebaseStorage
//import kotlinx.coroutines.launch
//import net.ezra.R
//import net.ezra.navigation.ROUTE_REGISTER
//
//@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
//@Composable
//fun ProfileScreen(navController: NavHostController) {
//    val currentUser = Firebase.auth.currentUser
//
//    // State to hold user profile
//    var userProfile by remember { mutableStateOf(UserProfile("", "")) }
//    var profilePictureUrl by remember { mutableStateOf<String?>(null) }
//
//    // Effect to fetch user profile once
//    LaunchedEffect(key1 = currentUser?.uid) {
//        currentUser?.let { user ->
//            // Fetch user profile from Firebase Authentication
//            userProfile = UserProfile(user.displayName ?: "", user.email ?: "")
//            profilePictureUrl = user.photoUrl?.toString()
//        }
//    }
//
//    val context = LocalContext.current as Activity
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let {
//            val storage = FirebaseStorage.getInstance()
//            val storageRef = storage.reference
//            val profilePicRef = storageRef.child("profile_pictures/${it.lastPathSegment}")
//
//            profilePicRef.putFile(it)
//                .addOnSuccessListener {
//                    profilePicRef.downloadUrl.addOnSuccessListener { url ->
//                        profilePictureUrl = url.toString()
//                        // Update user's profile picture URL in Firebase Authentication
//                        currentUser?.updateProfile {
//                            photoUri = url
//                        }
//                    }
//                }
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .background(Color(0xFF89cff0))
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Spacer(modifier = Modifier.height(32.dp))
//
//        // Profile Picture
//        Box(
//            modifier = Modifier
//                .size(128.dp)
//                .clip(CircleShape)
//                .background(Color.Gray)
//                .clickable {
//                    if (ActivityCompat.checkSelfPermission(
//                            context,
//                            Manifest.permission.READ_EXTERNAL_STORAGE
//                        ) != PackageManager.PERMISSION_GRANTED
//                    ) {
//                        ActivityCompat.requestPermissions(
//                            context,
//                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
//                            1
//                        )
//                    } else {
//                        launcher.launch("image/*")
//                    }
//                },
//            contentAlignment = Alignment.Center
//        ) {
//            if (profilePictureUrl != null) {
//                Image(
//                    painter = rememberImagePainter(profilePictureUrl),
//                    contentDescription = "Profile Picture",
//                    modifier = Modifier
//                        .size(128.dp)
//                        .clip(CircleShape),
//                    contentScale = ContentScale.Crop
//                )
//            } else {
//                Image(
//                    painter = painterResource(id = R.drawable.ic_person),
//                    contentDescription = "Default Profile Picture",
//                    modifier = Modifier
//                        .size(128.dp)
//                        .clip(CircleShape),
//                    contentScale = ContentScale.Crop
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Display Name
//        Text(
//            text = userProfile.displayName,
//            fontSize = 20.sp,
//            fontWeight = FontWeight.Bold,
//            color = Color.White
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        // Email
//        Text(
//            text = userProfile.email,
//            fontSize = 16.sp,
//            color = Color.White
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        // Placeholder events grid
//        EventsGrid(events = sampleEvents)
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        // Logout Button
//        Button(
//            onClick = {
//                Firebase.auth.signOut()
//                navController.navigate(ROUTE_REGISTER)
//            },
//            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff3A7CA5))
//        ) {
//            Text(
//                text = "Logout",
//                color = Color.White,
//                fontSize = 16.sp
//            )
//        }
//    }
//}
//
//@Composable
//fun EventsGrid(events: List<Event>) {
//    // Your implementation for displaying events grid
//}
//
//// Placeholder user profile data class
//data class UserProfile(
//    val displayName: String,
//    val email: String
//)
//
//// Placeholder events data
//data class Event(val name: String, val date: String)
//val sampleEvents = listOf(
//    Event("Event 1", "Date 1"),
//    Event("Event 2", "Date 2")
//    // Add more events as needed
//)
