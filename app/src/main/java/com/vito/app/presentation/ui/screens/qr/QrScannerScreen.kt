package com.vito.app.presentation.ui.screens.qr

import android.Manifest
import android.util.Size
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.vito.app.R
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
@Composable
fun QrScannerScreen(
    onQrScanned: (String) -> Unit,
    onBack: () -> Unit = {}
) {
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    
    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.link_new_device)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        if (!cameraPermissionState.status.isGranted) {
            Box(
                modifier = Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Filled.QrCodeScanner, null, Modifier.size(64.dp), MaterialTheme.colorScheme.outline)
                    Spacer(Modifier.height(16.dp))
                    Text("Camera permission required")
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { cameraPermissionState.launchPermissionRequest() }) { Text("Grant Permission") }
                }
            }
        } else {
            Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                CameraPreview(onQrDetected = onQrScanned)
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Box(Modifier.size(250.dp).clip(RoundedCornerShape(16.dp)).border(3.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(16.dp))
                }
                Box(Modifier.fillMaxWidth().align(Alignment.BottomCenter).background(Color.Black.copy(alpha = 0.5f)).padding(24.dp), contentAlignment = Alignment.Center) {
                    Text("Point camera at QR code", Color.White, MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}

@Composable
@androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
private fun CameraPreview(onQrDetected: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var hasScanned by remember { mutableStateOf(false) }
    
    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }
                val imageAnalysis = ImageAnalysis.Builder().setTargetResolution(Size(1280, 720)).setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build().also { analysis ->
                    analysis.setAnalyzer(cameraExecutor) { imageProxy ->
                        val mediaImage = imageProxy.image
                        if (mediaImage != null && !hasScanned) {
                            val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
                            val scanner = BarcodeScanning.getClient()
                            scanner.process(image).addOnSuccessListener { barcodes ->
                                for (barcode in barcodes) {
                                    if (barcode.valueType == Barcode.TYPE_URL || barcode.valueType == Barcode.TYPE_TEXT) {
                                        barcode.rawValue?.let { value ->
                                            if (!hasScanned) {
                                                hasScanned = true
                                                onQrScanned(value)
                                            }
                                        }
                                    }
                                }
                            }.addOnCompleteListener { imageProxy.close() }
                        } else { imageProxy.close() }
                    }
                }
                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, cameraSelector, preview, imageAnalysis)
                } catch (e: Exception) { e.printStackTrace() }
            }, ContextCompat.getMainExecutor(ctx))
            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}