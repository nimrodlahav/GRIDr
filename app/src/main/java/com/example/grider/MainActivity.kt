package com.example.grider
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.grider.models.CutSheetView
import com.example.grider.models.GridOnCutSheetView
import com.example.grider.models.GridOnImageView
import com.example.grider.models.ImageView
import com.example.grider.models.RotateView
import com.example.grider.models.SideBySideGridView
import com.example.grider.screens.GreetingScreen
import com.example.grider.screens.ImageCutScreen
import com.example.grider.screens.PaperCutScreen
import com.example.grider.screens.UploadImageScreen
import com.example.grider.ui.theme.GRIDerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GRIDerTheme {
                val navController = rememberNavController()

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFE4E2DD) // beige from logo background
                ) {
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { GreetingScreen(navController) }
                        composable("paper_cut") { PaperCutScreen(navController) }
                        composable("image_cut") { ImageCutScreen(navController) }
                        composable("upload_image/{width}/{height}") { backStackEntry ->
                            val width = backStackEntry.arguments?.getString("width") ?:""
                            val height = backStackEntry.arguments?.getString("height") ?: ""
                            UploadImageScreen(navController, width, height) }
                    }
                }
            }
        }
    }
}


