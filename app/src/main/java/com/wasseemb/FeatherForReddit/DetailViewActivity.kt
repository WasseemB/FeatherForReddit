package com.wasseemb.FeatherForReddit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import com.wasseemb.FeatherForReddit.Api.RestApi
import com.wasseemb.FeatherForReddit.R.layout
import com.wasseemb.FeatherForReddit.extensions.applySchedulersWithDelay
import com.wasseemb.FeatherForReddit.extensions.loadImg
import com.wasseemb.FeatherForReddit.extensions.numToK
import com.wasseemb.FeatherForReddit.extensions.timeFromNow
import com.wasseemb.FeatherForReddit.model.DisplayableItem
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.reddit_row.card_view
import kotlinx.android.synthetic.main.redditview_row.author
import kotlinx.android.synthetic.main.redditview_row.commentCount
import kotlinx.android.synthetic.main.redditview_row.created_utc
import kotlinx.android.synthetic.main.redditview_row.domain
import kotlinx.android.synthetic.main.redditview_row.info_image
import kotlinx.android.synthetic.main.redditview_row.score
import kotlinx.android.synthetic.main.redditview_row.subreddit
import kotlinx.android.synthetic.main.redditview_row.title_text
import java.util.Date

/**
 * Created by Wasseem on 07/10/2017.
 */
class DetailViewActivity : AppCompatActivity() {
  var data = ArrayList<DisplayableItem>()
  private val restApi = RestApi()
  var after: String? = ""
  private var subscribe: Disposable? = null
  private lateinit var result: Drawer
  private lateinit var headerResult: AccountHeader

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.redditview_row)

    val permalink = intent.getStringExtra("permalink")
    card_view.transitionName ="robot_trans"

    //Log.d("PERM",permalink)


    restApi.getPost(permalink)
        .applySchedulersWithDelay()
        .subscribe({
          val obj = it[0].data.children[0].data
          title_text.text = obj.title
          subreddit.text = obj.subreddit
          domain.text = obj.domain
          info_image.loadImg(obj.thumbnail)
          score.text = obj.score.toString()
          commentCount.text = String.format(
              this.resources.getString(R.string.comment_count_message),
              numToK(obj.num_comments))
          score.text = String.format(this.resources.getString(R.string.score_count_message),
              numToK(obj.score))
          author.text = obj.author
          val postDate = Date((obj.created_utc) * 1000)
          created_utc.text = postDate.timeFromNow()
          //Log.d("PERM",it[0].data.children[0].data.title)
        }, { e -> Log.d("PERM", e.toString()) }, { Log.d("PERM", "Completed") })


  }
}