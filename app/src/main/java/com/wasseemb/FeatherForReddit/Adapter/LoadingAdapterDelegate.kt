package com.wasseemb.FeatherForReddit.Adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.wasseemb.FeatherForReddit.R
import com.wasseemb.FeatherForReddit.extensions.inflate
import com.wasseemb.FeatherForReddit.model.DisplayableItem
import com.wasseemb.FeatherForReddit.model.Loading

/**
 * Created by Wasseem on 12/08/2017.
 */
class LoadingDelegateAdapter : AdapterDelegate<List<DisplayableItem>>() {
  override fun onCreateViewHolder(parent: ViewGroup): ViewHolder = TurnsViewHolder(parent)

  override fun isForViewType(items: List<DisplayableItem>, position: Int): Boolean {
    return items[position] is Loading

  }


  override fun onBindViewHolder(items: List<DisplayableItem>, position: Int,
      holder: ViewHolder, payloads: MutableList<Any>) {
  }
  internal class TurnsViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
      parent.inflate(R.layout.news_item_loading))

}