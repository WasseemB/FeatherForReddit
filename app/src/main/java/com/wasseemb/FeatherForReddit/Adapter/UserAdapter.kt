package com.wasseemb.FeatherForReddit.Adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.ViewGroup
import com.hannesdorfmann.adapterdelegates3.AdapterDelegatesManager
import com.wasseemb.FeatherForReddit.model.DisplayableItem
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject


/**
 * Created by Wasseem on 31/07/2017.
 */
class UserAdapter(activity: Activity, items: List<DisplayableItem>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
  val delegatesManager = AdapterDelegatesManager<List<DisplayableItem>>()
  val items = items
  val adapterDelegate = UserAdapterDelegate(activity)

  init {
    delegatesManager.addDelegate(
        adapterDelegate)
    delegatesManager.addDelegate(
        LoadingDelegateAdapter())
  }

  override fun getItemViewType(position: Int): Int {
    return delegatesManager.getItemViewType(items, position)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return delegatesManager.onCreateViewHolder(parent, viewType)
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    delegatesManager.onBindViewHolder(items, position, holder)
  }

  override fun onBindViewHolder(holder: ViewHolder?, position: Int, payloads: MutableList<Any>?) {
    super.onBindViewHolder(holder, position, payloads)
  }

  override fun getItemCount(): Int {
    return items.size
  }


}