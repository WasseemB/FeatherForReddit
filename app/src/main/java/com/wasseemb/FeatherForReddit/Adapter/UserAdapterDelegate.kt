package com.wasseemb.FeatherForReddit.Adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.wasseemb.FeatherForReddit.Api.RedditChildrenResponse
import com.wasseemb.FeatherForReddit.R
import com.wasseemb.FeatherForReddit.R.layout
import com.wasseemb.FeatherForReddit.RoundedImageView
import com.wasseemb.FeatherForReddit.extensions.inflate
import com.wasseemb.FeatherForReddit.extensions.loadImg
import com.wasseemb.FeatherForReddit.extensions.roundTwoDigits
import com.wasseemb.FeatherForReddit.extensions.timeFromNow
import com.wasseemb.FeatherForReddit.model.DisplayableItem
import kotlinx.android.synthetic.main.redditview_row.view.author
import kotlinx.android.synthetic.main.redditview_row.view.commentCount
import kotlinx.android.synthetic.main.redditview_row.view.created_utc
import kotlinx.android.synthetic.main.redditview_row.view.domain
import kotlinx.android.synthetic.main.redditview_row.view.info_image
import kotlinx.android.synthetic.main.redditview_row.view.relativeLayout
import kotlinx.android.synthetic.main.redditview_row.view.score
import kotlinx.android.synthetic.main.redditview_row.view.subreddit
import kotlinx.android.synthetic.main.redditview_row.view.title_text
import java.util.Date


/**
 * Created by Wasseem on 31/07/2017.
 */
class UserAdapterDelegate(val activity: Activity) : AdapterDelegate<List<DisplayableItem>>() {
  //var inflater: LayoutInflater = activity.layoutInflater


  override fun onCreateViewHolder(parent: ViewGroup?): ViewHolder {
    return UserViewHolder(parent?.inflate(layout.redditview_row)!!)
  }

  override fun isForViewType(items: List<DisplayableItem>, position: Int): Boolean {
    return items[position] is RedditChildrenResponse

  }

  override fun onBindViewHolder(items: List<DisplayableItem>, position: Int,
      holder: ViewHolder,
      payloads: MutableList<Any>) {
    val vh = holder as UserViewHolder
    //Cast to RedditChildrenResponse from DisplayableItem
    val redditItem = (items[position] as RedditChildrenResponse).data

    // Should start visible always (RecyclerView using some RecyclerItem
    vh.relativelayout.visibility = View.VISIBLE

    vh.title.text = redditItem.title
    vh.author.text = redditItem.author
    vh.subreddit.text = "/r/" + redditItem.subreddit
    vh.domain.text = redditItem.domain
    if (redditItem.preview != null) {
      if (!redditItem.url.contains("i.redd.it") && redditItem.url.contains(".gif")) {
        val url = redditItem.url.replace(".gif", "h.gif")
        Log.d("TAG", url)
        vh.imageView.loadImg(url)
      }
      else if(redditItem.url.contains("gfycat")) {
        val url = redditItem.url.replace("gfycat", "thumbs.gfycat").plus("-poster.jpg")
        vh.imageView.loadImg(url)

      }

      else vh.imageView.loadImg(redditItem.preview.images[0].source.url)
    } else
      vh.relativelayout.visibility = View.GONE
    vh.commentCount.text =  String.format(activity.resources.getString(R.string.comment_count_message), numToK(redditItem.num_comments))
    vh.score.text = String.format(activity.resources.getString(R.string.score_count_message), numToK(redditItem.score))
    val postDate = Date((redditItem.created_utc) * 1000)
    vh.created_utc.text = postDate.timeFromNow()
  }

  fun numToK(number: Double): String {
    if (number > 1000) return (number / 1000).roundTwoDigits() + "k"
    else return number.toInt().toString()

  }

  internal class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val title: TextView = itemView.title_text
    val author: TextView = itemView.author
    val domain: TextView = itemView.domain
    val imageView: RoundedImageView = itemView.info_image
    val commentCount: TextView = itemView.commentCount
    val score: TextView = itemView.score
    val subreddit: TextView = itemView.subreddit
    val created_utc: TextView = itemView.created_utc
    val relativelayout: RelativeLayout = itemView.relativeLayout

  }


}