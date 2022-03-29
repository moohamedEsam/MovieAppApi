package com.example.movieappapi.presentation.components

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.StarRate
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ExperimentalMotionApi
import androidx.constraintlayout.compose.MotionLayout
import androidx.constraintlayout.compose.MotionScene
import com.example.movieappapi.R
import com.example.movieappapi.ui.theme.MovieAppApiTheme

@OptIn(ExperimentalMotionApi::class, androidx.compose.material.ExperimentalMaterialApi::class)
@Composable
fun RateMotionLayout(
    initialProgress: Float = (0).toFloat(),
    onDismissRequest: () -> Unit,
    onRemoveRate: () -> Unit = {},
    onRate: (Float) -> Unit
) {
    val context = LocalContext.current
    val scene = context.resources.openRawResource(R.raw.rate_layout).readBytes().decodeToString()
    var progress by remember {
        mutableStateOf(initialProgress)
    }

    Dialog(onDismissRequest = { onDismissRequest() }) {
        Column(
            modifier = Modifier
                .background(Color.LightGray)
                .padding(8.dp)
        ) {
            MotionLayout(
                motionScene = MotionScene(content = scene),
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                val properties = motionProperties(id = "star")
                Icon(
                    imageVector = Icons.Default.StarRate,
                    contentDescription = null,
                    modifier = Modifier.layoutId("star"),
                    tint = properties.value.color("background")
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Slider(
                value = progress,
                onValueChange = { progress = it },
                steps = 19
            )

            Text(
                text = "%.1f".format(progress * 10),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.align(Alignment.End),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        onRate(progress * 10)
                        onDismissRequest()
                    },
                ) {
                    Text(text = "rate")
                }
                Spacer(modifier = Modifier.width(4.dp))

                Text(
                    text = "remove rate",
                    color = Color.Red,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            onRemoveRate()
                            onDismissRequest()
                        })

            }
        }
    }
}

@Preview
@Composable
fun DialogPreview() {
    MovieAppApiTheme {
        var progress by remember {
            mutableStateOf(0f)
        }
        SideEffect {
            Log.i("rateMotionLayout", "DialogPreview: $progress")
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {

            Slider(
                value = progress,
                onValueChange = { progress = it },
//                valueRange = 0.5f..10.0f,
                steps = 19
            )
        }
    }
}