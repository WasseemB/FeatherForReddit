package com.wasseemb.FeatherForReddit

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.wasseemb.FeatherForReddit.Adapter.UserAdapter
import com.wasseemb.FeatherForReddit.Api.RedditChildrenResponse
import com.wasseemb.FeatherForReddit.Api.RestApi
import com.wasseemb.FeatherForReddit.R.layout
import com.wasseemb.FeatherForReddit.extensions.applySchedulersWithDelay
import com.wasseemb.FeatherForReddit.model.DisplayableItem
import com.wasseemb.FeatherForReddit.model.Loading
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.controller_container
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.fragment_main.recyclerview


class MainActivity : AppCompatActivity() {

  private lateinit var router: Router
  var data = ArrayList<DisplayableItem>()
  private val restApi = RestApi()
  var after: String? = ""
  private var subscribe: Disposable? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)
    setSupportActionBar(toolbar)

    router = Conductor.attachRouter(this, controller_container, savedInstanceState)
    if (!router.hasRootController()) {
      router.setRoot(RouterTransaction.with(MainActivityFragment()))
    }

    recyclerview.setHasFixedSize(true)
    recyclerview.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    recyclerview.adapter = UserAdapter(this, data)


    restApi.getNews()
        .applySchedulersWithDelay()
        .subscribe({
          data.addAll(it.data.children)
          data.add(Loading())
          recyclerview.adapter.notifyDataSetChanged()
          after = it.data.after
        }, { e -> Log.d("Tag", e.toString()) }, { Log.d("Tag", "Completed") })


    recyclerview.addOnScrollListener(
        InfiniteScrollListener({
          restApi.getNews(after = after)
              .applySchedulersWithDelay()
              .subscribe {
                val last = data.size
                data.removeAt(last - 1)
                data.addAll(it.data.children)
                recyclerview.adapter.notifyItemInserted(last)
                after = it.data.after
                data.add(Loading())
              }
        }, recyclerview.layoutManager as LinearLayoutManager)
    )
    setupItemClick()
//
//
//
//    fab.setOnClickListener { view ->
//      Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//          .setAction("Action", null).show()
//      recyclerview.clearOnScrollListeners()
//      data.clear()
//      restApi.openNewSub("android")
//          .subscribeOn(Schedulers.io())
//          .observeOn(AndroidSchedulers.mainThread())
//          .subscribe({
//            data.addAll(it.data.children)
//            recyclerview.adapter = UserAdapter(this, data)
//            redditAfter = RedditAfterResponse(it.data.after, it.data.before)
//          }
//          )
//      recyclerview.addOnScrollListener(
//          InfiniteScrollListener({
//            restApi.openNewSub(subreddit = "android", after = redditAfter.after!!)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe {
//                  val last = data.size
//                  data.addAll(it.data.children)
//                  recyclerview.adapter.notifyItemInserted(last)
//                  redditAfter = RedditAfterResponse(it.data.after, it.data.before)
//
//                }
//          }
//
//              , layoutManager)
//      )
//
//
//    }


  }

  override fun onBackPressed() {
    if (!router.handleBack()) {
      super.onBackPressed()
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

  private fun setupItemClick() {
    subscribe = (recyclerview.adapter as UserAdapter).adapterDelegate.clickEvent
        .subscribe({
          val response = it as RedditChildrenResponse
          val intent = Intent(applicationContext, ImageViewActivity::class.java)
          intent.putExtra("image", response.data.url)
          startActivity(intent)
          //Toast.makeText(this, "Clicked on ${response.data.title}", Toast.LENGTH_LONG).show()
        })
  }
}

