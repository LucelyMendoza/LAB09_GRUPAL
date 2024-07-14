package com.idnp2024a.lab_09

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.idnp2024a.lab_09.R

class MainActivity : AppCompatActivity() {
    private lateinit var audioPlayServiceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Crear intent para el servicio de reproducción de audio
        audioPlayServiceIntent = Intent(applicationContext, AudioPlayService::class.java)

        // Listener para el botón Play
        findViewById<Button>(R.id.btnPlay).setOnClickListener {
            audioPlayServiceIntent.apply {
                putExtra(AudioPlayService.FILENAME, "image004.mp3") // Reemplaza con el nombre real de tu archivo
                putExtra(AudioPlayService.COMMAND, AudioPlayService.PLAY)
            }
            startService(audioPlayServiceIntent)
        }

        // Listener para el botón Pause
        findViewById<Button>(R.id.btnPause).setOnClickListener {
            audioPlayServiceIntent.apply {
                putExtra(AudioPlayService.COMMAND, AudioPlayService.PAUSE)
            }
            startService(audioPlayServiceIntent)
        }

        // Listener para el botón Resume
        findViewById<Button>(R.id.btnResume).setOnClickListener {
            audioPlayServiceIntent.apply {
                putExtra(AudioPlayService.COMMAND, AudioPlayService.RESUME)
            }
            startService(audioPlayServiceIntent)
        }

        // Listener para el botón Stop
        findViewById<Button>(R.id.btnStop).setOnClickListener {
            audioPlayServiceIntent.apply {
                putExtra(AudioPlayService.COMMAND, AudioPlayService.STOP)
            }
            startService(audioPlayServiceIntent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancelar la notificación cuando se destruya la actividad
        NotificationHelper.cancelNotification(this)
    }
}
