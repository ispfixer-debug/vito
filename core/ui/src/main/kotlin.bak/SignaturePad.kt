package com.vito.core.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import java.io.ByteArrayOutputStream

data class SignaturePath(
    val path: Path = Path(),
    val color: Color = Color.Black
)

@Composable
fun SignaturePad(
    onSignatureCaptured: (ByteArray?) -> Unit,
    modifier: Modifier = Modifier,
    strokeColor: Color = Color.Black,
    backgroundColor: Color = Color.White
) {
    var paths by remember { mutableStateOf(listOf<SignaturePath>()) }
    var currentPath by remember { mutableStateOf(Path()) }
    var isEmpty by remember { mutableStateOf(true) }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.outlineVariant,
                    RoundedCornerShape(8.dp)
                )
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                currentPath = Path().apply {
                                    moveTo(offset.x, offset.y)
                                }
                                isEmpty = false
                            },
                            onDrag = { change, _ ->
                                currentPath.lineTo(change.position.x, change.position.y)
                            },
                            onDragEnd = {
                                paths = paths + SignaturePath(currentPath, strokeColor)
                                currentPath = Path()
                            }
                        )
                    }
            ) {
                // Background
                drawRect(backgroundColor)

                // Completed paths
                paths.forEach { signaturePath ->
                    drawPath(
                        path = signaturePath.path,
                        color = signaturePath.color,
                        style = Stroke(
                            width = 4f,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }

                // Current path
                if (isEmpty.not()) {
                    drawPath(
                        path = currentPath,
                        color = strokeColor,
                        style = Stroke(
                            width = 4f,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            TextButton(
                onClick = {
                    paths = emptyList()
                    currentPath = Path()
                    isEmpty = true
                    onSignatureCaptured(null)
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Clear,
                    contentDescription = "Clear"
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Clear")
            }
        }
    }
}

fun captureSignature(paths: List<SignaturePath>): ByteArray? {
    if (paths.isEmpty()) return null
    
    try {
        val bitmap = Bitmap.createBitmap(800, 400, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bitmap)
        canvas.drawColor(android.graphics.Color.WHITE)
        
        val paint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
            strokeWidth = 4f
            style = android.graphics.Paint.Style.STROKE
            strokeCap = android.graphics.Paint.Cap.ROUND
            strokeJoin = android.graphics.Paint.Join.ROUND
        }
        
        paths.forEach { signaturePath ->
            canvas.drawPath(signaturePath.path.asAndroidPath(), paint)
        }
        
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream)
        return stream.toByteArray()
    } catch (e: Exception) {
        return null
    }
}