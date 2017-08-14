package com.wasseemb.FeatherForReddit

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.support.v4.view.ViewCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet

/**
 * Created by Wasseem on 08/06/2017.
 */

class RoundedImageView : AppCompatImageView {

  private var mMaskPath: Path? = null
  private val mMaskPaint = Paint(Paint.ANTI_ALIAS_FLAG)
  private var mCornerRadius = 10

  constructor(context: Context) : super(context) {

    init()
  }

  constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {

    init()
  }

  constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs,
      defStyle) {

    init()
  }

  private fun init() {
    ViewCompat.setLayerType(this, ViewCompat.LAYER_TYPE_SOFTWARE, null)
    mMaskPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
  }

  fun setCornerRadius(cornerRadius: Int) {
    mCornerRadius = cornerRadius
    generateMaskPath(width, height)
    invalidate()
  }

  override fun onSizeChanged(w: Int, h: Int, oldW: Int, oldH: Int) {
    super.onSizeChanged(w, h, oldW, oldH)

    if (w != oldW || h != oldH) {
      generateMaskPath(w, h)
    }
  }

  private fun generateMaskPath(w: Int, h: Int) {
    mMaskPath = Path()
    mMaskPath!!.addRoundRect(RectF(0f, 0f, w.toFloat(), h.toFloat()), mCornerRadius.toFloat(),
        mCornerRadius.toFloat(), Path.Direction.CW)
    mMaskPath!!.fillType = Path.FillType.INVERSE_WINDING
  }

  override fun onDraw(canvas: Canvas) {
    if (canvas.isOpaque) { // If canvas is opaque, make it transparent
      canvas.saveLayerAlpha(0f, 0f, canvas.width.toFloat(), canvas.height.toFloat(), 255,
          Canvas.HAS_ALPHA_LAYER_SAVE_FLAG)
    }

    super.onDraw(canvas)

    if (mMaskPath != null) {
      canvas.drawPath(mMaskPath!!, mMaskPaint)
    }
  }
}