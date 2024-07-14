package com.idnp2024a.lab_09

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

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
                putExtra(AudioPlayService.FILENAME, "image004.mp3")
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

    override fun onStop() {
        super.onStop()
        // Iniciar el servicio en primer plano cuando la aplicación se vaya al fondo
        audioPlayServiceIntent.putExtra(AudioPlayService.COMMAND, AudioPlayService.START_FOREGROUND)
        startService(audioPlayServiceIntent)
    }

    override fun onStart() {
        super.onStart()
        // Detener el servicio en primer plano cuando la aplicación vuelva a estar en primer plano
        audioPlayServiceIntent.putExtra(AudioPlayService.COMMAND, AudioPlayService.STOP_FOREGROUND)
        startService(audioPlayServiceIntent)
    }
}
