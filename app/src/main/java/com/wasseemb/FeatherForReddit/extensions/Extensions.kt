@file:JvmName("ExtensionsUtils")

package com.wasseemb.FeatherForReddit.extensions

import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.squareup.picasso.Picasso
import com.wasseemb.FeatherForReddit.R
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Date
import java.util.concurrent.TimeUnit

/**
 * Created by Wasseem on 03/08/2017.
 */
fun ViewGroup.inflate(layoutId: Int, attachToRoot: Boolean = false): View {
  return LayoutInflater.from(context).inflate(layoutId, this, attachToRoot)
}

fun Date.timeFromNow(): String {
  val date = Date()
  val duration = date.time - this.time
  val diffInSeconds = TimeUnit.MILLISECONDS.toSeconds(duration)
  val diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration)
  val diffInHours = TimeUnit.MILLISECONDS.toHours(duration)
  val diffInDays = TimeUnit.MILLISECONDS.toDays(duration)
  if (diffInSeconds < 60) return diffInSeconds.toString() + "s"
  if (diffInMinutes < 60) return diffInMinutes.toString() + "m"
  if (diffInHours < 24) return diffInHours.toString() + "h"
  if (diffInDays < 31) return diffInHours.toString() + "d"
  return "Out of range"
}

fun Double.roundTwoDigits(): String {
  val df = DecimalFormat("#.##")
  df.roundingMode = RoundingMode.CEILING
  return df.format(this)
}


fun ImageView.loadImg(imageUrl: String) {
  if (TextUtils.isEmpty(imageUrl)) {
    Picasso.with(context).load(R.mipmap.ic_launcher).into(this)
  } else {
    Picasso.with(context).load(imageUrl).into(this)
  }
}