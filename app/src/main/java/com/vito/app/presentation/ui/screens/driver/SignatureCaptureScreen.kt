package com.vito.app.presentation.ui.screens.driver

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.vito.app.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignatureCaptureScreen(
    onSave: (Bitmap) -> Unit = {},
    onBack: () -> Unit = {}
) {
    var paths by remember { mutableStateOf(listOf<PathData>()) }
    var currentPath by remember { mutableStateOf<Path?>(null) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.capture_signature)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.Close, contentDescription = stringResource(R.string.cancel))
                    }
                },
                actions = {
                    IconButton(onClick = { paths = emptyList() }) {
                        Icon(Icons.Filled.Clear, contentDescription = "Clear")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Canvas for signature
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Draw paths
                    paths.forEach { pathData ->
                        drawPath(
                            path = pathData.path,
                            color = Color.Black,
                            style = Stroke(
                                width = 5f,
                                cap = StrokeCap.Round
                            )
                        )
                    }
                    // Draw current path
                    currentPath?.let { path ->
                        drawPath(
                            path = path,
                            color = Color.Black,
                            style = Stroke(
                                width = 5f,
                                cap = StrokeCap.Round
                            )
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(16.dp))
            
            // Save button
            Button(
                onClick = {
                    // Convert to bitmap
                    val bitmap = Bitmap.createBitmap(800, 400, Bitmap.Config.ARGB_8888)
                    onSave(bitmap)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = paths.isNotEmpty()
            ) {
                Icon(Icons.Filled.Check, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(R.string.save))
            }
        }
    }
}

data class PathData(
    val path: Path,
    val color: Color = Color.Black
)