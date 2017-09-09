package com.wasseemb.FeatherForReddit

import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.wasseemb.FeatherForReddit.R.layout
import com.wasseemb.FeatherForReddit.extensions.loadImg
import kotlinx.android.synthetic.main.activity_imageview.imageViewD
import kotlinx.android.synthetic.main.activity_imageview.videoV
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.MediaController


/**
 * Created by Wasseem on 31/08/2017.
 */
class ImageViewActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_imageview)
    val url = intent.getStringExtra("image")
    if (!url.endsWith("gifv"))
      imageViewD.loadImg(url)
    else
    {
      val mediaController = MediaController(this)
      mediaController.setAnchorView(videoV)
      videoV.setMediaController(mediaController)
      videoV.setVideoPath(url.replace("gifv","mp4"))
      videoV.start()
      videoV.setOnCompletionListener { videoV.start() }
    }
  }
}