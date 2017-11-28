package com.wasseemb.FeatherForReddit

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.support.v7.widget.Toolbar
import android.support.v7.widget.helper.ItemTouchHelper
import android.text.InputType
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import co.zsmb.materialdrawerkt.R.attr.divider
import co.zsmb.materialdrawerkt.builders.drawer
import co.zsmb.materialdrawerkt.draweritems.badgeable.primaryItem
import co.zsmb.materialdrawerkt.draweritems.badgeable.secondaryItem
import com.afollestad.materialdialogs.MaterialDialog
import com.mikepenz.materialdrawer.AccountHeader
import com.mikepenz.materialdrawer.Drawer
import com.wasseemb.FeatherForReddit.Adapter.UserAdapter
import com.wasseemb.FeatherForReddit.Api.RedditNewsResponse
import com.wasseemb.FeatherForReddit.Api.RestApi
import com.wasseemb.FeatherForReddit.extensions.PreferenceHelper.defaultPrefs
import com.wasseemb.FeatherForReddit.extensions.PreferenceHelper.get
import com.wasseemb.FeatherForReddit.extensions.PreferenceHelper.set
import com.wasseemb.FeatherForReddit.extensions.applySchedulersWithDelay
import com.wasseemb.FeatherForReddit.extensions.setupImageClick
import com.wasseemb.FeatherForReddit.extensions.setupItemClick
import com.wasseemb.FeatherForReddit.model.DisplayableItem
import com.wasseemb.FeatherForReddit.model.Loading


class MainActivity : AppCompatActivity() {

  var data = ArrayList<DisplayableItem>()
  val restApi = RestApi()
  var after: String? = ""
  lateinit var result: Drawer
  lateinit var headerResult: AccountHeader
  val p = Paint()

  lateinit var recyclerView: RecyclerView
  lateinit var toolbar: Toolbar
  lateinit var swipeRefreshLayout: SwipeRefreshLayout


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    toolbar = findViewById(R.id.toolbar)
    recyclerView = findViewById(R.id.recyclerview)
    swipeRefreshLayout = findViewById(R.id.swipe_container)

    setSupportActionBar(toolbar)
    setUpRecyclerView()
    setUpDrawer()
    frontPage()
    setUpClicks()
    toolbar.setOnClickListener { recyclerView.scrollToPosition(0) }
    swipeRefreshLayout.setOnRefreshListener {
      funT({ frontPage() }, { openSub(toolbar.title.toString()) })
    }
    swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary)
  }

  inline fun funT(func: () -> Unit, func2: () -> Unit) {
    if (toolbar.title.equals("Feather for Reddit")) func() else
      func2()

  }

  private fun setUpRecyclerView() {
    recyclerView.setHasFixedSize(true)
    recyclerView.setItemViewCacheSize(20)
    recyclerView.isDrawingCacheEnabled = true
    recyclerView.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH
    recyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
    recyclerView.adapter = UserAdapter(this, data)
    val simpleItemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
      override fun onMove(recyclerView: RecyclerView?, viewHolder: ViewHolder?,
          target: ViewHolder?): Boolean {
        return false
        //To change body of created functions use File | Settings | File Templates.
      }

      override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        recyclerView.adapter.notifyItemChanged(position)
        Log.d("Swipe", "Swipe")
//whatever code you want the swipe to perform

        //awesome code when swiping right to remove recycler card and delete SQLite data
      }

      override fun onChildDraw(c: Canvas?, recyclerView: RecyclerView?, viewHolder: ViewHolder,
          dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
        val icon: Bitmap
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {

          val itemView = viewHolder.itemView
          val height = itemView.bottom.toFloat() - itemView.top.toFloat()
          val width = height / 3

          if (dX > 0) {
            p.color = Color.parseColor("#404B50")
            val background = RectF(itemView.left.toFloat(), itemView.top.toFloat(), dX,
                itemView.bottom.toFloat())
            c?.drawRect(background, p)
            icon = BitmapFactory.decodeResource(resources, R.drawable.ic_arrow_upward_black_24dp)
            val icon_dest = RectF(itemView.left.toFloat() + width, itemView.top.toFloat() + width,
                itemView.left.toFloat() + 2 * width, itemView.bottom.toFloat() - width)
            c?.drawBitmap(icon, null, icon_dest, p)
          } else {
            p.color = Color.parseColor("#404B50")
            val background = RectF(itemView.right.toFloat() + dX, itemView.top.toFloat(),
                itemView.right.toFloat(), itemView.bottom.toFloat())
            c?.drawRect(background, p)
            icon = BitmapFactory.decodeResource(resources, R.drawable.ic_arrow_downward_black_24dp)
            val icon_dest = RectF(itemView.right.toFloat() - 2 * width,
                itemView.top.toFloat() + width, itemView.right.toFloat() - width,
                itemView.bottom.toFloat() - width)
            c?.drawBitmap(icon, null, icon_dest, p)
          }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
      }
    }
    val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
    itemTouchHelper.attachToRecyclerView(recyclerView)
  }

  private fun setUpDrawer() {
    val recyclerView = recyclerView
    result = drawer {
      toolbar = this@MainActivity.toolbar
      primaryItem("Home") {
        icon = R.mipmap.ic_launcher
        onClick { _ ->
          frontPage()
          false
        }
      }
      primaryItem("enter a subreddit") {
        onClick { _ ->

          MaterialDialog.Builder(this@MainActivity)
              .title("Enter a Subreddit")
              .inputType(InputType.TYPE_CLASS_TEXT)
              .input("", "",
                  { dialog, input ->
                    openSub(input.toString())

                  })
              .show()
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
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity,
                LinearLayout.VERTICAL, false)
            recyclerView.adapter = UserAdapter(this@MainActivity, data)
            recyclerView.adapter.notifyDataSetChanged()
            setupItemClick(recyclerView.adapter as UserAdapter,
                applicationContext)
            setupImageClick(recyclerView.adapter as UserAdapter,
                applicationContext)
            false
          }
        }
      }
    }
  }

  private fun setUpClicks() {
    setupItemClick(recyclerView.adapter as UserAdapter,
        applicationContext)
    setupImageClick(recyclerView.adapter as UserAdapter,
        applicationContext)
  }

  private fun frontPage(mode: String = "hot", time: String? = null) {
    recyclerView.clearOnScrollListeners()
    restApi.getNews(mode = mode, time = time)
        .applySchedulersWithDelay()
        .subscribe({
          dataHandle(data, it)
        }, { e -> Log.d("Tag", e.toString()) }, {
          swipeRefreshLayout.isRefreshing = false
          toolbar.title = "Feather for Reddit"
          Log.d("Tag", "Completed")
        })
    recyclerView.addOnScrollListener(
        InfiniteScrollListener({
          restApi.getNews(after = after, time = time)
              .applySchedulersWithDelay()
              .subscribe {
                dataHandleInfiniteScrollListener(data, it)
              }
        }, recyclerView.layoutManager as LinearLayoutManager)
    )
  }

  private fun openSub(query: String, mode: String = "hot", time: String? = null) {
    recyclerView.clearOnScrollListeners()
    restApi.openSub(subreddit = query, mode = mode, time = time)
        .applySchedulersWithDelay()
        .subscribe({
          dataHandle(data, it)
          toolbar.title = query.capitalize()
        }, { e -> Log.d("Tag", e.toString()) }, {
          swipeRefreshLayout.isRefreshing = false
          Log.d("Tag", "Completed")
        })

    recyclerView.addOnScrollListener(
        InfiniteScrollListener({
          restApi.openSub(subreddit = query, mode = mode, after = after!!, time = time)
              .applySchedulersWithDelay()
              .subscribe {
                dataHandleInfiniteScrollListener(data, it)
              }
        }, recyclerView.layoutManager as LinearLayoutManager)
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
    menuInflater.inflate(R.menu.top_since, menu.findItem(R.id.action_top).subMenu)
    menuInflater.inflate(R.menu.top_since, menu.findItem(R.id.action_controversial).subMenu)

    return true
  }

  override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    val query = toolbar.title.toString()

    if (!query.equals("Feather for Reddit"))
      return when (item?.itemId) {
        R.id.action_hot -> {
          openSub(query, "hot")
          true
        }
        R.id.action_new -> {
          openSub(query, "new")
          true
        }
        R.id.action_rising -> {
          openSub(query, "rising")
          true
        }
        R.id.action_hour -> {
          openSub(query = query, mode = "top", time = "hour")
          true
        }
        R.id.action_day -> {
          openSub(query = query, mode = "top", time = "day")
          true
        }
        R.id.action_week -> {
          openSub(query = query, mode = "top", time = "week")
          true
        }
        R.id.action_month -> {
          openSub(query = query, mode = "top", time = "month")
          true
        }
        R.id.action_alltime -> {
          openSub(query = query, mode = "top", time = "all")
          true
        }
        R.id.action_hourc -> {
          openSub(query = query, mode = "controversial", time = "hour")
          true
        }
        R.id.action_dayc -> {
          openSub(query = query, mode = "controversial", time = "day")
          true
        }
        R.id.action_weekc -> {
          openSub(query = query, mode = "controversial", time = "week")
          true
        }
        R.id.action_monthc -> {
          openSub(query = query, mode = "controversial", time = "month")
          true
        }
        R.id.action_alltimec -> {
          openSub(query = query, mode = "controversial", time = "all")
          true
        }
        else -> super.onOptionsItemSelected(item)

      }
    else {
      return when (item?.itemId) {
        R.id.action_hot -> {
          frontPage("hot")
          true
        }
        R.id.action_new -> {
          frontPage("new")
          true
        }
        R.id.action_rising -> {
          frontPage("rising")
          true
        }
        R.id.action_hour -> {
          frontPage(mode = "top", time = "hour")
          true
        }
        R.id.action_day -> {
          frontPage(mode = "top", time = "day")
          true
        }
        R.id.action_week -> {
          frontPage(mode = "top", time = "week")
          true
        }
        R.id.action_month -> {
          frontPage(mode = "top", time = "month")
          true
        }
        R.id.action_alltime -> {
          frontPage(mode = "top", time = "all")
          true
        }
        R.id.action_hourc -> {
          frontPage(mode = "controversial", time = "hour")
          true
        }
        R.id.action_dayc -> {
          frontPage(mode = "controversial", time = "day")
          true
        }
        R.id.action_weekc -> {
          frontPage(mode = "controversial", time = "week")
          true
        }
        R.id.action_monthc -> {
          frontPage(mode = "controversial", time = "month")
          true
        }
        R.id.action_alltimec -> {
          frontPage(mode = "controversial", time = "all")
          true
        }
        else -> super.onOptionsItemSelected(item)

      }
    }
  }

  private fun showPopupFront(v: View) {
    val popup = PopupMenu(this, v)
    val inflater = popup.menuInflater
    inflater.inflate(R.menu.menu_main, popup.menu)
    popup.show()
    popup.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.action_hot -> {
          frontPage()
          true
        }
        R.id.action_new -> {
          frontPage("new")
          true
        }

        R.id.action_controversial -> {
          popupTopSinceFront(v, "controversial")
          true
        }
        R.id.action_rising -> {
          frontPage("rising")
          true
        }
        R.id.action_top -> {
          popupTopSinceFront(v, "top")
          true
        }
        else -> super.onOptionsItemSelected(it)
      }
    }
  }

  private fun popupTopSinceFront(v: View, mode: String) {
    val popup = PopupMenu(this, v)
    val inflater = popup.menuInflater
    inflater.inflate(R.menu.top_since, popup.menu)
    popup.show()
    popup.setOnMenuItemClickListener {
      when (it.itemId) {
        R.id.action_hour -> {
          frontPage(mode = mode, time = "hour")
          true
        }
        R.id.action_day -> {
          frontPage(mode = mode, time = "day")
          true
        }
        R.id.action_week -> {
          frontPage(mode = mode, time = "week")
          true
        }
        R.id.action_month -> {
          frontPage(mode = mode, time = "month")
          true
        }
        R.id.action_alltime -> {
          frontPage(mode = mode, time = "all")
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
    runLayoutAnimation(recyclerView)
    after = newsResponse.data.after
    recyclerView.smoothScrollToPosition(0)

  }

  private fun dataHandleInfiniteScrollListener(data: ArrayList<DisplayableItem>,
      newsResponse: RedditNewsResponse) {
    val last = data.size
    data.removeAt(last - 1)
    data.addAll(newsResponse.data.children)
    recyclerView.adapter.notifyItemInserted(last)
    after = newsResponse.data.after
    data.add(Loading())
  }
}