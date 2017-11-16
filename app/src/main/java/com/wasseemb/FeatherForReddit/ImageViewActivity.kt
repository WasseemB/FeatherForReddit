package com.wasseemb.FeatherForReddit

import android.net.Uri
import android.os.Bundle
import android.support.customtabs.CustomTabsIntent
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.MediaController
import com.wasseemb.FeatherForReddit.R.layout
import com.wasseemb.FeatherForReddit.extensions.UrlType.GIF
import com.wasseemb.FeatherForReddit.extensions.UrlType.IMAGE
import com.wasseemb.FeatherForReddit.extensions.UrlType.LINK
import com.wasseemb.FeatherForReddit.extensions.loadImg
import com.wasseemb.FeatherForReddit.extensions.urlType
import kotlinx.android.synthetic.main.activity_imageview.imageViewD
import kotlinx.android.synthetic.main.activity_imageview.videoV


/**
 * Created by Wasseem on 31/08/2017.
 */
class ImageViewActivity : AppCompatActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_imageview)
    val url = intent.getStringExtra("image")
    Log.d("TAG", url)
    // supportPostponeEnterTransition()

    //open(url)


    when (urlType(url)) {
      IMAGE -> {
        videoV.visibility = View.INVISIBLE
        imageViewD.visibility = View.VISIBLE
        imageViewD.loadImg(url)
      }
      GIF -> {
        videoV.visibility = View.VISIBLE
        imageViewD.visibility = View.INVISIBLE
        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoV)
        videoV.setMediaController(mediaController)
        videoV.setVideoPath(url.replace("gifv", "mp4"))
        videoV.start()
        videoV.setOnCompletionListener { videoV.start() }

      }
      LINK -> open(url)
    }
  }

  fun open(url: String) {
    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(R.color.primary)
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
    finish()
  }
}

