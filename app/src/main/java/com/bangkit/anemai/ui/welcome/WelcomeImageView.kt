package com.bangkit.anemai.ui.welcome

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.util.AttributeSet
import android.view.View
import com.bangkit.anemai.R

class WelcomeImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mImage: Bitmap? = null
    private var mMask: Bitmap? = null
    private var result: Bitmap? = null
    private lateinit var tempCanvas: Canvas
    private val paint: Paint = Paint().apply {
        isFilterBitmap = false
    }
    var imageVerticalOffset = 150f

    init {
        mImage = BitmapFactory.decodeResource(resources, R.drawable.character_in_welcome)
        mMask = BitmapFactory.decodeResource(resources, R.drawable.shapemask_in_welcome_2)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        mImage?.let { image ->
            val scaledWidth = (w * 0.7).toInt()
            val scaledHeight = (h * 0.7).toInt()
            val scaledImage = Bitmap.createScaledBitmap(image, scaledWidth, scaledHeight, true)
            result = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
            tempCanvas = Canvas(result!!)
            mImage = scaledImage
        }

        mMask?.let { mask ->
            val scaledMask = Bitmap.createScaledBitmap(mask, w, h, true)
            mMask = scaledMask
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        mImage?.let { image ->
            mMask?.let { mask ->
                result?.eraseColor(Color.TRANSPARENT)

                val offsetX = (width - image.width) / 2f
                val offsetY = (height - image.height) / 2f + imageVerticalOffset

                tempCanvas.drawBitmap(image, offsetX, offsetY, paint)

                paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.DST_ATOP)
                tempCanvas.drawBitmap(mask, 0f, 0f, paint)
                paint.xfermode = null

                canvas.drawBitmap(result!!, 0f, 0f, null)
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(width, height)
    }
}