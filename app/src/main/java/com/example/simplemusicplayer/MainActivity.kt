package com.example.simplemusicplayer

import android.media.session.MediaSession
import android.media.session.PlaybackState
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.simplemusicplayer.databinding.ActivityMainBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource

class MainActivity : AppCompatActivity() {
    private lateinit var mediaUri: Uri
    private lateinit var mediaItem: MediaItem
    private lateinit var mediaSource: MediaSource
    private lateinit var userAgent: DefaultDataSource.Factory

    // sets up view binding by lazy
    private val binding by lazy(LazyThreadSafetyMode.PUBLICATION){
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root) //sets the content view to the bounded root of the view
        exoPlayer() //initializes exo player
    }

    private fun exoPlayer(){
        val player =
            ExoPlayer.Builder(this)
                .build() // uses builder instead of factory
                .also {
                    exoPlayer ->
                    binding.epPlayerView.player = exoPlayer //binds view to the instance
                }
        mediaSessionPlayback() //invokes media session playback

        mediaUri = Uri.parse(UriAssetsConstants.fireside) //via file

        userAgent = DefaultDataSource.Factory(baseContext) //user agent

        mediaItem = MediaItem.fromUri(mediaUri) //media item from static uri

        mediaSource = ProgressiveMediaSource.Factory(userAgent)
            .createMediaSource(mediaItem) //creates media source
        player.setMediaSource(mediaSource) //sets media source
        player.prepare() //prepares media source
    }

    private fun mediaSessionPlayback(){
        val mediaSession = MediaSession(baseContext, "MainActivity")

        val playbackStateBuilder = PlaybackState.Builder()

        playbackStateBuilder.setActions(PlaybackState.ACTION_PLAY)
        playbackStateBuilder.setActions(PlaybackState.ACTION_PAUSE)
        playbackStateBuilder.setActions(PlaybackState.ACTION_FAST_FORWARD)

        mediaSession.setPlaybackState(playbackStateBuilder.build())
        mediaSession.isActive = true
    }
}