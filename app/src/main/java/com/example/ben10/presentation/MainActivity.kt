package com.example.ben10.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
import androidx.compose.foundation.Image
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.wear.compose.material.*
import com.example.ben10.R
import com.example.ben10.presentation.theme.Ben10Theme
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.compose.ui.platform.LocalView



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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SecondScreen() {
    val result = remember { mutableStateOf(1) }
    val view = LocalView.current

    fun performHapticFeedback() {
        view.performHapticFeedback(
            HapticFeedbackConstants.CLOCK_TICK,
            HapticFeedbackConstants.FLAG_IGNORE_GLOBAL_SETTING
        )
    }

    val imageResource = when (result.value) {
        0 -> R.drawable.transform
        1 -> R.drawable.ben
        2 -> R.drawable.alien1
        3 -> R.drawable.alien2
        4 -> R.drawable.alien3
        5 -> R.drawable.alien4
        6 -> R.drawable.alien5
        7 -> R.drawable.alien6
        8 -> R.drawable.alien7
        9 -> R.drawable.alien8
        10 -> R.drawable.alien9
        else -> R.drawable.alien10
    }



    Box(
        modifier = Modifier
            .fillMaxSize()
            .onRotaryScrollEvent { event ->
                result.value = when {
                    event.verticalScrollPixels > 0 -> if (result.value < 4) result.value + 1 else 1
                    else -> if (result.value > 1) result.value - 1 else 4
                }
                true
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { _, dragAmount ->
                    val oldValue = result.value
                    result.value = when {
                        dragAmount > 0 -> if (result.value < 10) result.value + 1 else 2
                        else -> if (result.value > 2) result.value - 1 else 10
                    }
                    if (oldValue != result.value) {
                        performHapticFeedback()
                    }
                }
            }
    ) {
        AnimatedContent(
            targetState = imageResource,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 300)) with
                        fadeOut(animationSpec = tween(durationMillis = 300))
            },
            modifier = Modifier.align(Alignment.Center)
        ) { targetResource ->
            Image(
                painter = painterResource(id = targetResource),
                contentDescription = "Ben 10 character ${result.value}",
                modifier = Modifier.clickable {
                    result.value = 0
                }
            )
        }
    }
}

@Preview(device = Devices.WEAR_OS_SMALL_ROUND, showSystemUi = true)
@Composable
fun DefaultPreview() {
    WearApp()
}