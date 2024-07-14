package com.idnp2024a.lab_09
import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.idnp2024a.lab_09.R

class AudioPlayService : Service() {

    private lateinit var mediaPlayer: MediaPlayer
    private var isPaused: Boolean = false
    private var currentFile: String? = null

    companion object {
        const val COMMAND = "COMMAND"
        const val FILENAME = "FILENAME"
        const val PLAY = "PLAY"
        const val PAUSE = "PAUSE"
        const val RESUME = "RESUME"
        const val STOP = "STOP"
    }

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            handleCommand(it)
        }
        return START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return Binder()
    }

    private fun handleCommand(intent: Intent) {
        val command = intent.getStringExtra(COMMAND)
        when (command) {
            PLAY -> {
                val filename = intent.getStringExtra(FILENAME)
                if (filename != currentFile) {
                    currentFile = filename
                    startPlayback(filename)
                    startForegroundService()
                } else {
                    resumePlayback()
                }
            }
            PAUSE -> {
                pausePlayback()
            }
            RESUME -> {
                resumePlayback()
            }
            STOP -> {
                stopPlayback()
                stopForeground(true)
                stopSelf()
            }
        }
    }

    private fun startPlayback(filename: String?) {
        filename?.let {
            try {
                mediaPlayer.apply {
                    reset()
                    setDataSource(assets.openFd(it).fileDescriptor)
                    prepareAsync()
                    setOnPreparedListener {
                        it.start()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun pausePlayback() {
        if (mediaPlayer.isPlaying) {
            mediaPlayer.pause()
            isPaused = true
        }
    }

    private fun resumePlayback() {
        if (isPaused) {
            mediaPlayer.start()
            isPaused = false
        }
    }

    private fun stopPlayback() {
        mediaPlayer.stop()
        mediaPlayer.reset()
        isPaused = false
        currentFile = null
    }

    private fun startForegroundService() {
        // Crear canal de notificación si se está ejecutando en Android Oreo o superior
        NotificationHelper.createNotificationChannel(this)

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val notification: Notification = NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID)
            .setContentTitle("Reproduciendo Audio")
            .setContentText(currentFile)  // Mostrar el nombre del archivo actual
            .setSmallIcon(R.drawable.ic_music_note)
            .setContentIntent(pendingIntent)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .build()

        startForeground(NotificationHelper.NOTIFICATION_ID, notification)
    }
}
