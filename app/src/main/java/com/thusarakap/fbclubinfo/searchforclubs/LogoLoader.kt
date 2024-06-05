package com.thusarakap.fbclubinfo.searchforclubs

import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

// Load image bitmap from URL
suspend fun loadImageFromURL(url: String) = withContext(Dispatchers.IO) {
    try {
        // Creating a URL object from the URL string
        val url = URL(url)
        // Open connection to the URL
        val connection = url.openConnection()
        // Allow input streams
        connection.doInput = true
        // Connect to the URL
        connection.connect()
        // Get the input stream from the connection
        val input = connection.getInputStream()
        // Convert the input stream into a bitmap using BitmapFactory
        BitmapFactory.decodeStream(input)
    } catch (e: Exception) {
        // Exception handling
        e.printStackTrace()
        // Return null if an error occurs
        null
    }
}
