package net.ezra.ui.dashboard



import android.annotation.SuppressLint
import android.app.ProgressDialog
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import net.ezra.R
import net.ezra.navigation.ROUTE_ABOUT
import net.ezra.navigation.ROUTE_ADD_PRODUCT
import net.ezra.navigation.ROUTE_DASHBOARD
import net.ezra.navigation.ROUTE_MYPROFILE
import net.ezra.navigation.ROUTE_SEARCH
import net.ezra.navigation.ROUTE_VIEW_PROD
import net.ezra.ui.about.AboutFont
import net.ezra.ui.auth.AuthHeader
import net.ezra.ui.products.Event


val myCustomFontFamily = FontFamily(
    Font(R.font.poppins_black, FontWeight.Normal),

)


private var progressDialog: ProgressDialog? = null
@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun DashboardScreen(navController: NavHostController)  {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val firestore = FirebaseFirestore.getInstance()
    var user: User? by remember { mutableStateOf(null) }
    var isLoading by remember { mutableStateOf(true) }
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var loading by remember { mutableStateOf(false) }

    var isDrawerOpen by remember { mutableStateOf(false) }

        val firestores = Firebase.firestore


    val context = LocalContext.current

    BackHandler {
        navController.popBackStack()

    }


    // Fetch user details from Firestore
    LaunchedEffect(key1 = currentUser?.uid) {
        if (currentUser != null) {
            val userDocRef = firestore.collection("users").document(currentUser.uid)
            userDocRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        user = document.toObject<User>()
                    }
                    isLoading = false
                }
                .addOnFailureListener { e ->
                    // Handle failure
                    isLoading = false
                }
        }
    }





    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.apen),
                        color = Color.White,
                        style = MaterialTheme.typography.h3.copy(
                            fontFamily = myCustomFontFamily
                        )
                    )
                },
                navigationIcon = @Composable {
                    if (!isDrawerOpen) {
                        IconButton(onClick = { isDrawerOpen = true }) {
                            Icon(
                                Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                    }
                },

                actions = {
                    IconButton(onClick = {
                        navController.navigate(ROUTE_MYPROFILE)


                    }) {
                        Icon(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                },

                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xff3A7CA5),
                    titleContentColor = Color.White,

                    )

            )
        }, content = @Composable {

            Spacer(modifier = Modifier.height(10.dp))



            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xffffffff))
            ){

                item {


                    Text(text = "Make It Happen!",
                        style = MaterialTheme.typography.h4.copy(
                            fontFamily = myCustomFontFamily,
                            color = Color.Black
                        ))

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .clickable {
                                if (isDrawerOpen) {
                                    isDrawerOpen = false
                                }
                            }
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xffffffff)),
//                verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(350.dp)
                                ) {
                                    // Combine overlapping modifiers for Image
                                    Image(
                                        painter = painterResource(id = R.drawable.pic11),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    // Position Text elements at the bottom using padding
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(bottom = 16.dp, end = 16.dp) // Padding from bottom and right
                                    ) {
                                        Text(
                                            text = "All About Events!",
//                                            textDecoration = TextDecoration.Underline,
                                            textAlign = TextAlign.Right,
                                            style = MaterialTheme.typography.h3.copy(
                                                fontFamily = myCustomFontFamily
                                            ),
                                            color = Color.White,
                                        )
                                        Spacer(modifier = Modifier.height(12.dp)) // Smaller spacer

                                        Row(
                                            modifier = Modifier
                                                .padding(16.dp)
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Text(
                                                text = "Connect with like-minded people! Event Scope makes it easy to discover events and find others who share your passions.",
                                                style = MaterialTheme.typography.h4.copy(
                                                    fontSize = 18.sp,
                                                    fontFamily = AboutFont
                                                ),
                                                color = Color.White,
                                                textAlign = TextAlign.Start,
                                                modifier = Modifier
                                                    .weight(1f)
                                            )
                                        }

                                    }
                                }
                            }

                            Row {
                                Text(text = "For You",
                                    style = TextStyle(
                                        fontFamily = AboutFont,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 20.sp,
                                        color = Color(0xff000000),
                                    ),
                                    modifier = Modifier
                                        .padding(top = 8.dp)
                                )


                            }
                            LazyRow (
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ){
                                item {


                                    Card(

                                        backgroundColor = Color.White,
                                        modifier = Modifier
                                            .height(250.dp)
                                            .width(200.dp)
                                    ) {
                                        Image(painter = painterResource(id = R.drawable.card11),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .height(250.dp)
                                                .width(200.dp))

                                    }

                                    Spacer(
                                        modifier = Modifier
                                            .width(8.dp)
                                    )
                                    Card(
                                        backgroundColor = Color.White,
                                        modifier = Modifier
                                            .height(250.dp)
                                            .width(200.dp)
                                    ) {


                                        Image(painter = painterResource(id = R.drawable.card2),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .height(250.dp)
                                                .width(200.dp))
                                    }

                                    Spacer(
                                        modifier = Modifier
                                            .width(8.dp)
                                    )
                                    Card(
                                        backgroundColor = Color.White,
//                                    ),
                                        modifier = Modifier
                                            .height(250.dp)
                                            .width(200.dp)
                                    ) {
                                        Image(painter = painterResource(id = R.drawable.card3),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .height(250.dp)
                                                .width(200.dp))
                                    }

                                    Spacer(
                                        modifier = Modifier
                                            .width(8.dp)
                                    )

                                    Card(
                                        backgroundColor = Color.White,
                                        modifier = Modifier
                                            .height(250.dp)
                                            .width(200.dp)

                                    ) {
                                        Image(painter = painterResource(id = R.drawable.card4),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .height(250.dp)
                                                .width(200.dp))
                                    }

                                    Spacer(modifier = Modifier.width(5.dp))



                                    IconButton(onClick = {
                                        navController.navigate(ROUTE_VIEW_PROD) },

                                        modifier = Modifier
                                            .border(1.dp,Color.Gray, CircleShape)


                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                            contentDescription = null,
                                            tint = Color.Black
                                        )
                                    }
                                }
                            }

                            Row {
                                Text(
                                    text = "For Event Planners",
                                    style = MaterialTheme.typography.h4.copy(
                                        fontFamily = myCustomFontFamily
                                    ),
                                    color = Color.White
                                )


                            }

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(350.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(350.dp)
                                ) {
                                    // Combine overlapping modifiers for Image
                                    Image(
                                        painter = painterResource(id = R.drawable.pic12),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    // Position Text elements at the bottom using padding
                                    Column(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(
                                                bottom = 16.dp,
                                                end = 16.dp
                                            ) // Padding from bottom and right
                                    ) {
                                        Text(
                                            text = "For Event Planners!",
                                            textAlign = TextAlign.Right,
                                            style = MaterialTheme.typography.h3.copy(
                                                fontFamily = myCustomFontFamily
                                            ),
                                            color = Color.White
                                        )
                                        Spacer(modifier = Modifier.height(12.dp)) // Smaller spacer

                                        Row(
                                            modifier = Modifier
                                                .padding(top = 16.dp)
                                                .fillMaxWidth(),
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.Start
                                        ) {
                                            Text(
                                                text = "Go mobile-friendly! Event Scope offers a seamless event experience for both you and your attendees through our mobile app..",
                                                textAlign = TextAlign.Right,
                                                style = MaterialTheme.typography.h5.copy(
                                                    fontFamily = AboutFont
                                                ),
                                                color = Color.White,

                                                )
                                        }


                                        Row (
                                            modifier = Modifier
                                                .background(Color.Transparent)
                                                .fillMaxWidth()
                                        ){
                                            Spacer(modifier = Modifier.weight(1f))

                                            TextButton(
                                                onClick = {
                                                    navController.navigate(
                                                        ROUTE_ADD_PRODUCT
                                                    )
                                                },
                                                colors = ButtonDefaults.buttonColors(
                                                    Color(0xff87ceeb)
                                                ),

                                                )

                                            {
                                                Text(
                                                    text = "ADD EVENT",
                                                    style = TextStyle(
                                                        fontFamily = AboutFont
                                                    )
                                                )
                                            }
                                        }
                                    }
                                }

                            }


































                        }
                    }
                }
            }
        },

        bottomBar = { BottomBar(navController = navController) }


    )

    AnimatedDrawer(
        isOpen = isDrawerOpen,
        navController
    ) { isDrawerOpen = false }


}
//

@Composable
fun AnimatedDrawer(isOpen: Boolean, navController: NavHostController, onClose: () -> Unit) {
    val drawerWidth = remember { Animatable(if (isOpen) 250f else 0f) }
    var showContactPopup by remember { mutableStateOf(false) }


    LaunchedEffect(isOpen) {
        drawerWidth.animateTo(if (isOpen) 250f else 0f, animationSpec = tween(durationMillis = 300))
    }

    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .width(drawerWidth.value.dp),
        color = Color(0xff3A7CA5)
//        elevation = 16.dp
    ) {
        Column {

            Spacer(modifier = Modifier.height(16.dp))

            AuthHeader()

            Text(
                text = stringResource(id = R.string.developer),
                style = TextStyle(
                    fontFamily = AboutFont,
                    fontSize = 20.sp,
                    color = Color.White
                )
            )
            Spacer(modifier = Modifier.height(18.dp))


            TextButton(
                onClick = { showContactPopup = true },
                content = {
                    Text(
                        "Contact Us",
                        style = TextStyle(
                            fontFamily = AboutFont,
                            fontSize = 20.sp,
                            color = Color.White,
                        )
                    )
                }
            )

// Popup content Composable
            if (showContactPopup) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.8f)) // Transparent background
                        .clickable { showContactPopup = false }, // Dismiss on click outside
                    content = {
                        Surface(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                            shape = RoundedCornerShape(8.dp),
                            contentColor = Color.Black,
                            elevation = 4.dp,
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "Need some assistance? Let us know!",
                                    style = TextStyle(
                                        fontFamily = AboutFont,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                // Add your contact information here (e.g., email, phone number)
                                Text(
                                    text = "Email: puritysang180@gmail.com",
                                    style = TextStyle(
                                        fontFamily = AboutFont,
                                    )
                                )
                                Text(
                                    text = "Phone: +254745370617",
                                    style = TextStyle(
                                        fontFamily = AboutFont,
                                    )
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                                Button(
                                    onClick = { navController.navigate(ROUTE_DASHBOARD) },
                                    colors = ButtonDefaults.buttonColors(Color(0xff3a7ca5))
                                ) {
                                    Text(
                                        "Close",
                                        style = TextStyle(
                                            fontFamily = AboutFont,
                                        )
                                    )
                                }
                            }
                        }
                    }
                )
            }


            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate(ROUTE_ABOUT) }) {
                Text(
                    text = "About us",
                    style = TextStyle(
                        fontFamily = AboutFont,
                        fontSize = 20.sp,
                        color = Color.White,
                    )
                )
            }
            Spacer(
                modifier = Modifier
                    .height(400.dp)
            )
            Text(
                text = stringResource(id = R.string.apen),
                style = MaterialTheme.typography.h4.copy(
                    fontFamily = myCustomFontFamily,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black
                ),

                )
        }
    }
}







@Composable
fun BottomBar(navController: NavController) {
    val selectedIndex = remember { mutableStateOf(0) }
    BottomNavigation(
        elevation = 10.dp,
        backgroundColor = Color(0xff3A7CA5)


    ) {

        BottomNavigationItem(icon = {
            Icon(imageVector = Icons.Default.Home, "", tint = Color.White)
        },
            label = {
                Text(
                    text = "Home",
                    style = TextStyle(
                        fontFamily = AboutFont,
                        color = Color.White
                    )
                )
            },
            selected = (selectedIndex.value == 0),
            onClick = {
                navController.navigate(ROUTE_DASHBOARD)

            })

        BottomNavigationItem(icon = {
            Icon(painter = painterResource(id = R.drawable.event1),
                tint = Color.White,
                modifier = Modifier
                    .size(20.dp),
                contentDescription = null )

        },
            label = {
                Text(
                    text = "Events",
                    style = TextStyle(
                        fontFamily = AboutFont,
                        color = Color.White
                    )
                )
            },
            selected = (selectedIndex.value == 1),
            onClick = {
                navController.navigate(ROUTE_VIEW_PROD)

            }
        )


    }
}


suspend fun fetchEvent(eventId: String): Event? {
    val db = FirebaseFirestore.getInstance()
    return try {
        val document = db.collection("events").document(eventId).get().await()
        document.toObject(Event::class.java)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun EventCard(eventId: String) {
    var event by remember { mutableStateOf<Event?>(null) }


    LaunchedEffect(eventId) {
            event = fetchEvent(eventId)
    }

    if (event != null) {
        LazyRow {
            item {

                Card(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    elevation = 4.dp
                ) {
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                    ) {
                        Text(text = event!!.name,
                            style = TextStyle(
                                fontFamily = AboutFont,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = event!!.description, style = TextStyle(
                            fontFamily = AboutFont,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = event!!.location, style = TextStyle(
                            fontFamily = AboutFont,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold)
                        )

                    }
                }
            }
        }
    } else {
        Row(
            modifier = Modifier
                .fillMaxSize(),
            verticalAlignment  = Alignment.CenterVertically
        ) {
            Text(text = "Loading...", 
                style = TextStyle(
                fontFamily = AboutFont,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold)
            )
        }
    }
}

data class User(
    val userId: String = "",
    val school: String = "",
    val name: String = ""
)

fun saveUserDetails(user: User, param: (Any) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection("users").document(user.userId)
        .set(user, SetOptions.merge())
        .addOnSuccessListener {

            progressDialog?.dismiss()
            // Success message or navigation
        }
        .addOnFailureListener {

            progressDialog?.dismiss()
            // Handle failure
        }
}
