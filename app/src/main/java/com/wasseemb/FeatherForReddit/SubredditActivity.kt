package com.wasseemb.FeatherForReddit

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.lapism.searchview.SearchView
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import com.wasseemb.FeatherForReddit.Adapter.UserAdapter
import com.wasseemb.FeatherForReddit.Api.RestApi
import com.wasseemb.FeatherForReddit.R.layout
import com.wasseemb.FeatherForReddit.extensions.applySchedulersWithDelay
import com.wasseemb.FeatherForReddit.model.DisplayableItem
import com.wasseemb.FeatherForReddit.model.Loading
import kotlinx.android.synthetic.main.fragment_main.recyclerview

/**
 * Created by Wasseem on 05/10/2017.
 */
class SubredditActivity : AppCompatActivity() {

  var data = ArrayList<DisplayableItem>()
  private val restApi = RestApi()
  var after: String? = ""
  private lateinit var result: Drawer
  private lateinit var headerResult: AccountHeader

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_subreddit)

    val subreddit = intent.getStringExtra("subreddit")
    //supportActionBar?.title = subreddit.capitalize()

    val searchView = findViewById<SearchView>(R.id.searchView)

    if (searchView != null) {
      searchView.versionMargins = SearchView.VersionMargins.TOOLBAR_SMALL
      searchView.hint = subreddit
      searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
          val intent = Intent(applicationContext, SubredditActivity::class.java)
          intent.putExtra("subreddit", query)
          startActivity(intent)
          searchView.close(false)
          return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
          return false
        }
      })
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
          //recyclerview.adapter = UserAdapter(this, data)
          recyclerview.adapter.notifyDataSetChanged()

        }

    recyclerview.addOnScrollListener(
        InfiniteScrollListener({
          restApi.openNewSub(subreddit, after!!)
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
    com.wasseemb.FeatherForReddit.extensions.setupItemClick(recyclerview.adapter as UserAdapter,
        applicationContext)
    com.wasseemb.FeatherForReddit.extensions.setupImageClick(recyclerview.adapter as UserAdapter,
        applicationContext)

  }


}