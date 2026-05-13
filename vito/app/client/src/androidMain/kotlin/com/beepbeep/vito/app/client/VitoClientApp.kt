/**
 * Vito Client App - End-user application
 */
package com.beepbeep.vito.app.client

import android.app.Application

class VitoClientApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Firebase initialized in manifest
    }
}