package com.example.instagramapp.ui

import android.os.Build
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.hilt.navigation.compose.hiltViewModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.example.instagramapp.viewmodel.PostViewModel

class BottomNavigationActivity : Screen {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @Composable
    override fun Content() {
        val selectedIndex = rememberSaveable { mutableIntStateOf(0) }

        
        Scaffold(
            bottomBar = {
             NavigationBar {
                    list.forEachIndexed { i, item ->
                        val isSelected = selectedIndex.intValue == i
                        val color = if (isSelected) Color.Black
                        else Color.Gray.copy(alpha = 0.8f)
                        NavigationBarItem(
                            selected = isSelected,
                            label = { Text(item.title, color = color) },
                            onClick = {
                                selectedIndex.intValue = i
                            },
                            icon = {
                                Icon(
                                    item.selectedIcon,
                                    contentDescription = null,
                                    tint = color
                                )
                            })
                    }
                }
            }
        ) {
            BackHandler(enabled = selectedIndex.intValue != 0) {
                if (selectedIndex.intValue != 0) {
                    selectedIndex.intValue = 0
                }
            }
            when(selectedIndex.intValue){
                    0 -> Navigator(screen = HomeActivity(modifier = Modifier.padding(it)))
                    1 -> Text(text = "Profile")
                    2 -> Text(text = "Notifications")
                }
        }
    }
}

private val list = listOf(
    BottomNavigationItem(title = "Home", selectedIcon = Icons.Rounded.Home),
    BottomNavigationItem(title = "Profile", selectedIcon = Icons.Rounded.Person),
    BottomNavigationItem(title = "Notifications", selectedIcon = Icons.Rounded.Notifications),
)

private data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
)
