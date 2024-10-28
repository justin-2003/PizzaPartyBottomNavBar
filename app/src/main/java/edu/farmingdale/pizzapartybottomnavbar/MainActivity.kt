package edu.farmingdale.pizzapartybottomnavbar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import edu.farmingdale.pizzapartybottomnavbar.ui.theme.PizzaPartyBottomNavBarTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PizzaPartyBottomNavBarTheme {
                val navController: NavHostController = rememberNavController()
                var buttonsVisible by remember { mutableStateOf(true) }
                var selectedItemId by remember { mutableStateOf("Pizza_Order") } // Default selected item
                val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
                val coroutineScope = rememberCoroutineScope()

                // Define menu items
                val items = listOf(
                    MenuItem(
                        id = "Pizza_Order",
                        title = "Pizza Order",
                        contentDescription = "Go to Pizza screen",
                        icon = Icons.Default.ShoppingCart
                    ),
                    MenuItem(
                        id = "Gpa_App",
                        title = "GPA App",
                        contentDescription = "Go to GPA screen",
                        icon = Icons.Default.Info
                    ),
                    MenuItem(
                        id = "Screen_3",
                        title = "Screen 3",
                        contentDescription = "Go to Screen 3",
                        icon = Icons.Default.Person
                    )
                )

                ModalNavigationDrawer(
                    drawerState = drawerState,
                    drawerContent = {
                        ModalDrawerSheet {
                            DrawerBody(
                                items = items.map { it.copy(isSelected = it.id == selectedItemId) }, // Update selected item
                                onItemClick = { selectedItem ->
                                    selectedItemId = selectedItem.id // Update selected item
                                    coroutineScope.launch { drawerState.close() } // Close drawer on item click
                                }
                            )
                        }
                    }
                ) {
                    Scaffold(
                        bottomBar = {
                            if (buttonsVisible) {
                                BottomBar(
                                    navController = navController,
                                    state = buttonsVisible,
                                    modifier = Modifier
                                )
                            }
                        },
                        topBar = {
                            TopAppBar(
                                title = { Text(text = "Pizza Party") },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        coroutineScope.launch {
                                            drawerState.open()
                                        }
                                    }) {
                                        Icon(
                                            imageVector = Icons.Default.Menu,
                                            contentDescription = "Toggle drawer"
                                        )
                                    }
                                }
                            )
                        }
                    ) { paddingValues ->
                        Box(
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            NavigationGraph(navController = navController) { isVisible ->
                                buttonsVisible = isVisible
                            }
                        }
                    }
                }
            }
        }
    }
}