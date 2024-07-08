package net.ezra.ui.products

import android.annotation.SuppressLint
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import net.ezra.navigation.ROUTE_HOME
import net.ezra.navigation.ROUTE_VIEW_PROD
import net.ezra.navigation.ROUTE_VIEW_STUDENTS
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import coil.compose.rememberAsyncImagePainter
import net.ezra.navigation.ROUTE_DASHBOARD
import net.ezra.ui.about.AboutFont
import net.ezra.ui.dashboard.BottomBar
import net.ezra.ui.dashboard.myCustomFontFamily

data class Event(
    var id: String = "",
    val name: String = "",
    val description: String ="",
    var imageUrl: String = "",
    val location : String = ""
)



@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventListScreen(navController: NavController, events: List<Event>) {
    var isLoading by remember { mutableStateOf(true) }
    var eventList by remember { mutableStateOf(emptyList<Event>()) }
    var displayedEventCount by remember { mutableStateOf(20) }
    var progress by remember { mutableStateOf(0) }


    LaunchedEffect(Unit) {
        fetchEvents { fetchedEvents ->
            eventList = fetchedEvents
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Events",
                        style = TextStyle(
                            fontSize = 30.sp,
                            fontFamily = myCustomFontFamily,
                        )
                        , color = Color.White)
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate(ROUTE_DASHBOARD)
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xffffffff))
            ) {
                if (isLoading) {
                    // Progress indicator
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(progress = progress / 100f)
                        Text(text = "Please wait... $progress%",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = AboutFont,
                                
                            ),
                            )
                    }
                } else {
                    if (eventList.isEmpty()) {
                        // No events found
                        Text(text = "No events found",
                            style = TextStyle(
                                fontSize = 20.sp,
                                fontFamily = AboutFont,
                                )
                        )
                    } else {
                        // Events list
                        LazyVerticalGrid(columns = GridCells.Fixed(1)) {
                            items(eventList.take(displayedEventCount)) { event ->
                                EventListItem(event) {
                                    navController.navigate("eventDetail/${event.id}")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // Load More Button
                        if (displayedEventCount < eventList.size) {
                            Button(
                                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xff3A7CA5)),
                                onClick = { displayedEventCount += 8},
                                modifier = Modifier.align(Alignment.CenterHorizontally)
                            ) {
                                Text(text = "More Events",
                                    style = TextStyle(
                                        fontSize = 20.sp,
                                        fontFamily = AboutFont,
                                        color = Color.White
                                    )

                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {BottomBar(navController = navController)}
    )
}

@Composable
fun EventListItem(event: Event, onItemClick: (String) -> Unit) {
    var isClicked by remember { mutableStateOf(false) }

    // Animated color transition
    val iconColor by animateColorAsState(
        targetValue = if (isClicked) Color.Red else Color(0xff87ceeb)
    )
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(350.dp)
            .padding(8.dp)
            .clickable { onItemClick(event.id) },
        border = BorderStroke(2.dp, Color(0xff87ceeb))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize()
        ) {
            // Event Image
            Image(
                painter = rememberAsyncImagePainter(event.imageUrl),
                contentDescription = null,
//                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxHeight()
//                    .aspectRatio(6f/2f)

            )

            Spacer(modifier = Modifier.width(16.dp))

            // Event Details
            Column (
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(16.dp)

            ){


                Row {

                    Text(
                        text = "Details",
                        style = TextStyle(
                            fontSize = 18.sp,
                            fontFamily = myCustomFontFamily
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = {
                        isClicked = !isClicked}
                    )
                    {
                        Icon(
                            imageVector = if (isClicked) Icons.Default.Favorite
                            else Icons.Default.FavoriteBorder,
                            contentDescription =null,
                            tint = Color(0xff87ceeb)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Event Name",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = myCustomFontFamily
                    )
                )

                Text(text = event.name,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = AboutFont)
                )
                Spacer(modifier = Modifier.height(5.dp))

                Text(text = "Event Location",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = myCustomFontFamily
                    )
                )
                Text(text = event.location,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontFamily = AboutFont)
                )
            }
        }
    }
}

private suspend fun fetchEvents(onSuccess: (List<Event>) -> Unit) {
    val firestore = Firebase.firestore
    val snapshot = firestore.collection("events").get().await()
    val eventList = snapshot.documents.mapNotNull { doc ->
        val event = doc.toObject<Event>()
        event?.id = doc.id
        event
    }
    onSuccess(eventList)
}

suspend fun fetchEvent(eventId: String, onSuccess: (Event?) -> Unit) {
    val firestore = Firebase.firestore
    val docRef = firestore.collection("events").document(eventId)
    val snapshot = docRef.get().await()
    val event = snapshot.toObject<Event>()
    onSuccess(event)
}

