package net.ezra.ui.products



import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import net.ezra.R
import net.ezra.navigation.ROUTE_DASHBOARD
import net.ezra.ui.about.AboutFont
import net.ezra.ui.dashboard.BottomBar

class EventViewModel : ViewModel() {
    private val firestore = FirebaseFirestore.getInstance()

    val eventState = MutableLiveData<Event>()

    fun getEvent(eventId: String) {
        viewModelScope.launch {
            val document = firestore.collection("events").document(eventId).get().await()
            val event = document.toObject(Event::class.java) ?: return@launch
            eventState.value = event
        }
    }

    fun updateEvent(event: Event) {
        firestore.collection("events").document(event.id).set(event)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UpdateEventScreen(navController: NavController, eventId: String, eventViewModel: EventViewModel) {

    var updatedEventName by remember { mutableStateOf("") }
    var updatedEventDescription by remember { mutableStateOf("") }
    var updatedEventImageUri by remember { mutableStateOf<Uri?>(null) }

    val EventState by eventViewModel.eventState.observeAsState()

    val event = EventState ?: Event()

    updatedEventName = event.name
    updatedEventDescription = event.description
    updatedEventImageUri = Uri.parse(event.imageUrl)

    val storage = FirebaseStorage.getInstance()
    fun uploadImageToStorage(eventId: String, imageUri: Uri?, onSuccess: (String) -> Unit) {
        if (imageUri != null) {
            val storageRef = storage.reference.child("event_images").child("$eventId.jpg")
            val uploadTask = storageRef.putFile(imageUri)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                storageRef.downloadUrl.addOnSuccessListener { uri ->
                    onSuccess(uri.toString())
                }
            }
        }
    }

    val context = LocalContext.current
    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            updatedEventImageUri = uri
        }

    Scaffold(

        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Edit Event",
                        style = TextStyle(
                            fontFamily = AboutFont,
                            fontSize = 20.sp
                        )
                    )

                },
                navigationIcon = {
                    IconButton(onClick = {


                        navController.navigate(ROUTE_DASHBOARD)


                    }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack, "backIcon",
                            tint = Color.White
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xff3a7ca5),
                    titleContentColor = Color.White,

                    )
            )
        }, content = {


            Column(modifier = Modifier.padding(16.dp)) {
//                Text(text = "Edit Event", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(16.dp))
                updatedEventImageUri?.let { uri ->
                    Image(
                        painter = painterResource(id = R.drawable.small), // Placeholder image
                        contentDescription = null,
                        modifier = Modifier.size(120.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = updatedEventName,
                    onValueChange = { updatedEventName = it },
                    label = { Text(updatedEventName.takeUnless { it.isBlank() } ?: "Event Name") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = updatedEventDescription,
                    onValueChange = { updatedEventDescription = it },
                    label = {
                        Text(updatedEventDescription.takeUnless { it.isBlank() }
                            ?: "Event Description")
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        getContent.launch("image/*")
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xff3a7ca5)
                    )
                ) {
                    Text(
                        "Select Image",
                        style = TextStyle(
                            fontFamily = AboutFont
                        )
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val updatedEvent = Event(
                            id = eventId,
                            name = updatedEventName,
                            description = updatedEventDescription,
                            imageUrl = ""
                        )

                        if (updatedEventImageUri != null) {
                            uploadImageToStorage(eventId, updatedEventImageUri) { imageUrl ->
                                updatedEvent.imageUrl = imageUrl
                                eventViewModel.updateEvent(updatedEvent)
                                navController.popBackStack()
                            }
                        } else {
                            eventViewModel.updateEvent(updatedEvent)
                            navController.popBackStack()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xff3a7ca5)
                    ),
                    modifier = Modifier.align(Alignment.End)
                )

                {
                    Text(
                        "Save",
                        style = TextStyle(
                            fontFamily = AboutFont
                        )
                    )
                }
            }

        },


        bottomBar = { BottomBar(navController = navController) }
    )
}
