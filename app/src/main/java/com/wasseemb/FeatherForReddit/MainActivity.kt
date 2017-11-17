package com.wasseemb.FeatherForReddit

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import co.zsmb.materialdrawerkt.R.attr.divider
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import com.lapism.searchview.SearchView
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import com.wasseemb.FeatherForReddit.Adapter.UserAdapter
import com.wasseemb.FeatherForReddit.Api.RedditNewsResponse
import com.wasseemb.FeatherForReddit.Api.RestApi
import com.wasseemb.FeatherForReddit.R.layout
import com.wasseemb.FeatherForReddit.extensions.PreferenceHelper.defaultPrefs
import com.wasseemb.FeatherForReddit.extensions.PreferenceHelper.get
import com.wasseemb.FeatherForReddit.extensions.PreferenceHelper.set
import com.wasseemb.FeatherForReddit.extensions.applySchedulersWithDelay
import com.wasseemb.FeatherForReddit.extensions.setupImageClick
import com.wasseemb.FeatherForReddit.extensions.setupItemClick
import com.wasseemb.FeatherForReddit.model.DisplayableItem
import com.wasseemb.FeatherForReddit.model.Loading
import kotlinx.android.synthetic.main.activity_main.searchView
import kotlinx.android.synthetic.main.fragment_main.recyclerview


class MainActivity : AppCompatActivity() {

  var data = ArrayList<DisplayableItem>()
  private val restApi = RestApi()
  var after: String? = ""
  private lateinit var result: Drawer
  private lateinit var headerResult: AccountHeader

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(layout.activity_main)
    //setSupportActionBar(toolbar)

    setUpSearchView()
    recyclerview.setHasFixedSize(true)
    recyclerview.setItemViewCacheSize(10)
    recyclerview.isDrawingCacheEnabled = true
    recyclerview.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
    recyclerview.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    recyclerview.adapter = UserAdapter(this, data)
    result = drawer {
      primaryItem("Home") {

        icon = R.mipmap.ic_launcher
        onClick { _ ->
          frontPage()
          searchView.hint = "Feather for Reddit"
          false
        }
      }
      divider
      primaryItem("Settings") {
        icon = android.R.drawable.ic_menu_preferences
        secondaryItem("Switch Cards") {
          onClick { _ ->
            val prefs = defaultPrefs(this@MainActivity)
            val value: String? = prefs["card", "mini"] //getter
            if (value.equals("mini"))
              prefs["card"] = "large"
            else
              prefs["card"] = "mini"
            recyclerview.layoutManager = LinearLayoutManager(this@MainActivity,
                LinearLayout.VERTICAL, false)
            recyclerview.adapter = UserAdapter(this@MainActivity, data)
            recyclerview.adapter.notifyDataSetChanged()
            setupItemClick(recyclerview.adapter as UserAdapter,
                applicationContext)
            setupImageClick(recyclerview.adapter as UserAdapter,
                applicationContext)
            false
          }
        }
      }
    }
    frontPage()


//    result = drawer {
//      toolbar = this@MainActivity.toolbar
//      hasStableIds = true
//      savedInstance = savedInstanceState
//      primaryItem("r/ enter a subreddit") {
//        // Called only when this item is clicked
//        onClick { _ ->
//          MaterialDialog.Builder(this@MainActivity)
//              .title("Enter a Subreddit")
//              .inputType(InputType.TYPE_CLASS_TEXT)
//              .input("", "",
//                  { dialog, input ->
//                    val intent = Intent(applicationContext, SubredditActivity::class.java)
//                    intent.putExtra("subreddit", input.toString())
//                    startActivity(intent)
////                    restApi.openNewSub(input.toString())
////                        .applySchedulersWithDelay()
////                        .subscribe {
////                          data.clear()
////                          data.addAll(it.data.children)
////                          recyclerview.adapter = UserAdapter(this@MainActivity, data)
////                          setupItemClick()
////
////                        }
//                    // Do something
//                  })
//              .show()
//          // Log.d("DRAWER", "Click.")
//          false
//        }
//      }
//
//    }
    setupItemClick(recyclerview.adapter as UserAdapter,
        applicationContext)
    setupImageClick(recyclerview.adapter as UserAdapter,
        applicationContext)


  }


  private fun setUpSearchView() {
    if (searchView != null) {
      searchView.versionMargins = SearchView.VersionMargins.TOOLBAR_SMALL
      searchView.hint = "Feather for Reddit"
      searchView.shouldClearOnClose = true
      searchView.setVoiceIcon(R.drawable.abc_ic_menu_overflow_material)

      searchView.setOnVoiceIconClickListener {
        showPopup(searchView.rootView.findViewById(R.id.search_imageView_mic))
      }
      searchView.setOnNavigationIconClickListener {
        result.openDrawer()

      }
      searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
//          val intent = Intent(applicationContext, SubredditActivity::class.java)
//          intent.putExtra("subreddit", query)
//          startActivity(intent)
          recyclerview.clearOnScrollListeners()
          openSub(query, "hot")
          searchView.close(false)
          return true
        }

        override fun onQueryTextChange(newText: String): Boolean {
          return false
        }
      })
    }
  }

  private fun frontPage(mode: String = "hot") {
    recyclerview.clearOnScrollListeners()
    restApi.getNews(mode = mode)
        .applySchedulersWithDelay()
        .subscribe({
          dataHandle(data, it)
        }, { e -> Log.d("Tag", e.toString()) }, { Log.d("Tag", "Completed") })


    recyclerview.addOnScrollListener(
        InfiniteScrollListener({
          restApi.getNews(after = after)
              .applySchedulersWithDelay()
              .subscribe {
                dataHandleInfiniteScrollListener(data, it)
              }
        }, recyclerview.layoutManager as LinearLayoutManager)
    )
  }

  fun openSub(query: String, mode: String) {

    restApi.openNewSub(subreddit = query, mode = mode)
        .applySchedulersWithDelay()
        .subscribe {
          dataHandle(data, it)
          searchView.hint = query.capitalize()
        }
    recyclerview.addOnScrollListener(
        InfiniteScrollListener({
          restApi.openNewSub(subreddit = query, mode = mode, after = after!!)
              .applySchedulersWithDelay()
              .subscribe {
                dataHandleInfiniteScrollListener(data, it)
              }
        }, recyclerview.layoutManager as LinearLayoutManager)
    )
  }


  private fun openSubSort(query: String, mode: String, time: String) {

    restApi.openSubSort(subreddit = query, mode = mode, time = time)
        .applySchedulersWithDelay()
        .subscribe {
          dataHandle(data, it)
          searchView.hint = query.capitalize()
        }
    recyclerview.addOnScrollListener(
        InfiniteScrollListener({
          restApi.openSubSort(subreddit = query, mode = mode, after = after!!, time = time)
              .applySchedulersWithDelay()
              .subscribe {
                dataHandleInfiniteScrollListener(data, it)
              }
        }, recyclerview.layoutManager as LinearLayoutManager)
    )
  }


  private fun runLayoutAnimation(recyclerView: RecyclerView) {
    val context = recyclerView.context
    val controller = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_slide_left)
    recyclerView.layoutAnimation = controller
    recyclerView.adapter.notifyDataSetChanged()
    recyclerView.scheduleLayoutAnimation()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }


  private fun showPopup(v: View) {
    val query = searchView.hint.toString()
    val popup = PopupMenu(this, v)
    val inflater = popup.menuInflater
    inflater.inflate(R.menu.menu_main, popup.menu)
    popup.show()

    popup.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.action_hot -> {
          openSub(query, "hot")
          true
        }
        R.id.action_new -> {
          openSub(query, "new")
          true
        }

        R.id.action_controversial -> {
          popupTopSince(v, "controversial")
          true
        }
        R.id.action_rising -> {
          openSub(query, "rising")
          true
        }
        R.id.action_top -> {
          popupTopSince(v, "top")
          true
        }
        else -> super.onOptionsItemSelected(it)
      }
    }
  }

  private fun popupTopSince(v: View, mode: String) {
    val query = searchView.hint.toString()
    val popup = PopupMenu(this, v)
    val inflater = popup.menuInflater
    inflater.inflate(R.menu.top_since, popup.menu)
    popup.show()
    popup.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.action_hour -> {
          openSubSort(query = query, mode = mode, time = "hour")
          true
        }
        R.id.action_day -> {
          openSubSort(query = query, mode = mode, time = "day")
          true
        }
        R.id.action_week -> {
          openSubSort(query = query, mode = mode, time = "week")
          true
        }
        R.id.action_month -> {
          openSubSort(query = query, mode = mode, time = "month")
          true
        }
        R.id.action_alltime -> {
          openSubSort(query = query, mode = mode, time = "all")
          true
        }
        else -> super.onOptionsItemSelected(it)
      }
    }

  }

  private fun dataHandle(data: ArrayList<DisplayableItem>, newsResponse: RedditNewsResponse) {
    data.clear()
    data.addAll(newsResponse.data.children)
    data.add(Loading())
    // recyclerview.adapter.notifyDataSetChanged()
    runLayoutAnimation(recyclerview)
    after = newsResponse.data.after
    recyclerview.smoothScrollToPosition(0)

  }

  private fun dataHandleInfiniteScrollListener(data: ArrayList<DisplayableItem>,
      newsResponse: RedditNewsResponse) {
    val last = data.size
    data.removeAt(last - 1)
    data.addAll(newsResponse.data.children)
    recyclerview.adapter.notifyItemInserted(last)
    after = newsResponse.data.after
    data.add(Loading())
  }


}

