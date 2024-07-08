package net.ezra.ui.products

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import net.ezra.navigation.ROUTE_VIEW_PROD
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import net.ezra.navigation.ROUTE_UPDATE_PRODUCT
import net.ezra.ui.about.AboutFont
import net.ezra.ui.dashboard.BottomBar
import net.ezra.ui.dashboard.myCustomFontFamily


@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailScreen(navController: NavController, eventId: String) {

    var event by remember { mutableStateOf<Event?>(null) }

    LaunchedEffect(Unit) {
        fetchEvent(eventId) { fetchedEvent ->
            event = fetchedEvent
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    // Display the event name if available
                    Text(
                        text = event?.name ?: "Details",
                        fontSize = 30.sp,
                        color = Color.White
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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xffffffff)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                event?.let {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center

                    ) {
                        
                        Text(text = "")
                        Card(
                            colors = CardDefaults.cardColors(Color(0xffffffff)),
                            border = BorderStroke(2.dp, Color(0xff87ceeb)),
                            modifier = Modifier
                                .padding(16.dp)
                                .height(600.dp)

                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .border(1.dp, Color.White, RoundedCornerShape(4.dp))
                            ) {

                                Image(
                                    painter = rememberAsyncImagePainter(it.imageUrl),
                                    contentDescription = null,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(300.dp)
                                        .padding(10.dp)

                                )
                            }
                            LazyColumn(
                                Modifier
                                    .background(Color.White)
                                    .fillMaxWidth()
                                    .height(300.dp)
                                    .padding(10.dp)
                            ) {

                                item {

                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row (
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ){

                                        Text(
                                            text = it.name,
                                            style = MaterialTheme.typography.h5.copy(
                                                fontFamily = myCustomFontFamily,
                                                fontSize = 30.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        )
                                    }
                                    HorizontalDivider(
                                        color = Color(0xff87ceeb)
                                    )
                                    Spacer(modifier = Modifier.height(8.dp)

                                    )
                                    Text(text = "Event Description:",
                                        style = TextStyle(
                                            fontFamily = AboutFont,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    )
                                    Text(
                                        text = it.description,
                                        style = MaterialTheme.typography.body1.copy(
                                            fontFamily = AboutFont
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(text = "Event Location:",
                                        style = TextStyle(
                                            fontFamily = AboutFont,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp
                                        )
                                    )
                                    Text(
                                        text = it.location,
                                        style = MaterialTheme.typography.body1.copy(
                                            fontFamily = AboutFont
                                        )
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))

                OutlinedButton(onClick = {navController.navigate(ROUTE_UPDATE_PRODUCT)},
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color(0xFF3A7CA5)
                    )
                ) {
                    Text(text = "Update Product",
                        style = TextStyle(
                            fontFamily = AboutFont,
                            fontWeight = FontWeight.Bold,
                            color = Color.White),

                    )
                }
            }
        },
        bottomBar = {BottomBar(navController = navController)}
    )
}


suspend fun fetchEvent(eventId: String): Event? {
    val db = FirebaseFirestore.getInstance()
    val eventsCollection = db.collection("events")

    return try {
        val documentSnapshot = eventsCollection.document(eventId).get().await()
        if (documentSnapshot.exists()) {
            val eventData = documentSnapshot.data ?: return null
            Event(
                id = eventId,
                name = eventData["name"] as String,
                // Add other event properties here
            )
        } else {
            null
        }
    } catch (e: Exception) {
        null
    }
}
