package kartex.tododer.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.view.setPadding
import kartex.tododer.R
import kartex.tododer.lib.extensions.toDpi
import kartex.tododer.lib.extensions.toDpiF
import kotlin.properties.Delegates

class ProgressView : View {

	// <editor-fold desc="PRIVATE FIELDS">
	private var fillPaint: Paint = Paint()
	private var strokePaint: Paint = Paint().apply {
		style = Paint.Style.STROKE
	}

	private var _ratio: Float by Delegates.notNull()
	private var _strokeColor: Int by Delegates.notNull()
	private var _fillColor: Int by Delegates.notNull()
	private lateinit var _fillColorObj: AlphaColor

	private val radiusDpi: Int = 9
	private val strokeWidthDpi: Float = radiusDpi * strokeRatio
	private var strokeRadiusDpi: Float by Delegates.notNull()
	private var radius: Float by Delegates.notNull()
	private var strokeWidth: Float by Delegates.notNull()
	private var strokeRadius: Float by Delegates.notNull()
	// </editor-fold>

	// <editor-fold desc="PROP`S"
	var ratio: Float
		get() = _ratio
		set(value) {
			_ratio = clippingRatio(value)
			computeFillColor()
			fillPaint.color = _fillColor
			invalidate()
		}

	var strokeColor: Int
		get() = _strokeColor
		set(value) {
			_strokeColor = value
			strokePaint.color = _strokeColor
			invalidate()
		}

	var fillColor: Int
		get() = _fillColor
		set(value) {
			_fillColor = value
			fillPaint.color = _fillColor
			invalidate()
		}
	// </editor-fold>

	// <editor-fold desc="CTOR`S"
	constructor(context: Context) : super(context) {
		init(null, 0)
	}

	constructor(context: Context, attr: AttributeSet?) : super(context, attr) {
		init(attr, 0)
	}

	constructor(context: Context, attr: AttributeSet?, defStyleAttr: Int) : super(context, attr, defStyleAttr) {
		init(attr, defStyleAttr)
	}
	// </editor-fold>

	override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
		computeFillColor()

		val diameter = (radius * 2).toInt()
		val width = paddingLeft + diameter + paddingRight
		val height = paddingTop + diameter + paddingBottom
		setMeasuredDimension(width, height)
	}

	override fun onDraw(canvas: Canvas) {
		super.onDraw(canvas)
		canvas.drawCircle(paddingLeft + radius, paddingTop + radius, radius, fillPaint)
		canvas.drawCircle(paddingLeft + radius, paddingTop + radius, strokeRadius, strokePaint)
	}

	// <editor-fold desc="PRIVATE">
	@SuppressLint("ResourceType")
	private fun init(attr: AttributeSet?, defStyleAttr: Int) {
		setPadding(5.toDpi(context))

		strokeRadiusDpi = radiusDpi - strokeWidthDpi / 2

		radius = radiusDpi.toDpiF(context)
		strokeWidth = strokeWidthDpi.toDpiF(context)
		strokeRadius = strokeRadiusDpi.toDpiF(context)

		strokePaint.strokeWidth = strokeWidth

		val typedArray = context.theme.obtainStyledAttributes(attr, R.styleable.ProgressView, defStyleAttr, 0).apply {
			_ratio = getFloat(R.styleable.ProgressView_ratio, DEF_RATIO)

			val themeColors = colorsFromTheme(context)
			val themeFillColor = themeColors.getColor(0, DEF_FILL_COLOR)
			val themeStrokeColor = themeColors.getColor(1, DEF_STROKE_COLOR)

			val fillColorInt = getColor(R.styleable.ProgressView_fillColor, themeFillColor)
			_fillColorObj = AlphaColor(fillColorInt)
			_strokeColor = getColor(R.styleable.ProgressView_strokeColor, themeStrokeColor)
			computeFillColor()
		}
		typedArray.recycle()

		fillPaint.color = _fillColor
		strokePaint.color = _strokeColor
	}

	private fun computeFillColor() {
		_fillColor = _fillColorObj.computeWithRatio(_ratio)
	}

	private fun clippingRatio(ratio: Float): Float {
		if (ratio > 1.0f)
			return 1.0f
		if (ratio < 0.0f)
			return 0.0f
		return ratio
	}
	// </editor-fold>

	companion object {

		// <editor-fold desc="STATIC PRIVATE">
		private const val strokeRatio: Float = 2 / 9.toFloat()

		private const val DEF_RATIO: Float = 0.0f

		private const val DEF_STROKE_COLOR: Int = 0x88000000.toInt()
		private const val DEF_FILL_COLOR: Int = 0xff242424.toInt()

		private fun colorsFromTheme(context: Context): TypedArray {
			val rIds = intArrayOf(
				com.google.android.material.R.attr.colorPrimary,
				com.google.android.material.R.attr.colorSecondaryVariant
			)

			return context.theme.obtainStyledAttributes(rIds)
		}
		// </editor-fold>
	}
}