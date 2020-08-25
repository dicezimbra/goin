package br.com.goin.v2

import android.animation.*
import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import android.widget.SeekBar
import kotlinx.android.synthetic.main.view_goin_ticket.view.*
import android.view.animation.Animation.ZORDER_BOTTOM
import android.view.animation.Animation.ZORDER_TOP
import android.view.animation.TranslateAnimation
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.annotation.IntDef
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import br.com.goin.base.extensions.dpToPx
import br.com.goin.base.extensions.gone
import br.com.goin.base.extensions.invisible
import br.com.goin.feature.wallet.R
import br.com.goin.v2.GoinTicketView.TicketState.Companion.CUTOFF
import br.com.goin.v2.GoinTicketView.TicketState.Companion.WITHOUT_SCISSOR
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

private const val ANIM_TRANSLATE_Y = 70f
private const val ANIM_TRANSLATE_X = 30f
private const val ANIM_ROTATION = -20f

class GoinTicketView: LinearLayout{

    @Retention(RetentionPolicy.SOURCE)
    @IntDef(TicketState.NORMAL, TicketState.CUTOFF, TicketState.WITHOUT_SCISSOR)
    annotation class TicketState {
        companion object {
            const val NORMAL = 0
            const val CUTOFF = 1
            const val WITHOUT_SCISSOR = 2
        }
    }

    private var ticketOrientation: Int = 0
    private var topLayout: Int? = null
    private var bottomLayout: Int? = null
    private var circleRadius: Int = 0
    private var ticketElevation: Float = 0f
    private var ticketBackgroundColor: Int = 0
    @TicketState var ticketState: Int = 0

    var onTicketCutoffListener: (() -> Unit) = {}
    var afterAnimationListener: (() -> Unit) = {}

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initAttrs(attrs)
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initAttrs(attrs)
        init()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        when(ticketOrientation){
            TicketView.Orientation.HORIZONTAL -> {
                val topWidth = top_container.measuredWidth
                val bottomWidth = bottom_container.measuredWidth
                val biggerWidth = Math.max(topWidth, bottomWidth)

                if(topWidth != biggerWidth) {
                    val topLayoutParams = top_container.layoutParams
                    topLayoutParams.width = biggerWidth
                    top_container.layoutParams = topLayoutParams
                }

                if(bottomWidth != biggerWidth) {
                    val bottomLayoutParams = bottom_container.layoutParams
                    bottomLayoutParams.width = biggerWidth
                    bottom_container.layoutParams = bottomLayoutParams
                }
            }
            else -> {
                val topHeight = top_container.measuredHeight
                val bottomHeight = bottom_container.measuredHeight
                val biggerHeight = Math.max(topHeight, bottomHeight)

                if(topHeight != biggerHeight) {
                    val topLayoutParams = top_container.layoutParams
                    topLayoutParams.height = biggerHeight
                    top_container.layoutParams = topLayoutParams
                }

                if(bottomHeight != biggerHeight) {
                    val bottomLayoutParams = bottom_container.layoutParams
                    bottomLayoutParams.height = biggerHeight
                    bottom_container.layoutParams = bottomLayoutParams
                }

                if(bottomHeight != biggerHeight || topHeight != biggerHeight)
                    invalidate()
            }
        }
    }

    private fun initAttrs(attrs: AttributeSet?) {
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.GoinTicketView)
            ticketOrientation = typedArray.getInt(R.styleable.GoinTicketView_goinTicketOrientation, TicketView.Orientation.HORIZONTAL)
            topLayout = typedArray.getResourceId(R.styleable.GoinTicketView_goinTicketTopLayout, 0)
            bottomLayout = typedArray.getResourceId(R.styleable.GoinTicketView_goinTicketBottomLayout, 0)
            ticketBackgroundColor = typedArray.getColor(R.styleable.GoinTicketView_goinTicketBackground, 0)
            ticketState = typedArray.getInt(R.styleable.GoinTicketView_goinTicketStatus, 0)
            circleRadius = typedArray.getDimensionPixelSize(R.styleable.GoinTicketView_goinTicketCirclesRadius, 20.dpToPx())

            if (typedArray.hasValue(R.styleable.GoinTicketView_goinTicketElevation)) {
                ticketElevation = typedArray.getDimension(R.styleable.GoinTicketView_goinTicketElevation, 0f)
            } else if (typedArray.hasValue(R.styleable.TicketView_android_elevation)) {
                ticketElevation = typedArray.getDimension(R.styleable.TicketView_android_elevation, 0f)
            }
            typedArray.recycle()
        }
    }

    private fun init(){
        when(ticketOrientation){
            TicketView.Orientation.HORIZONTAL -> {
                View.inflate(context, R.layout.view_goin_ticket, this)
            }
            else -> {
                View.inflate(context, R.layout.view_goin_ticket_vertical, this)
            }
        }
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        passParameterToTicketView()
        configureContainersPadding()
        configureSeekbar()
        configureState()

        if(topLayout != null && topLayout!! > 0){
            val view = findViewById<View>(topLayout!!)
            if(view != null) {
                detachViewFromParent(view)
                top_container.addView(view)
            }
        }

        if(bottomLayout != null && bottomLayout!! > 0){
            val view = findViewById<View>(bottomLayout!!)
            if(view != null) {
                detachViewFromParent(view)
                bottom_container.addView(view)
            }
        }
    }

    private fun configureContainersPadding() {
        val maxElevation = 24.dpToPx()
        val mShadowBlurRadius = Math.min(25f * (ticketElevation / maxElevation), 25f)

        top_container.setPadding(
            top_container.paddingLeft + mShadowBlurRadius.toInt(),
            top_container.paddingTop + mShadowBlurRadius.toInt() / 2,
            top_container.paddingRight + mShadowBlurRadius.toInt(),
            top_container.paddingBottom
        )

        bottom_container.setPadding(
            bottom_container.paddingLeft + mShadowBlurRadius.toInt(),
            bottom_container.paddingTop,
            bottom_container.paddingRight + mShadowBlurRadius.toInt(),
            bottom_container.paddingBottom + mShadowBlurRadius.toInt() / 2
        )
    }

    private fun configureSeekbar() {
        seek_bar.setPadding(circleRadius * 2,0,0 + circleRadius * 2, 0)
        seek_bar.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                if(seekBar.progress < 100){
                    ObjectAnimator.ofInt(seekBar, "progress", 0).setDuration(200).start()
                }else{
                    onTicketCutoffListener()
                }
            }
        })
    }

    fun cutOffTicket() {
        val translateY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, ANIM_TRANSLATE_Y.dpToPx())
        val translateX = PropertyValuesHolder.ofFloat(View.TRANSLATION_X, ANIM_TRANSLATE_X.dpToPx())
        val alpha = PropertyValuesHolder.ofFloat(View.ALPHA, 0f)
        val rotation = PropertyValuesHolder.ofFloat(View.ROTATION, ANIM_ROTATION)

        ObjectAnimator.ofFloat(seek_bar, "alpha", 0f).setDuration(200).start()

        val animator1 = ObjectAnimator.ofPropertyValuesHolder(bottom_ticket, translateY, translateX, rotation, alpha)
        val animator2 = ObjectAnimator.ofPropertyValuesHolder(bottom_container, translateY, translateX, rotation, alpha)
        label_regulament.gone()

        val animSetXY = AnimatorSet()
        animSetXY.addListener(object : Animator.AnimatorListener {
            override fun onAnimationEnd(animation: Animator?) {
                afterAnimationListener()
                bottom_container.gone()
                bottom_ticket.gone()
                seek_bar.gone()
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }
        })

        animSetXY.playTogether(animator1, animator2)
        animSetXY.interpolator = DecelerateInterpolator()
        animSetXY.duration = 500
        animSetXY.start()
    }

    fun configureState(){
        when(ticketState){
           CUTOFF -> {
               bottom_ticket.translationY = ANIM_TRANSLATE_Y.dpToPx()
               bottom_ticket.translationX = ANIM_TRANSLATE_X.dpToPx()
               bottom_ticket.rotation = ANIM_ROTATION

               seek_bar.visibility = View.GONE
           }

            WITHOUT_SCISSOR -> {
                seek_bar.isEnabled = false
                seek_bar.thumb = null
            }
            else -> {
                seek_bar.isEnabled = true
                seek_bar.thumb = ContextCompat.getDrawable(context, R.drawable.seekbar_thumb)
            }
        }
    }

    fun reset(){
        ObjectAnimator.ofInt(seek_bar, "progress", 0).setDuration(200).start()
    }

    private fun passParameterToTicketView() {
        top_ticket.scallopRadius = circleRadius
        bottom_ticket.scallopRadius = circleRadius
        top_ticket.setTicketElevation(ticketElevation)
        bottom_ticket.setTicketElevation(ticketElevation)
        top_ticket.setBackgroundColor(ticketBackgroundColor)
        bottom_ticket.setBackgroundColor(ticketBackgroundColor)
        top_ticket.orientation = ticketOrientation
        bottom_ticket.orientation = ticketOrientation
    }
}