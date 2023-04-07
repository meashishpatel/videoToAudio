package com.example.videotoaudio

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.arthenica.mobileffmpeg.FFmpeg
import java.io.File

class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSIONS = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Check for required permissions
        checkPermissions()

        val extractAudioButton = findViewById<Button>(R.id.extract_audio_button)
        extractAudioButton.setOnClickListener { extractAudioFromVideo() }
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
            // Permission not granted, request it
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            REQUEST_PERMISSIONS -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted
                } else {
                    // Permission denied
                    Toast.makeText(this, "Permission denied, audio extraction not possible", Toast.LENGTH_SHORT).show()
                }
                return
            }
        }
    }

    private fun extractAudioFromVideo() {
        // Get the input video file path
        val inputFilePath = "/sdcard/Download/myvideo.mp4"

        // Create the output audio file path
        val outputDir = File(Environment.getExternalStorageDirectory().absolutePath + "/ExtractedAudio")
        outputDir.mkdirs()
        val outputFile = File(outputDir, "myaudio.mp3")

        // Set the FFmpeg command to extract audio
        val cmd = arrayOf("-i", inputFilePath, "-vn", "-acodec", "copy", "-f", "mp3", outputFile.absolutePath)

        // Run FFmpeg command
        val rc = FFmpeg.execute(cmd)

        if (rc == 0) {
            // Audio extraction successful
            Toast.makeText(this, "Audio extraction successful", Toast.LENGTH_SHORT).show()
            } else if (rc == -1) {
            // Audio extraction cancelled
            Toast.makeText(this, "Audio extraction cancelled", Toast.LENGTH_SHORT).show()
        } else {
            // Audio extraction failed
            Toast.makeText(this, "Audio extraction failed", Toast.LENGTH_SHORT).show()
        }
    }
}
