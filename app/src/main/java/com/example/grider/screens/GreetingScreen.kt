package com.example.grider.screens
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.grider.R
import com.example.grider.ui.theme.GRIDerTheme

@Composable
fun GreetingScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(48.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(240.dp)
                .padding(bottom = 48.dp)
        )
        Button(
            onClick = { navController.navigate("paper_cut") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFDF3E22),
                contentColor = Color.White
            )
        ) {
            Text(text = "Paper Cut")
        }
        Button(
            onClick = { navController.navigate("image_cut") },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFDF3E22),
                contentColor = Color.White
            )
        ) {
            Text(text = "Image Cut")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GRIDerTheme {
        GreetingScreen(navController = rememberNavController())
    }
}