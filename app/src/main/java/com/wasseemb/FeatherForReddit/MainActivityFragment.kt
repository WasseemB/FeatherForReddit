package com.wasseemb.FeatherForReddit

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller

/**
 * A placeholder fragment containing a simple view.
 */
class MainActivityFragment : Controller() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup): View {
    return inflater.inflate(R.layout.fragment_main, container, false)

  }

}
