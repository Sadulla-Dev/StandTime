package com.example.standtime.standtime

import android.content.ComponentName
import android.content.Context
import android.media.session.MediaController
import android.media.session.MediaSessionManager
import android.media.session.PlaybackState
import android.service.notification.NotificationListenerService
import androidx.core.app.NotificationManagerCompat

class StandTimeMediaService : NotificationListenerService() {

    private val sessionManager by lazy {
        getSystemService(MediaSessionManager::class.java)
    }

    private val sessionListener = MediaSessionManager.OnActiveSessionsChangedListener { controllers ->
        updateActiveController(controllers.orEmpty())
    }

    override fun onListenerConnected() {
        super.onListenerConnected()
        listenerComponent = ComponentName(this, StandTimeMediaService::class.java)
        sessionManager?.addOnActiveSessionsChangedListener(sessionListener, listenerComponent)
        updateActiveController(sessionManager?.getActiveSessions(listenerComponent).orEmpty())
    }

    override fun onListenerDisconnected() {
        super.onListenerDisconnected()
        sessionManager?.removeOnActiveSessionsChangedListener(sessionListener)
        activeController = null
    }

    private fun updateActiveController(controllers: List<MediaController>) {
        activeController = controllers.firstOrNull {
            it.playbackState?.state == PlaybackState.STATE_PLAYING
        } ?: controllers.firstOrNull()
    }

    companion object {
        @Volatile
        private var activeController: MediaController? = null

        @Volatile
        private var listenerComponent: ComponentName? = null

        fun snapshot(context: Context): MediaSnapshot {
            val permissionGranted = NotificationManagerCompat.getEnabledListenerPackages(context)
                .contains(context.packageName)
            val controller = activeController
            val metadata = controller?.metadata
            val title = metadata?.getString("android.media.metadata.TITLE").orEmpty()
            val subtitle = metadata?.getString("android.media.metadata.ARTIST")
                ?: metadata?.getString("android.media.metadata.ALBUM_ARTIST")
                ?: metadata?.getString("android.media.metadata.ALBUM")
                .orEmpty()
            val state = controller?.playbackState?.state ?: PlaybackState.STATE_NONE

            return MediaSnapshot(
                permissionGranted = permissionGranted,
                sessionAvailable = permissionGranted && controller != null,
                appName = controller?.packageName.orEmpty(),
                title = title,
                subtitle = subtitle,
                isPlaying = state == PlaybackState.STATE_PLAYING
            )
        }

        fun togglePlayback() {
            val controller = activeController ?: return
            val state = controller.playbackState?.state ?: PlaybackState.STATE_NONE
            if (state == PlaybackState.STATE_PLAYING) {
                controller.transportControls.pause()
            } else {
                controller.transportControls.play()
            }
        }

        fun skipToNext() {
            activeController?.transportControls?.skipToNext()
        }
    }
}

data class MediaSnapshot(
    val permissionGranted: Boolean,
    val sessionAvailable: Boolean,
    val appName: String,
    val title: String,
    val subtitle: String,
    val isPlaying: Boolean
)
