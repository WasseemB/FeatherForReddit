package com.wasseemb.FeatherForReddit.Adapter

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.webkit.URLUtil
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.hannesdorfmann.adapterdelegates3.AdapterDelegate
import com.wasseemb.FeatherForReddit.Api.RedditChildrenResponse
import com.wasseemb.FeatherForReddit.R
import com.wasseemb.FeatherForReddit.R.layout
import com.wasseemb.FeatherForReddit.RoundedImageView
import com.wasseemb.FeatherForReddit.extensions.UrlType.GIF
import com.wasseemb.FeatherForReddit.extensions.UrlType.IMAGE
import com.wasseemb.FeatherForReddit.extensions.UrlType.LINK
import com.wasseemb.FeatherForReddit.extensions.inflate
import com.wasseemb.FeatherForReddit.extensions.loadImg
import com.wasseemb.FeatherForReddit.extensions.numToK
import com.wasseemb.FeatherForReddit.extensions.timeFromNow
import com.wasseemb.FeatherForReddit.extensions.urlType
import com.wasseemb.FeatherForReddit.model.DisplayableItem
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.redditview_row.view.author
import kotlinx.android.synthetic.main.redditview_row.view.commentCount
import kotlinx.android.synthetic.main.redditview_row.view.created_utc
import kotlinx.android.synthetic.main.redditview_row.view.domain
import kotlinx.android.synthetic.main.redditview_row.view.image_type
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
  private val clickSubject = PublishSubject.create<DisplayableItem>()
  private val imageSubject = PublishSubject.create<DisplayableItem>()

  private lateinit var items: List<DisplayableItem>


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
    this.items = items
    //Cast to RedditChildrenResponse from DisplayableItem
    val redditItem = (items[position] as RedditChildrenResponse).data

    // Should start visible always (RecyclerView using some RecyclerItem)
    vh.relativelayout.visibility = View.VISIBLE
    vh.title.text = redditItem.title
    vh.author.text = redditItem.author
    vh.subreddit.text = "/r/" + redditItem.subreddit
    vh.domain.text = redditItem.domain
    //handleImage(redditItem).let { vh.imageView.loadImg(it) }

    if (URLUtil.isValidUrl(redditItem.thumbnail)) {
      vh.imageView.loadImg(redditItem.thumbnail)
      Log.d("Valid","valid")

    } else {
      vh.relativelayout.visibility = View.GONE
      Log.d("Valid","Not valid")
    }
    vh.commentCount.text = String.format(
        activity.resources.getString(R.string.comment_count_message),
        numToK(redditItem.num_comments))
    vh.score.text = String.format(activity.resources.getString(R.string.score_count_message),
        numToK(redditItem.score))
    val postDate = Date((redditItem.created_utc) * 1000)
    vh.created_utc.text = postDate.timeFromNow()
    when (urlType(redditItem.url)) {
      IMAGE -> vh.imageType.setImageDrawable(
          activity.resources.getDrawable(R.drawable.ic_photo_album_black_24dp))
      GIF -> vh.imageType.setImageDrawable(
          activity.resources.getDrawable(R.drawable.ic_gif_black_24dp))
      LINK -> vh.imageType.setImageDrawable(
          activity.resources.getDrawable(R.drawable.ic_link_white_18dp))
      else -> { // Note the block
        vh.imageType.setImageDrawable(activity.resources.getDrawable(R.drawable.ic_link_white_18dp))
      }
    }


  }


  val clickEvent: Observable<DisplayableItem> = clickSubject
  val imageClickEvent: Observable<DisplayableItem> = imageSubject


  inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val title: TextView = itemView.title_text
    val author: TextView = itemView.author
    val domain: TextView = itemView.domain
    val imageView: RoundedImageView = itemView.info_image
    val imageType: ImageView = itemView.image_type
    val commentCount: TextView = itemView.commentCount
    val score: TextView = itemView.score
    val subreddit: TextView = itemView.subreddit
    val created_utc: TextView = itemView.created_utc
    val relativelayout: RelativeLayout = itemView.relativeLayout


    init {
      imageView.setOnClickListener {
        imageSubject.onNext(items[layoutPosition])
      }
      itemView.setOnClickListener {
        clickSubject.onNext(items[layoutPosition])
      }
    }

  }


}