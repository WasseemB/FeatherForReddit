package com.wasseemb.FeatherForReddit

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log

/**
 * Created by Wasseem on 06/08/2017.
 */
class InfiniteScrollListener(
    val func: () -> Unit,
    val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

  private var previousTotal = 0
  private var loading = true

  override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
    super.onScrolled(recyclerView, dx, dy)

    if (dy <= 0) return
    val visibleItemCount = recyclerView.childCount
    val totalItemCount = layoutManager.itemCount
    val firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

    if (loading) {
      if (totalItemCount > previousTotal) {
        loading = false
        previousTotal = totalItemCount
      }
    }
    if (!loading && totalItemCount - visibleItemCount <= firstVisibleItem) {
      Log.d("TAG","End has been reached")
      // Do something
      func()
      loading = true
    }
  }

}