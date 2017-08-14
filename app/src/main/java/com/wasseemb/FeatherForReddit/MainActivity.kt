package com.wasseemb.FeatherForReddit

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.wasseemb.FeatherForReddit.Adapter.UserAdapter
import com.wasseemb.FeatherForReddit.Api.RedditAfterResponse
import com.wasseemb.FeatherForReddit.Api.RestApi
import com.wasseemb.FeatherForReddit.model.DisplayableItem
import com.wasseemb.FeatherForReddit.model.Loading
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.fab
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.fragment_main.recyclerview


class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)

    recyclerview.setHasFixedSize(true)
    val layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    recyclerview.layoutManager = layoutManager

    var data = ArrayList<DisplayableItem>()

    val restApi = RestApi()


    // it. is used to replace retrievednews-> which is the passed parameter
    var redditAfter = RedditAfterResponse("", "")
    restApi.getNews("10", "")
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe {
          data.addAll(it.data.children)
          data.add(Loading())
          recyclerview.adapter = UserAdapter(this, data)
          redditAfter = RedditAfterResponse(it.data.after, it.data.before)
        }



    recyclerview.addOnScrollListener(
        InfiniteScrollListener({
          restApi.getNews(limit = "10", after = redditAfter.after)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe {
                val last = data.size
                data.removeAt(last-1)
                data.addAll(it.data.children)
                recyclerview.adapter.notifyItemInserted(last)
                redditAfter = RedditAfterResponse(it.data.after, it.data.before)
                data.add(Loading())

              }
        }

            , layoutManager)
    )



    fab.setOnClickListener { view ->
      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
          .setAction("Action", null).show()
      recyclerview.clearOnScrollListeners()
      data.clear()
      restApi.openNewSub("android")
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe({
            data.addAll(it.data.children)
            recyclerview.adapter = UserAdapter(this, data)
            redditAfter = RedditAfterResponse(it.data.after, it.data.before)
          }
          )
      recyclerview.addOnScrollListener(
          InfiniteScrollListener({
            restApi.openNewSub(subreddit = "android", after = redditAfter.after!!)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                  val last = data.size
                  data.addAll(it.data.children)
                  recyclerview.adapter.notifyItemInserted(last)
                  redditAfter = RedditAfterResponse(it.data.after, it.data.before)

                }
          }

              , layoutManager)
      )


    }

  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    return when (item.itemId) {
      R.id.action_settings -> true
      else -> super.onOptionsItemSelected(item)
    }
  }
}
