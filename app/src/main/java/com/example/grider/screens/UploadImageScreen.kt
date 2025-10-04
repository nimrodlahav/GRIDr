package com.example.grider.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grider.R
import com.example.grider.components.UploadImageView
import com.example.grider.models.colsState
import com.example.grider.models.imageUriState
import com.example.grider.models.rowsState
import com.example.grider.models.rotationState
import com.example.grider.utils.getImageDimensions

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadImageScreen(navController: NavController, width: String, height: String) {
    // 🔹 Shared states
    rowsState = remember { mutableStateOf(1) }
    colsState = remember { mutableStateOf(1) }
    imageUriState = remember { mutableStateOf<Uri?>(null) }
    rotationState = remember { mutableStateOf(0f) }

    var selectedImageUri by imageUriState
    val context = LocalContext.current
    var imageWidth by remember { mutableStateOf(0) }
    var imageHeight by remember { mutableStateOf(0) }

    // 🔹 Image picker
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        selectedImageUri = uri
        uri?.let {
            getImageDimensions(context, it)?.let { (w, h) ->
                imageWidth = w
                imageHeight = h
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 🔹 Logo
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 Show previews if image is uploaded
            if (selectedImageUri != null) {
                UploadImageView(
                    selectedImageUri = selectedImageUri,
                    rows = rowsState.value,
                    cols = colsState.value,
                    paperWidthCm = width.toFloatOrNull() ?: 21f,
                    paperHeightCm = height.toFloatOrNull() ?: 29.7f,
                    rotation = rotationState.value
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 Controls
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Button(
                    onClick = {
                        launcher.launch(
                            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                        )
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = androidx.compose.ui.graphics.Color(0xFFDF3E22),
                        contentColor = androidx.compose.ui.graphics.Color.White
                    )
                ) {
                    Text("Upload Image")
                }

                if (selectedImageUri != null) {
                    Button(
                        onClick = {
                            rotationState.value = (rotationState.value + 90f) % 360f
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = androidx.compose.ui.graphics.Color.Gray,
                            contentColor = androidx.compose.ui.graphics.Color.White
                        )
                    ) {
                        Text("Rotate")
                    }
                }
            }
        }
    }
}
