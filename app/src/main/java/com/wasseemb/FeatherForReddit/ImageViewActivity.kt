package com.wasseemb.FeatherForReddit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.MediaController
import com.wasseemb.FeatherForReddit.R.layout
import com.wasseemb.FeatherForReddit.extensions.loadImg
import kotlinx.android.synthetic.main.activity_imageview.imageViewD
import kotlinx.android.synthetic.main.activity_imageview.videoV
import android.support.customtabs.CustomTabsIntent
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection


/**
 * Created by Wasseem on 31/08/2017.
 */
class ImageViewActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_imageview)
    val url = intent.getStringExtra("image")
    Log.d("TAG", url)



//    if (!url.endsWith("gifv") && url.contains("imgur"))
//      imageViewD.loadImg(url)
//    else if (url.endsWith("gifv")) {
//      val mediaController = MediaController(this)
//      mediaController.setAnchorView(videoV)
//      videoV.setMediaController(mediaController)
//      videoV.setVideoPath(url.replace("gifv", "mp4"))
//      videoV.start()
//      videoV.setOnCompletionListener { videoV.start() }
//    } else {
//      open(url)
//    }

  }
  fun open(url: String) {
    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(R.color.primary)
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
    finish()
  }
}

