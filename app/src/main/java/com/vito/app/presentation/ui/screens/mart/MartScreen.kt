package com.vito.app.presentation.ui.screens.mart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vito.app.data.mock.MockData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MartScreen() {
    Scaffold(
        topBar = { TopAppBar(title = { Text("Mart") }) },
        floatingActionButton = { FloatingActionButton(onClick = {}) { Icon(Icons.Filled.ShoppingCart, "Cart") } }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            ScrollableTabRow(selectedTabIndex = 0) {
                listOf("All", "Drinks", "Snacks").forEach { Tab(selected = it == "All", onClick = {}, text = { Text(it) }) }
            }
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(MockData.mockProducts) { product ->
                    Card(Modifier.fillMaxWidth()) {
                        Column {
                            AsyncImage(
                                model = product.imageUrls.firstOrNull(),
                                contentDescription = product.name,
                                modifier = Modifier.height(100.dp),
                                contentScale = ContentScale.Crop
                            )
                            Column(Modifier.padding(8.dp)) {
                                Text(product.name)
                                Text("$${product.price}")
                                Button(onClick = {}, modifier = Modifier.fillMaxWidth()) {
                                    Icon(Icons.Filled.Add, null)
                                    Text("Add")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
