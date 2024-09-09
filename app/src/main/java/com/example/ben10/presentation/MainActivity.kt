/* While this template provides a good starting point for using Wear Compose, you can always
 * take a look at https://github.com/android/wear-os-samples/tree/main/ComposeStarter and
 * https://github.com/android/wear-os-samples/tree/main/ComposeAdvanced to find the most up to date
 * changes to the libraries and their usages.
 */
package com.example.ben10.presentation

import android.os.Bundle
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.*
import com.example.ben10.R // This import is necessary for accessing drawables
import com.example.ben10.presentation.theme.Ben10Theme
import coil.load



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setTheme(android.R.style.Theme_DeviceDefault)

        setContent {
            WearApp()
        }
    }
}

@Composable
fun WearApp() {
    val navController = rememberNavController()

    Ben10Theme {
        NavHost(
            navController = navController,
            startDestination = "greeting_screen"
        ) {
            composable("greeting_screen") {
                Greeting(navController)
            }
            composable("second_screen") {
                SecondScreen()
            }
        }
    }
}

@Composable
fun Greeting(navController: NavHostController) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = Color.Green,
            fontSize = 32.sp,
            text = "Hello Sir"
        )

       

        Spacer(modifier = Modifier.height(16.dp))


        Button(
            onClick = { navController.navigate("second_screen") },
            modifier = Modifier
                .width(120.dp)
                .height(32.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Gray)
        ) {
            Text(
                textAlign = TextAlign.Center,
                text = "Start",
                fontSize = 24.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 3.dp)
            )
        }
    }
}

@Composable
fun SecondScreen() {

    val result = remember { mutableStateOf(1) }


    val imageResource = when (result.value) {
        1 -> R.drawable.ben
        2 -> R.drawable.alien1
        3 -> R.drawable.alien2
        4 -> R.drawable.alien3
        5 -> R.drawable.transform
        else -> R.drawable.alien4
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            // Detect bezel rotation (rotary input)
            .onRotaryScrollEvent { event ->
                if (event.verticalScrollPixels > 0) {
                    // Bezel rotated to the right (increase result value)
                    result.value = if (result.value < 4) result.value + 1 else 1
                } else {
                    // Bezel rotated to the left (decrease result value)
                    result.value = if (result.value > 1) result.value - 1 else 4
                }
                true // Indicate that the event was consumed
            }
            // Detect horizontal drag gestures (swipes)
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    if (dragAmount > 0) {
                        // Swipe right (increase result)
                        result.value = if (result.value < 4) result.value + 1 else 1
                    } else {
                        // Swipe left (decrease result)
                        result.value = if (result.value > 1) result.value - 1 else 4
                    }
                }
            }
    ) {
        // Display the image based on the result
        Image(
            painter = painterResource(id = imageResource),
            contentDescription = result.value.toString(),
            modifier = Modifier.align(Alignment.Center)
                .clickable {

                    result.value = 5
                }
        )
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}