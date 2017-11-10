package com.wasseemb.FeatherForReddit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.bluelinelabs.conductor.Conductor
import com.bluelinelabs.conductor.Router
import com.bluelinelabs.conductor.RouterTransaction
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import com.wasseemb.FeatherForReddit.Adapter.UserAdapter
import com.wasseemb.FeatherForReddit.Api.RestApi
import com.wasseemb.FeatherForReddit.R.layout
import com.wasseemb.FeatherForReddit.extensions.applySchedulersWithDelay
import com.wasseemb.FeatherForReddit.model.DisplayableItem
import com.wasseemb.FeatherForReddit.model.Loading
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_main.controller_container
import kotlinx.android.synthetic.main.activity_main.toolbar
import kotlinx.android.synthetic.main.fragment_main.recyclerview

/**
 * Created by Wasseem on 05/10/2017.
 */
class SubredditActivity :AppCompatActivity() {

  private lateinit var router: Router
  var data = ArrayList<DisplayableItem>()
  private val restApi = RestApi()
  var after: String? = ""
  private var subscribe: Disposable? = null
  private lateinit var result: Drawer
  private lateinit var headerResult: AccountHeader

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_subreddit)

    val subreddit = intent.getStringExtra("subreddit")
    supportActionBar?.title = subreddit.capitalize()

    router = Conductor.attachRouter(this, controller_container, savedInstanceState)
    if (!router.hasRootController()) {
      router.setRoot(RouterTransaction.with(MainActivityFragment()))
    }

    recyclerview.setHasFixedSize(true)
    recyclerview.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    recyclerview.adapter = UserAdapter(this, data)

    restApi.openNewSub(subreddit)
                        .applySchedulersWithDelay()
                        .subscribe {
                          data.clear()
                          data.addAll(it.data.children)
                          after = it.data.after
                          recyclerview.adapter = UserAdapter(this, data)

                        }

    recyclerview.addOnScrollListener(
        InfiniteScrollListener({
          restApi.openNewSub(subreddit,after!!)
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
  }

}