package net.ezra.ui.about

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import net.ezra.R
import net.ezra.navigation.ROUTE_ABOUT
import net.ezra.navigation.ROUTE_ADD_STUDENTS
import net.ezra.navigation.ROUTE_DASHBOARD
import net.ezra.navigation.ROUTE_HOME
import net.ezra.ui.auth.AuthHeader
import net.ezra.ui.dashboard.BottomBar
import net.ezra.ui.dashboard.myCustomFontFamily


val AboutFont = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),

)


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AboutScreen(navController: NavHostController) {

    val callLauncher: ManagedActivityResultLauncher<Intent, ActivityResult> =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { _ ->

        }
   Scaffold(

        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.about),
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
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "backIcon",
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

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xffffffff)),

            ) {


                AuthHeader()


                Text(
                    text = stringResource(id = R.string.apen),
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontFamily = myCustomFontFamily,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(bottom = 16.dp)

                )
                //Add the about text here

                Text(
                    text = "Our user-friendly app takes the guesswork out of event discovery. " +
                            " Gone are the days of scouring endless websites or relying on word-of-mouth." +
                            "  Event Scope empowers you with a powerful search engine that allows you to filter your exploration " +
                            " category, location, and date.  Uncover events that align perfectly with your interests," +
                            " whether you're a music aficionado seeking the next electrifying concert," +
                            " a lifelong learner drawn to insightful workshops, " +
                            "or a social butterfly yearning to connect at captivating networking events.",

                    style = TextStyle(
                        fontFamily = AboutFont,
                        textAlign = TextAlign.Unspecified,
                    )
                )

                Spacer(modifier = Modifier.height(35.dp))

                Text(
                    text = stringResource(id = R.string.call),
                    style = TextStyle(
                        fontFamily = AboutFont,
                        color = Color.Black,
                        fontSize = 20.sp,
                    ),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable {

                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data = Uri.parse("tel:+254745370617")

                            callLauncher.launch(intent)
                        }
                )


            }

       },


            bottomBar = { BottomBar(navController = navController)}
   )

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO)
@Composable
fun HomeScreenPreviewLight() {
    AboutScreen(rememberNavController())
}

