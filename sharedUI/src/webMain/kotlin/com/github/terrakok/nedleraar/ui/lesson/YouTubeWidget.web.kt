package com.github.terrakok.nedleraar.ui.lesson

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.HtmlElementView
import androidx.compose.ui.viewinterop.WebElementView
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.dom.appendElement
import org.w3c.dom.HTMLDivElement
import kotlin.js.ExperimentalWasmJsInterop
import kotlin.js.js

@OptIn(ExperimentalComposeUiApi::class)
@Composable
actual fun YouTubeWidget(
    videoId: String,
    controller: YouTubeController,
    modifier: Modifier
) {
    Box(modifier) {
        HtmlElementView(
            modifier = Modifier.fillMaxSize(),
            factory = {
                (document.createElement("div") as HTMLDivElement).also { div ->
                    div.appendElement("div") {
                        id = "yt_player"
                    }
                    div.appendElement("script") {
                        textContent = jsPlayer(videoId)
                    }
                    window.requestAnimationFrame {
                        setYoutubeProgressListener(controller::onProgress)
                    }
                }
            },
        )
        LaunchedEffect(Unit) {
            launch {
                for (position in controller.seek) {
                    seekTo(position)
                }
            }
        }
        DisposableEffect(Unit) {
            onDispose {
                releaseYoutubePlayer()
            }
        }
    }
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun setYoutubeProgressListener(callback: (Int) -> Unit) {
    js("setYoutubeProgressListener(callback)")
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun seekTo(seconds: Int) {
    js("seekYoutubeTo(seconds)")
}

@OptIn(ExperimentalWasmJsInterop::class)
private fun releaseYoutubePlayer() {
    js("releaseYoutubePlayer()")
}

private fun jsPlayer(videoId: String) = """
    var progressListener;
    function setYoutubeProgressListener(callback) {
      progressListener = callback;
    }
    
    var player;
    
    var progressInterval
    function loopProgress() {
      progressInterval = setInterval(() => {
        if (progressListener) {
          const currentTime = player.getCurrentTime();
          progressListener(currentTime);
        }
      }, 100);
    }
    
    var seekToSeconds = 0;
    function seekYoutubeTo(seconds) {
      seekToSeconds = seconds;
      if (player) player.seekTo(seconds, true);
    }
    
    function configurePlayer() {
      console.log('YouTube player init');
      player = new YT.Player('yt_player', {
        height: '100%',
        width: '100%',
        videoId: '$videoId',
        events: {
          'onReady': onPlayerReady,
          'onStateChange': onPlayerStateChange
        }
      });
    }
    function onPlayerReady(event) {
      console.log('YouTube player start');
      player.seekTo(seekToSeconds, true);
    }
    function onPlayerStateChange(event) {
      switch (event.data) {
        case YT.PlayerState.PLAYING:
          loopProgress();
          break;
          
        default:
          if (progressInterval) clearInterval(progressInterval);
          break;
      }
    }
    
    function releaseYoutubePlayer() {
      console.log('YouTube player destroy');
      if (progressInterval) clearInterval(progressInterval);
      if (progressListener) progressListener = null;
      if (player) player.destroy();
    }
    
    if (window.YT && window.YT.Player) {
      configurePlayer();
    } else {
      if (!document.getElementById('yt-api-script')) {
        var tag = document.createElement('script');
        tag.id = 'yt-api-script';
        tag.src = 'https://www.youtube.com/iframe_api';
        var firstScriptTag = document.getElementsByTagName('script')[0];
        firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);
      }
      
      function onYouTubeIframeAPIReady() {
        configurePlayer();
      }
    }
""".trimIndent()