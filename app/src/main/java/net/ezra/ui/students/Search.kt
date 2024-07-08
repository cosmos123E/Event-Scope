package net.ezra.ui.students
//
//
//import android.annotation.SuppressLint
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.CircularProgressIndicator
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextField
//import androidx.compose.runtime.*
//import androidx.compose.ui.*
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.text.input.TextFieldValue
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//import coil.compose.SubcomposeAsyncImage
//import coil.request.ImageRequest
//import com.google.firebase.Firebase
//import com.google.firebase.firestore.firestore
//
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.automirrored.filled.ArrowBack
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material3.AlertDialogDefaults.containerColor
//import androidx.compose.material3.CenterAlignedTopAppBar
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.material3.TopAppBarDefaults
//import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
//import androidx.compose.ui.graphics.Color
//import androidx.navigation.NavController
//import net.ezra.R
//import net.ezra.navigation.ROUTE_DASHBOARD
//import net.ezra.navigation.ROUTE_HOME
//import net.ezra.navigation.ROUTE_SEARCH
//import net.ezra.ui.products.Event
//
//
//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "ResourceAsColor")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun Search(navController: NavHostController) {
//    var searchText by remember { mutableStateOf(TextFieldValue()) }
//    var filteredData by remember { mutableStateOf(emptyList<Event>()) }
//
//    // Firestore reference
//    val firestore = Firebase.firestore
//
//    DisposableEffect(searchText.text) {
//        val query = firestore.collection("event")
//            .whereGreaterThanOrEqualTo("EventName", searchText.text)
//            .whereLessThanOrEqualTo("EventName", searchText.text + "\uf8ff")
//
//
//        val listener = query.addSnapshotListener { snapshot, error ->
//            if (error != null) {
//                // Handle error
//                return@addSnapshotListener
//            }
//
//            snapshot?.let {
//                val data = it.toObjects(Event::class.java)
//                filteredData = data
//            }
//        }
//
//        onDispose {
//            listener.remove()
//        }
//    }
//    Scaffold(
//        topBar = {
//            CenterAlignedTopAppBar(
//                title = {
//                    Text(text = "Search Events")
//                },
//                navigationIcon = {
//                    IconButton(onClick = {
//                        navController.navigate(ROUTE_DASHBOARD)
//
//                    }) {
//                        Icon(
//                            Icons.AutoMirrored.Filled.ArrowBack,
//                            "backIcon",
//                            tint = Color.White
//                        )
//                    }
//                },
//
//
//
//                colors = topAppBarColors(
//        containerColor = Color(0xff3A7CA5),
//
//
//        titleContentColor = Color.White,
//    ),
//            )
//        },
//
//        content = {
//            Column(
//
//                modifier = Modifier
//                    .background(Color(0xff87ceeb))
//                    .fillMaxSize()
//
//            ) {
//
//                Spacer(modifier = Modifier.height(55.dp))
//
//                OutlinedTextField(
//                    value = searchText,
//                    onValueChange = { searchText = it },
//                    placeholder = { Text("Search Event..") },
//                    modifier = Modifier.fillMaxWidth(),
//                    trailingIcon = {
//                        Icon(imageVector = Icons.Default.Search,
//                            contentDescription = "emailIcon"
//                        ) },
//
//                )
//
//                Spacer(modifier = Modifier.height(5.dp))
//
//
//    LazyVerticalGrid(columns = GridCells.Fixed(1),) {
//
//    items(filteredData) { item ->
//
//        Column (
//            modifier = Modifier
//                .padding(it)
//                .fillMaxSize()
//                ,
//            horizontalAlignment = Alignment.CenterHorizontally
//
//        ){
//
//            SubcomposeAsyncImage(
//                model = ImageRequest.Builder(LocalContext.current)
//                    .data(item.imageUrl)
//                    .crossfade(true)
//                    .build(),
//                loading = {
//                    CircularProgressIndicator()
//                },
//                contentDescription = item.name,
//                modifier = Modifier
//                    .clip(RoundedCornerShape(10))
//                    .size(150.dp)
//
//            )
//
//            item.name?.let { Text(text = it) }
//            item.description?.let { Text(text = it) }
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
//        }
//
//    }
//
//}
//
//            }
//        },
//
//        bottomBar = { net.ezra.ui.dashboard.BottomBar(navController = navController) }
//
//
//        )
//
//
//
//}
//


import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.ui.graphics.Color
import net.ezra.R
import net.ezra.navigation.ROUTE_DASHBOARD
import net.ezra.ui.products.Event

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Search(navController: NavHostController) {
    var searchText by remember { mutableStateOf(TextFieldValue()) }
    var filteredData by remember { mutableStateOf(emptyList<Event>()) }

    // Firestore reference
    val firestore = Firebase.firestore

    DisposableEffect(searchText.text) {
        val query = firestore.collection("event")
            .whereGreaterThanOrEqualTo("EventName", searchText.text)
            .whereLessThanOrEqualTo("EventName", searchText.text + "\uf8ff")

        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            snapshot?.let {
                val data = it.toObjects(Event::class.java)
                filteredData = data
            }
        }

        onDispose {
            listener.remove()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Search Events") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ROUTE_DASHBOARD) }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "backIcon",
                            tint = Color.White
                        )
                    }
                },
                colors = topAppBarColors(
                    containerColor = Color(0xff3A7CA5),
                    titleContentColor = Color.White,
                ),
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .background(Color(0xff87ceeb))
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(55.dp))

                OutlinedTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    placeholder = { Text("Search Event..") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "searchIcon"
                        )
                    },
                )

                Spacer(modifier = Modifier.height(5.dp))

                LazyVerticalGrid(columns = GridCells.Fixed(1)) {
                    items(filteredData) { item ->
                        Column(
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            SubcomposeAsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(item.imageUrl)
                                    .crossfade(true)
                                    .build(),
                                loading = { CircularProgressIndicator() },
                                contentDescription = item.name,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .size(150.dp)
                            )

                            item.name.let { Text(text = it) }
                            item.description.let { Text(text = it) }
                        }
                    }
                }
            }
        },
        bottomBar = { net.ezra.ui.dashboard.BottomBar(navController = navController) }
    )
}
