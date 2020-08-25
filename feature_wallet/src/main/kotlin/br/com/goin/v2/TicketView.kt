package br.com.goin.v2

import android.content.Context
import android.graphics.*
import android.graphics.Bitmap.Config.ALPHA_8
import android.graphics.Color.BLACK
import android.graphics.Color.TRANSPARENT
import android.graphics.Paint.ANTI_ALIAS_FLAG
import android.graphics.PorterDuff.Mode.SRC_IN
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import br.com.goin.base.extensions.dpToPx
import br.com.goin.feature.wallet.R
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

/**
 * Created by Vipul Asri on 31/10/17.
 */

class TicketView : View {

    private val mBackgroundPaint = Paint()
    private val mBorderPaint = Paint()
    private var mOrientation: Int = 0
    private val mPath = Path()
    private val mShadowPath = Path()
    private var mDirty = true
    private var mDividerStartX: Float = 0.toFloat()
    private var mDividerStartY: Float = 0.toFloat()
    private var mDividerStopX: Float = 0.toFloat()
    private var mDividerStopY: Float = 0.toFloat()
    private val mRect = RectF()
    private val mRoundedCornerArc = RectF()
    private val mScallopCornerArc = RectF()
    private var mScallopHeight: Int = 0
    private var mScallopPosition: Float = 0.toFloat()
    private var mScallopPositionPercent: Float = 0.toFloat()
    private var mBackgroundColor: Int = 0
    private var mShowBorder: Boolean = false
    private var mBorderWidth: Int = 0
    private var mBorderColor: Int = 0
    private var mShowDivider: Boolean = false
    private var mScallopRadius: Int = 0
    private var mDividerDashLength: Int = 0
    private var mDividerDashGap: Int = 0
    private var mDividerType: Int = 0
    private var mDividerWidth: Int = 0
    private var mDividerColor: Int = 0
    private var mCornerType: Int = 0
    private var mCornerRadius: Int = 0
    private var mDividerPadding: Int = 0
    private var mShadow: Bitmap? = null
    private val mShadowPaint = Paint(ANTI_ALIAS_FLAG)
    private var mShadowBlurRadius = 0f
    private var mShowBottomShadow = true
    private var mShowTopShadow = true


    var orientation: Int
        get() = mOrientation
        set(orientation) {
            this.mOrientation = orientation
            initElements()
        }

    var scallopPositionPercent: Float
        get() = mScallopPositionPercent
        set(scallopPositionPercent) {
            this.mScallopPositionPercent = scallopPositionPercent
            initElements()
        }

    var isShowBorder: Boolean
        get() = mShowBorder
        set(showBorder) {
            this.mShowBorder = showBorder
            initElements()
        }

    var borderWidth: Int
        get() = mBorderWidth
        set(borderWidth) {
            this.mBorderWidth = borderWidth
            initElements()
        }

    var borderColor: Int
        get() = mBorderColor
        set(borderColor) {
            this.mBorderColor = borderColor
            initElements()
        }

    var isShowDivider: Boolean
        get() = mShowDivider
        set(showDivider) {
            this.mShowDivider = showDivider
            initElements()
        }

    var scallopRadius: Int
        get() = mScallopRadius
        set(scallopRadius) {
            this.mScallopRadius = scallopRadius
            initElements()
        }

    var dividerDashLength: Int
        get() = mDividerDashLength
        set(dividerDashLength) {
            this.mDividerDashLength = dividerDashLength
            initElements()
        }

    var dividerDashGap: Int
        get() = mDividerDashGap
        set(dividerDashGap) {
            this.mDividerDashGap = dividerDashGap
            initElements()
        }

    var dividerType: Int
        get() = mDividerType
        set(dividerType) {
            this.mDividerType = dividerType
            initElements()
        }

    var dividerWidth: Int
        get() = mDividerWidth
        set(dividerWidth) {
            this.mDividerWidth = dividerWidth
            initElements()
        }

    var dividerPadding: Int
        get() = mDividerPadding
        set(mDividerPadding) {
            this.mDividerPadding = mDividerPadding
            initElements()
        }

    var dividerColor: Int
        get() = mDividerColor
        set(dividerColor) {
            this.mDividerColor = dividerColor
            initElements()
        }

    var cornerType: Int
        get() = mCornerType
        set(cornerType) {
            this.mCornerType = cornerType
            initElements()
        }

    var cornerRadius: Int
        get() = mCornerRadius
        set(cornerRadius) {
            this.mCornerRadius = cornerRadius
            initElements()
        }

    private val isJellyBeanAndAbove: Boolean
        get() = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(Orientation.HORIZONTAL, Orientation.VERTICAL)
    annotation class Orientation {
        companion object {
            const val HORIZONTAL = 0
            const val VERTICAL = 1
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(DividerType.NORMAL, DividerType.DASH)
    annotation class DividerType {
        companion object {
            const val NORMAL = 0
            const val DASH = 1
        }
    }

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(CornerType.NORMAL, CornerType.ROUNDED)
    annotation class CornerType {
        companion object {
            const val NORMAL = 0
            const val ROUNDED = 1
            const val SCALLOP = 2
        }
    }

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mDirty) {
            doLayout()
        }
        if (mShadowBlurRadius > 0f && !isInEditMode) {
            canvas.drawBitmap(mShadow!!, 0f, 0f, null)
        }
        canvas.drawPath(mPath, mBackgroundPaint)
        if (mShowBorder) {
            canvas.drawPath(mPath, mBorderPaint)
        }
    }


    private fun doLayout() {
        val offset: Float
        val left = paddingLeft + mShadowBlurRadius
        val right = width.toFloat() - paddingRight.toFloat() - mShadowBlurRadius

        val top = if (mShowTopShadow) {
            paddingTop + mShadowBlurRadius / 2
        }else{
            paddingTop.toFloat()
        }

        val bottom = if (mShowBottomShadow) {
            height.toFloat() - paddingBottom.toFloat() - mShadowBlurRadius / 2
        }else{
            (height.toFloat() - (paddingBottom.toFloat()))
        }

        mPath.reset()
        mShadowPath.reset()

        if (mOrientation == Orientation.HORIZONTAL) {
            offset = (top + bottom) / mScallopPosition - mScallopRadius

            if (mCornerType == CornerType.ROUNDED) {
                if(mScallopPositionPercent != 0f) {
                    mPath.arcTo(getTopLeftCornerRoundedArc(top, left), 180.0f, 90.0f, false)
                    mPath.lineTo(left + mCornerRadius, top)
                    mPath.lineTo(right - mCornerRadius, top)
                    mPath.arcTo(getTopRightCornerRoundedArc(top, right), -90.0f, 90.0f, false)

                    mShadowPath.arcTo(getTopLeftCornerRoundedArc(top, left), 180.0f, 90.0f, false)
                    mShadowPath.lineTo( 0f + mCornerRadius, top)
                    mShadowPath.lineTo(right - mCornerRadius, top)
                    mShadowPath.arcTo(getTopRightCornerRoundedArc(top, right), -90.0f, 90.0f, false)
                }else{
                    //mPath.moveTo(left, top)
                    //mPath.lineTo(right, top)
                }

            }else {
                mPath.moveTo(left, top)
                mPath.lineTo(right, top)

                mShadowPath.moveTo(0f, top)
                mShadowPath.lineTo(right, top)
            }

            mRect.set(
                    right - mScallopRadius,
                    top + offset,
                    right + mScallopRadius,
                    mScallopHeight.toFloat() + offset + top
            )
            mPath.arcTo(mRect, 270f, -180.0f, false)
            mShadowPath.arcTo(mRect, 270f, -180.0f, false)

            if (mCornerType == CornerType.ROUNDED) {
                if(mScallopPositionPercent < 90f) {

                    mPath.arcTo(getBottomRightCornerRoundedArc(bottom, right), 0.0f, 90.0f, false)
                    mPath.lineTo(right - mCornerRadius, bottom)

                    mPath.lineTo(left + mCornerRadius, bottom)
                    mPath.arcTo(getBottomLeftCornerRoundedArc(left, bottom), 90.0f, 90.0f, false)

                    mShadowPath.arcTo(getBottomRightCornerRoundedArc(bottom, right), 0.0f, 90.0f, false)
                    mShadowPath.lineTo(right - mCornerRadius, bottom)

                    mShadowPath.lineTo(left + mCornerRadius, bottom)
                    mShadowPath.arcTo(getBottomLeftCornerRoundedArc(left, bottom), 90.0f, 90.0f, false)
                }else{
                    //mPath.lineTo(right, bottom)
                    //mPath.lineTo(left, bottom)

                    //mShadowPath.lineTo(right, bottom)
                    //mShadowPath.lineTo(left, bottom)
                }

            }  else {
                mPath.lineTo(right, bottom)
                mPath.lineTo(left, bottom)

                mShadowPath.lineTo(right, bottom)
                mShadowPath.lineTo(left, bottom)
            }

            mRect.set(
                    left - mScallopRadius,
                    top + offset,
                    left + mScallopRadius,
                    mScallopHeight.toFloat() + offset + top
            )

            mPath.arcTo(mRect, 90.0f, -180.0f, false)
            mPath.close()

            mShadowPath.arcTo(mRect, 90.0f, -180.0f, false)
            mShadowPath.close()

        } else {
            offset = (right + left) / mScallopPosition - mScallopRadius

            if (mCornerType == CornerType.ROUNDED) {
                if(mScallopPositionPercent != 0f) {
                    mPath.arcTo(getTopLeftCornerRoundedArc(top, left), 180.0f, 90.0f, false)
                    mPath.lineTo(left + mCornerRadius, top)

                    mShadowPath.arcTo(getTopLeftCornerRoundedArc(top, left), 180.0f, 90.0f, false)
                    mShadowPath.lineTo(left + mCornerRadius, top)
                }
            }

            mRect.set(
                    left + offset,
                    top - mScallopRadius,
                    mScallopHeight.toFloat() + offset + left,
                    top + mScallopRadius
            )
            mPath.arcTo(mRect, 180.0f, -180.0f, false)
            mShadowPath.arcTo(mRect, 180.0f, -180.0f, false)

            if (mCornerType == CornerType.ROUNDED) {
                if(mScallopPositionPercent < 90f) {
                    mPath.lineTo(right - mCornerRadius, top)
                    mPath.arcTo(getTopRightCornerRoundedArc(top, right), -90.0f, 90.0f, false)

                    mPath.arcTo(getBottomRightCornerRoundedArc(bottom, right), 0.0f, 90.0f, false)
                    mPath.lineTo(right - mCornerRadius, bottom)

                    mShadowPath.lineTo(right - mCornerRadius, top)
                    mShadowPath.arcTo(getTopRightCornerRoundedArc(top, right), -90.0f, 90.0f, false)

                    mShadowPath.arcTo(getBottomRightCornerRoundedArc(bottom, right), 0.0f, 90.0f, false)
                    mShadowPath.lineTo(right - mCornerRadius, bottom)
                }

            }  else {
                mPath.lineTo(right, top)
                mPath.lineTo(right, bottom)

                mShadowPath.lineTo(right, top)
                mShadowPath.lineTo(right, bottom)
            }

            mRect.set(
                    left + offset,
                    bottom - mScallopRadius,
                    mScallopHeight.toFloat() + offset + left,
                    bottom + mScallopRadius
            )
            mPath.arcTo(mRect, 0.0f, -180.0f, false)
            mShadowPath.arcTo(mRect, 0.0f, -180.0f, false)

            if (mCornerType == CornerType.ROUNDED) {
                if(mScallopPositionPercent != 0f) {
                    mPath.arcTo(getBottomLeftCornerRoundedArc(left, bottom), 90.0f, 90.0f, false)
                    mPath.lineTo(left, bottom - mCornerRadius)

                    mShadowPath.arcTo(getBottomLeftCornerRoundedArc(left, bottom), 90.0f, 90.0f, false)
                    mShadowPath.lineTo(left, bottom - mCornerRadius)
                }

            }  else {
                mPath.lineTo(left, bottom)
                mShadowPath.lineTo(left, bottom)
            }
            mPath.close()
            mShadowPath.close()
        }

        generateShadow()
        mDirty = false
    }


    private fun generateShadow() {
        if (isJellyBeanAndAbove && !isInEditMode) {
            if (mShadowBlurRadius == 0f) return

            if (mShadow == null) {
                if (mShowTopShadow) {
                    mShadow = Bitmap.createBitmap(width, height, ALPHA_8)
                } else {
                    mShadow = Bitmap.createBitmap(width + mShadowBlurRadius.toInt(), height + mShadowBlurRadius.toInt(), ALPHA_8)
                }
            } else {
                mShadow!!.eraseColor(TRANSPARENT)
            }
            val c = Canvas(mShadow!!)
            val matrix = Matrix()

            matrix.postScale(1.15f, 1.15f)
            mShadowPath.transform(matrix)
            c.drawPath(mPath, mShadowPaint)

            val rs = RenderScript.create(context)
            val blur = ScriptIntrinsicBlur.create(rs, Element.U8(rs))
            val input = Allocation.createFromBitmap(rs, mShadow)
            val output = Allocation.createTyped(rs, input.type)
            blur.setRadius(mShadowBlurRadius)
            blur.setInput(input)
            blur.forEach(output)
            output.copyTo(mShadow)
            input.destroy()
            output.destroy()
            blur.destroy()
        }
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.TicketView)
            mOrientation = typedArray.getInt(R.styleable.TicketView_ticketOrientation, Orientation.HORIZONTAL)
            mBackgroundColor = typedArray.getColor(
                    R.styleable.TicketView_ticketBackgroundColor,
                    resources.getColor(android.R.color.white)
            )
            mScallopRadius = typedArray.getDimensionPixelSize(
                    R.styleable.TicketView_ticketScallopRadius,
                    20.dpToPx()
            )
            mScallopPositionPercent = typedArray.getFloat(R.styleable.TicketView_ticketScallopPositionPercent, 50f)
            mShowBorder = typedArray.getBoolean(R.styleable.TicketView_ticketShowBorder, false)
            mBorderWidth = typedArray.getDimensionPixelSize(
                    R.styleable.TicketView_ticketBorderWidth,
                    2.dpToPx()
            )
            mBorderColor = typedArray.getColor(
                    R.styleable.TicketView_ticketBorderColor,
                    resources.getColor(android.R.color.black)
            )
            mShowDivider = typedArray.getBoolean(R.styleable.TicketView_ticketShowDivider, false)
            mDividerType = typedArray.getInt(R.styleable.TicketView_ticketDividerType, DividerType.NORMAL)
            mDividerWidth = typedArray.getDimensionPixelSize(
                    R.styleable.TicketView_ticketDividerWidth,
                    2.dpToPx()
            )
            mDividerColor = typedArray.getColor(
                    R.styleable.TicketView_ticketDividerColor,
                    resources.getColor(android.R.color.darker_gray)
            )
            mDividerDashLength = typedArray.getDimensionPixelSize(
                    R.styleable.TicketView_ticketDividerDashLength,
                    8.dpToPx()
            )
            mDividerDashGap = typedArray.getDimensionPixelSize(
                    R.styleable.TicketView_ticketDividerDashGap,
                    4.dpToPx()
            )
            mCornerType = typedArray.getInt(R.styleable.TicketView_ticketCornerType, CornerType.NORMAL)
            mShowBottomShadow = typedArray.getBoolean(R.styleable.TicketView_ticketShowBottomShadow, true)
            mShowTopShadow = typedArray.getBoolean(R.styleable.TicketView_ticketShowTopShadow, true)
            mCornerRadius = typedArray.getDimensionPixelSize(
                    R.styleable.TicketView_ticketCornerRadius,
                    4.dpToPx()
            )
            mDividerPadding = typedArray.getDimensionPixelSize(
                    R.styleable.TicketView_ticketDividerPadding,
                    10.dpToPx()
            )
            var elevation = 0f
            if (typedArray.hasValue(R.styleable.TicketView_ticketElevation)) {
                elevation = typedArray.getDimension(R.styleable.TicketView_ticketElevation, elevation)
            } else if (typedArray.hasValue(R.styleable.TicketView_android_elevation)) {
                elevation = typedArray.getDimension(R.styleable.TicketView_android_elevation, elevation)
            }
            if (elevation > 0f) {
                setShadowBlurRadius(elevation)
            }

            typedArray.recycle()
        }

        mShadowPaint.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, R.color.shadow), SRC_IN)
        mShadowPaint.alpha = 50
        initElements()

        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    private fun initElements() {
        if (mDividerWidth > mScallopRadius) {
            mDividerWidth = mScallopRadius
            Log.w(
                    TAG,
                    "You cannot apply divider width greater than scallop radius. Applying divider width to scallop radius."
            )
        }

        mScallopPosition = 100 / mScallopPositionPercent
        mScallopHeight = mScallopRadius * 2

        setBackgroundPaint()
        setBorderPaint()

        mDirty = true
        invalidate()
    }

    private fun setBackgroundPaint() {
        mBackgroundPaint.alpha = 0
        mBackgroundPaint.isAntiAlias = true
        mBackgroundPaint.color = mBackgroundColor
        mBackgroundPaint.style = Paint.Style.FILL
    }

    private fun setBorderPaint() {
        mBorderPaint.alpha = 0
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = mBorderColor
        mBorderPaint.strokeWidth = mBorderWidth.toFloat()
        mBorderPaint.style = Paint.Style.STROKE
    }

    private fun getTopLeftCornerRoundedArc(top: Float, left: Float): RectF {
        mRoundedCornerArc.set(left, top, left + mCornerRadius * 2, top + mCornerRadius * 2)
        return mRoundedCornerArc
    }

    private fun getTopRightCornerRoundedArc(top: Float, right: Float): RectF {
        mRoundedCornerArc.set(right - mCornerRadius * 2, top, right, top + mCornerRadius * 2)
        return mRoundedCornerArc
    }

    private fun getBottomLeftCornerRoundedArc(left: Float, bottom: Float): RectF {
        mRoundedCornerArc.set(left, bottom - mCornerRadius * 2, left + mCornerRadius * 2, bottom)
        return mRoundedCornerArc
    }

    private fun getBottomRightCornerRoundedArc(bottom: Float, right: Float): RectF {
        mRoundedCornerArc.set(right - mCornerRadius * 2, bottom - mCornerRadius * 2, right, bottom)
        return mRoundedCornerArc
    }

    private fun getTopLeftCornerScallopArc(top: Float, left: Float): RectF {
        mScallopCornerArc.set(left - mCornerRadius, top - mCornerRadius, left + mCornerRadius, top + mCornerRadius)
        return mScallopCornerArc
    }

    private fun getTopRightCornerScallopArc(top: Float, right: Float): RectF {
        mScallopCornerArc.set(right - mCornerRadius, top - mCornerRadius, right + mCornerRadius, top + mCornerRadius)
        return mScallopCornerArc
    }

    private fun getBottomLeftCornerScallopArc(left: Float, bottom: Float): RectF {
        mScallopCornerArc.set(
                left - mCornerRadius,
                bottom - mCornerRadius,
                left + mCornerRadius,
                bottom + mCornerRadius
        )
        return mScallopCornerArc
    }

    private fun getBottomRightCornerScallopArc(bottom: Float, right: Float): RectF {
        mScallopCornerArc.set(
                right - mCornerRadius,
                bottom - mCornerRadius,
                right + mCornerRadius,
                bottom + mCornerRadius
        )
        return mScallopCornerArc
    }

    fun getBackgroundColor(): Int {
        return mBackgroundColor
    }

    override fun setBackgroundColor(backgroundColor: Int) {
        this.mBackgroundColor = backgroundColor
        initElements()
    }

    fun setTicketElevation(elevation: Float) {
        if (!isJellyBeanAndAbove) {
            Log.w(TAG, "Ticket elevation only works with Android Jelly Bean and above")
            return
        }
        setShadowBlurRadius(elevation)
        initElements()
    }

    private fun setShadowBlurRadius(elevation: Float) {
        if (!isJellyBeanAndAbove) {
            Log.w(TAG, "Ticket elevation only works with Android Jelly Bean and above")
            return
        }
        val maxElevation = 24.dpToPx()
        mShadowBlurRadius = Math.min(25f * (elevation / maxElevation), 25f)
    }

    companion object {

        val TAG = TicketView::class.java.simpleName
    }
}
