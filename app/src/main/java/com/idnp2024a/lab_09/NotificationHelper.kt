package com.idnp2024a.lab_09

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.idnp2024a.lab_09.R

object NotificationHelper {
    const val CHANNEL_ID = "AudioPlayServiceChannel"
    const val NOTIFICATION_ID = 1

    // Crear canal de notificación si se está ejecutando en Android Oreo o superior
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Audio Play Service Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = context.getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(serviceChannel)
        }
    }

    // Crear la notificación para la reproducción de audio
    // Crear la notificación para la reproducción de audio
    fun createNotification(context: Context, filename: String): NotificationCompat.Builder {
        val notificationIntent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE  // Usar FLAG_IMMUTABLE aquí también
        )

        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle("Playing Audio")
            .setContentText("Playing: $filename")
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true) // Mantener la notificación persistente
            .addAction(createPauseAction(context)) // Añadir acción de pausa
    }


    // Crear acción de pausa para la notificación
    private fun createPauseAction(context: Context): NotificationCompat.Action {
        val pauseIntent = Intent(context, AudioPlayService::class.java).apply {
            action = AudioPlayService.STOP
        }
        val pendingPauseIntent = PendingIntent.getService(
            context, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Action.Builder(
            android.R.drawable.ic_media_pause,
            "Pause",
            pendingPauseIntent
        ).build()
    }

    // Eliminar la notificación cuando se detenga la reproducción
    fun cancelNotification(context: Context) {
        val manager = context.getSystemService(NotificationManager::class.java)
        manager?.cancel(NOTIFICATION_ID)
    }
}
