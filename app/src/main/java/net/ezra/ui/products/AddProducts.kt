package net.ezra.ui.products

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import net.ezra.R
import net.ezra.navigation.ROUTE_DASHBOARD
import net.ezra.navigation.ROUTE_HOME
import net.ezra.navigation.ROUTE_SEARCH
import net.ezra.navigation.ROUTE_VIEW_PROD
import net.ezra.ui.about.AboutFont
import net.ezra.ui.dashboard.BottomBar
import java.util.*

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEventScreen(navController: NavController, onEventAdded: () -> Unit) {
    var EventName by remember { mutableStateOf("") }
    var EventDescription by remember { mutableStateOf("") }
    var Eventlocation by remember { mutableStateOf("") }
    var EventDuration by remember { mutableStateOf("") }
    var EventTime by remember { mutableStateOf("") }
    var EventImageUri by remember { mutableStateOf<Uri?>(null) }


    // Track if fields are empty
    var EventNameError by remember { mutableStateOf(false) }
    var EventDescriptionError by remember { mutableStateOf(false) }
    var EventlocationError by remember { mutableStateOf(false) }
    var EventDurationError by remember { mutableStateOf(false) }
    var EventTimeError by remember { mutableStateOf(false) }
    var EventImageError by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            EventImageUri = it
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Add Events",
                        style = TextStyle(
                            fontFamily = AboutFont,
                            fontSize = 30.sp,
                            color = Color.White
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(ROUTE_VIEW_PROD)
                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            "backIcon",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xff3A7CA5),
                    titleContentColor = Color.White,
                )
            )
        },
        content = {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xff87ceeb))
            ) {
                item {
                    if (EventImageUri != null) {
                        // Display selected image
                        Image(
                            painter = rememberAsyncImagePainter(EventImageUri), // Using rememberImagePainter with Uri
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                        )
                    } else {
                        // Display placeholder if no image selected
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No Image Selected",
                                style = TextStyle(
                                    fontFamily = AboutFont)
                                ,modifier = Modifier.padding(8.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { launcher.launch("image/*") },
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = Color(0xff3A7CA5)
                        ),
                    ) {
                        Text("Select Image",
                            style = TextStyle(
                                fontFamily = AboutFont,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = EventName,
                        onValueChange = { EventName = it },
                        label = {
                            Text(
                                "Event Name",
                                style = TextStyle(
                                    fontFamily = AboutFont,
                                fontWeight = FontWeight.Bold,
                                color = Color.White)
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = EventDescription,
                        onValueChange = { EventDescription = it },
                        label = {
                            Text(
                                "Event Description",
                                style = TextStyle(
                                    fontFamily = AboutFont,
                                fontWeight = FontWeight.Bold,
                                color = Color.White)
                            )
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = Eventlocation,
                        onValueChange = { Eventlocation = it },
                        label = {
                            Text(
                                "Location of the Event",
                                style = TextStyle(
                                    fontFamily = AboutFont,
                                fontWeight = FontWeight.Bold,
                                color = Color.White),
                                modifier = Modifier



                            )
                        },
                       modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    if (EventNameError) {
                        Text("Event Name is required",
                            style = TextStyle(
                                fontFamily = AboutFont,
                                color = Color.Red)
                        )
                    }
                    if (EventDescriptionError) {
                        Text("Event Description is required",
                            style = TextStyle(
                                fontFamily = AboutFont,
                                color = Color.Red)
                        )
                    }
                    if (EventlocationError) {
                        Text("Event location is required",
                            style = TextStyle(
                                fontFamily = AboutFont,color = Color.Red)
                        )
                    }
                    if (EventImageError) {
                        Text("Event Image is required",
                            style = TextStyle(
                                fontFamily = AboutFont,
                                color = Color.Red)
                        )
                    }

                    // Button to add product
                    OutlinedButton(
                        onClick = {
                            // Reset error flags
                            EventNameError = EventName.isBlank()
                            EventDescriptionError = EventDescription.isBlank()
                            EventlocationError = Eventlocation.isBlank()
                            EventImageError = EventImageUri == null

                            // Add product if all fields are filled
                            if (!EventNameError && !EventDescriptionError && !EventlocationError && !EventImageError) {
                                addEventToFirestore(
                                    navController,
                                    onEventAdded,
                                    EventName,
                                    EventDescription,
                                    Eventlocation,
                                    EventImageUri
                                )


                            }

                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            backgroundColor = Color(0xff3A7CA5)
                        )

                    ) {
                        Text("Add Event",
                            style = TextStyle(
                                fontFamily = AboutFont,
                                fontWeight = FontWeight.Bold,
                                color = Color.White)

                        )
                    }
                }
            }
        },
        bottomBar ={BottomBar(navController = navController)}
    )
}

private fun addEventToFirestore(navController: NavController, onEventAdded: () -> Unit, EventName: String, EventDescription: String, Eventlocation: String, EventImageUri: Uri?) {
    if (EventName.isEmpty() || EventDescription.isEmpty() || Eventlocation.isEmpty() || EventImageUri == null) {
        // Validate input fields
        return
    }
    val eventId = UUID.randomUUID().toString()

    val firestore = Firebase.firestore
    val eventData = hashMapOf(
        "name" to EventName,
        "description" to EventDescription,
        "location" to Eventlocation,
        "imageUrl" to ""
    )

    firestore.collection("events").document(eventId)
        .set(eventData)
        .addOnSuccessListener {
            uploadImageToStorage(eventId, EventImageUri) { imageUrl ->
                firestore.collection("events").document(eventId)
                    .update("imageUrl", imageUrl)
                    .addOnSuccessListener {
                        // Display toast message
                        Toast.makeText(
                            navController.context,
                            "Event added successfully!",
                            Toast.LENGTH_SHORT
                        ).show()

                        // Navigate to another screen
                        navController.navigate(ROUTE_VIEW_PROD)

                        // Invoke the onProductAdded callback
                        onEventAdded()
                    }
                    .addOnFailureListener { e ->
                        // Handle error updating product document
                    }
            }
        }
        .addOnFailureListener { e ->
            // Handle error adding product to Firestore
        }
}

private fun uploadImageToStorage(eventId: String, imageUri: Uri?, onSuccess: (String) -> Unit) {
    if (imageUri == null) {
        onSuccess("")
        return
    }

    val storageRef = Firebase.storage.reference
    val imagesRef = storageRef.child("events/$eventId.jpg")

    imagesRef.putFile(imageUri)
        .addOnSuccessListener { taskSnapshot ->
            imagesRef.downloadUrl
                .addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }
                .addOnFailureListener {
                    // Handle failure to get download URL
                }
        }
        .addOnFailureListener {
            // Handle failure to upload image
        }
}



