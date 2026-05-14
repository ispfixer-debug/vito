package com.vito.core.ui.components

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun QrCodeImage(
    content: String,
    modifier: Modifier = Modifier,
    size: Int = 512,
    foregroundColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.Black,
    backgroundColor: androidx.compose.ui.graphics.Color = androidx.compose.ui.graphics.Color.White
) {
    var bitmap by remember(content, size) { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(content, size) {
        bitmap = withContext(Dispatchers.Default) {
            try {
                val hints = mapOf(
                    EncodeHintType.MARGIN to 1,
                    EncodeHintType.CHARACTER_SET to "UTF-8"
                )
                val writer = QRCodeWriter()
                val bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, size, size, hints)
                
                val pixels = IntArray(size * size)
                for (y in 0 until size) {
                    for (x in 0 until size) {
                        pixels[y * size + x] = if (bitMatrix[x, y]) {
                            android.graphics.Color.rgb(foregroundColor.red, foregroundColor.green, foregroundColor.blue)
                        } else {
                            android.graphics.Color.rgb(backgroundColor.red, backgroundColor.green, backgroundColor.blue)
                        }
                    }
                }
                Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888).apply {
                    setPixels(pixels, 0, size, 0, 0, size, size)
                }
            } catch (e: Exception) {
                null
            }
        }
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(backgroundColor)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        bitmap?.let {
            Image(
                bitmap = it.asImageBitmap(),
                contentDescription = "QR Code",
                modifier = Modifier.size(size.dp)
            )
        } ?: Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text("Loading...", style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun GenerateQrCode(
    token: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        QrCodeImage(
            content = token,
            size = 256
        )
    }
}
