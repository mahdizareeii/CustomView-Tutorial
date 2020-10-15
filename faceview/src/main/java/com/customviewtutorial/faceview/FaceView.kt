package com.customviewtutorial.faceview

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View

class FaceView(
    context: Context,
    private val attrs: AttributeSet
) : View(context, attrs) {

    companion object {
        private const val DEFAULT_FACE_COLOR = Color.YELLOW
        private const val DEFAULT_EYES_COLOR = Color.BLACK
        private const val DEFAULT_MOUTH_COLOR = Color.BLACK
        private const val DEFAULT_BORDER_COLOR = Color.BLACK
        private const val DEFAULT_BORDER_WIDTH = 4.0f

        const val HAPPY = 0
        const val SAD = 1
    }

    private val paint = Paint()
    private val mouthPath = Path()
    private var faceColor = DEFAULT_FACE_COLOR
    private var eyesColor = DEFAULT_EYES_COLOR
    private var mouthColor = DEFAULT_MOUTH_COLOR
    private var borderColor = DEFAULT_BORDER_COLOR
    private var borderWith = DEFAULT_BORDER_WIDTH
    private var viewSize = 0

    var happyState = HAPPY
        set(value) {
            field = value
            invalidate()
        }

    init {
        paint.isAntiAlias = true
        setUpAttributes()
    }

    private fun setUpAttributes() {
        val typedArray = context.theme.obtainStyledAttributes(
            attrs, R.styleable.FaceView, 0, 0
        )

        happyState = typedArray.getInt(R.styleable.FaceView_happyState, happyState)
        faceColor = typedArray.getColor(R.styleable.FaceView_faceColor, DEFAULT_FACE_COLOR)
        eyesColor = typedArray.getColor(R.styleable.FaceView_eyesColor, DEFAULT_EYES_COLOR)
        mouthColor = typedArray.getColor(R.styleable.FaceView_mouthColor, DEFAULT_MOUTH_COLOR)
        borderColor = typedArray.getColor(R.styleable.FaceView_borderColor, DEFAULT_BORDER_COLOR)
        borderWith = typedArray.getDimension(R.styleable.FaceView_borderWidth, DEFAULT_BORDER_WIDTH)

        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        viewSize = Math.min(measuredWidth, measuredHeight)
        setMeasuredDimension(viewSize, viewSize)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        if (canvas != null) {
            drawFaceBackground(canvas)
            drawEyes(canvas)
            drawMouth(canvas)
        }
    }

    private fun drawFaceBackground(canvas: Canvas) {
        //draw fill circle
        paint.color = faceColor
        paint.style = Paint.Style.FILL
        val radius = viewSize / 2f
        canvas.drawCircle(viewSize / 2f, viewSize / 2f, radius, paint)
        //draw stroke of circle
        paint.color = borderColor
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = borderWith
        canvas.drawCircle(viewSize / 2f, viewSize / 2f, radius, paint)
    }

    private fun drawEyes(canvas: Canvas) {
        paint.color = eyesColor
        paint.style = Paint.Style.FILL

        val leftEyeRec =
            RectF(viewSize * 0.32f, viewSize * 0.23f, viewSize * 0.43f, viewSize * 0.50f)
        canvas.drawOval(leftEyeRec, paint)

        val rightEyeRec =
            RectF(viewSize * 0.57f, viewSize * 0.23f, viewSize * 0.68f, viewSize * 0.50f)
        canvas.drawOval(rightEyeRec, paint)
    }

    private fun drawMouth(canvas: Canvas) {
        mouthPath.reset()
        mouthPath.moveTo(viewSize * 0.22f, viewSize * 0.7f)

        if (happyState == HAPPY) {
            mouthPath.quadTo(viewSize * 0.50f, viewSize * 0.80f, viewSize * 0.78f, viewSize * 0.70f)
            mouthPath.quadTo(viewSize * 0.50f, viewSize * 0.90f, viewSize * 0.22f, viewSize * 0.70f)
        } else {
            mouthPath.quadTo(viewSize * 0.5f, viewSize * 0.50f, viewSize * 0.78f, viewSize * 0.7f)
            mouthPath.quadTo(viewSize * 0.5f, viewSize * 0.60f, viewSize * 0.22f, viewSize * 0.7f)
        }
        paint.color = mouthColor
        paint.style = Paint.Style.FILL

        canvas.drawPath(mouthPath, paint)
    }

}