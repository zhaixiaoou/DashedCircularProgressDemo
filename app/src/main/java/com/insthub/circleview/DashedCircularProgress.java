package com.insthub.circleview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.RelativeLayout;

/**
 * Created by xiaoouzhai on 16/10/19.
 */

public class DashedCircularProgress extends RelativeLayout {

    private static final String TAG = DashedCircularProgress.class.getName();


    private Interpolator interpolator = new AccelerateDecelerateInterpolator();

    private int progressColor = Color.parseColor("#20af60");
    private int dBaserPainterColor = Color.parseColor("#444444");

    private int PADDING = 30;
    private boolean isInitCircleProgress = true;
    private boolean isInnerCircle = true;
    private boolean isAnimation = true;
    private boolean isHead      = true;
    private float min = 0;
    private float begin = min;

    private float max = 100;
    private ValueAnimator valueAnimator;
    private OnValueChangeListener valueChangeListener;
    private float mValue;
    private int duration = 1000;//动画的时间间隔

    private int progressStrokeWidth = 20;
    private int circleStrokeWidth = 5;
    private int headRadius  = 10;//默认小圆点的半径

    private int paddingT;
    private boolean isComplete = true;

    private int dashWith = 3;
    private float dashSpace = 30;//虚线之前的宽度
    private int dashCount = 60;//虚线总数 默认是60

    private Paint progressPaint;
    private Paint basePaint;
    private Paint circlrPaint;
    private Paint circlrProgressPaint;



    private RectF baseCircle;
    private RectF progressCircle;
    private float baseStartAngle = 270f;
    private float baseFinishAngle = 359.8f;
    private float progressStartAngle = 270f;
    private float plusAngle;
    private int paddingR;
    private int paddingL;
    private int paddingB;

    public boolean isContinue() {
        return isContinue;
    }

    private boolean isContinue;

    private int innerCircleWidth ;//内圈
    private Context     mContext;

    public DashedCircularProgress(Context context){
        this(context,null);
        mContext = context;
    }

    public DashedCircularProgress(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public DashedCircularProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context, attrs, defStyleAttr);
    }


    private void init(Context context, AttributeSet attributeSet, int defStyleAttr) {
        setWillNotDraw(false);
        TypedArray attributes = context.obtainStyledAttributes(attributeSet, R.styleable.DashedCircularProgress, defStyleAttr, 0);
        //获取padding属性
        paddingT = getPaddingTop();
        paddingL = getPaddingLeft();
        paddingR = getPaddingRight();
        paddingB = getPaddingBottom();

        paddingT = PADDING + paddingT + progressStrokeWidth / 2;
        paddingL = PADDING + paddingL + progressStrokeWidth / 2;
        paddingR = PADDING + paddingR + progressStrokeWidth / 2;
        paddingB = PADDING + paddingB + progressStrokeWidth / 2;
        Log.d(TAG, "init: paddingT="+paddingT);
        Log.d(TAG, "init: paddingL="+paddingL);
        Log.d(TAG, "init: paddingR="+paddingR);
        Log.d(TAG, "init: paddingB="+paddingB);
        initAttributes(attributes);
    }

    private void initAttributes(TypedArray attributes) {

        dBaserPainterColor = attributes.getColor(R.styleable.DashedCircularProgress_base_color,
                dBaserPainterColor);
        progressColor = attributes.getColor(R.styleable.DashedCircularProgress_progress_color,
                progressColor);
        max = attributes.getFloat(R.styleable.DashedCircularProgress_max, max);
        duration = attributes.getInt(R.styleable.DashedCircularProgress_duration, duration);
        isInitCircleProgress = attributes.getBoolean(R.styleable.DashedCircularProgress_circle_progress,isInitCircleProgress);
        isInnerCircle   = attributes.getBoolean(R.styleable.DashedCircularProgress_inner_circle,isInnerCircle);
        progressStrokeWidth = attributes.getDimensionPixelOffset(R.styleable.DashedCircularProgress_progress_stroke_width,
                progressStrokeWidth);
        circleStrokeWidth   = attributes.getDimensionPixelOffset(R.styleable.DashedCircularProgress_circle_stroke_width,circleStrokeWidth);
        innerCircleWidth = attributes.getDimensionPixelOffset(R.styleable.DashedCircularProgress_inner_circle_width,
                innerCircleWidth);
        headRadius  = attributes.getDimensionPixelOffset(R.styleable.DashedCircularProgress_head_radius,headRadius);
        dashCount = attributes.getInteger(R.styleable.DashedCircularProgress_dash_count,dashCount);
        isAnimation = attributes.getBoolean(R.styleable.DashedCircularProgress_is_animation,isAnimation);
        isHead      = attributes.getBoolean(R.styleable.DashedCircularProgress_is_head,isHead);
        initPainters();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "onMeasure");
        Log.d(TAG, "onMeasure: 周长="+getMeasuredWidth()*Math.PI);
        dashSpace =  (float) (((getMeasuredWidth()-paddingL-paddingR)*Math.PI)/dashCount);
        Log.d(TAG, "onMeasure: dashSpace="+dashSpace);
        initProgressPainter();
        initBasePainter();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.e(TAG, "onlayout");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        dashSpace =  (float) (((w-paddingL-paddingR)*Math.PI)/dashCount);
        initBaseRectF(h, w);
        initProgressRectF(h, w);
    }

    private void initProgressRectF(int h, int w) {
        progressCircle = new RectF();
        progressCircle.set(paddingL, paddingT, w - paddingR, h - paddingB);
    }

    private void initBaseRectF(int h, int w) {
        baseCircle = new RectF();
        baseCircle.set(paddingL, paddingT, w - paddingR, h - paddingB);
    }

    private void initPainters() {
        initBasePainter();
        initProgressPainter();
        initCirclePainter();
        if (isInitCircleProgress){
            initCircleProgressPainter();
        }
        initValueAnimator();
    }

    //初始化
    public void initCirclrProgress(boolean isCircleProgress){
        if(isCircleProgress){
            initCircleProgressPainter();
        }
    }


    private void initCircleProgressPainter() {
        circlrProgressPaint = new Paint();
        circlrProgressPaint.setAntiAlias(true);
        circlrProgressPaint.setStrokeWidth(circleStrokeWidth);
        circlrProgressPaint.setStyle(Paint.Style.STROKE);
        circlrProgressPaint.setColor(progressColor);
    }

    /**
     * 初始化圆环画笔
     */
    private void initCirclePainter() {
        circlrPaint = new Paint();
        circlrPaint.setAntiAlias(true);
        circlrPaint.setStyle(Paint.Style.STROKE);
        circlrPaint.setStrokeWidth(circleStrokeWidth);
        circlrPaint.setColor(dBaserPainterColor);

    }

    /**
     * 初始化进度画笔
     */
    private void initProgressPainter() {
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStrokeWidth(progressStrokeWidth);
        progressPaint.setColor(progressColor);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setPathEffect(new DashPathEffect(new float[]{dashWith,  dashSpace},
                dashSpace));

    }

    /**
     * 初始化 底层 画笔
     */
    private void initBasePainter() {
        basePaint = new Paint();
        basePaint.setAntiAlias(true); //消除锯齿
        basePaint.setStrokeWidth(progressStrokeWidth);
        basePaint.setColor(dBaserPainterColor);
        basePaint.setStyle(Paint.Style.STROKE);//绘制空心圆
        basePaint.setPathEffect(new DashPathEffect(new float[]{dashWith, dashSpace},
                dashSpace));

    }


    /**
     * 进度显示动画效果初始化
     */
    private void initValueAnimator() {
        valueAnimator = new ValueAnimator();
        valueAnimator.setInterpolator(interpolator);
        valueAnimator.addUpdateListener(new ValueAnimatorListenerImp());
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        Log.d(TAG, "onDraw: -------");
        //是否显示圆环
        if (isInnerCircle){
            drawInnerCircle( canvas);
        }
        canvas.drawArc(baseCircle, baseStartAngle, baseFinishAngle, false, basePaint);
        canvas.drawArc(progressCircle, progressStartAngle, plusAngle, false, progressPaint);
        if (isComplete) {
            return;
        }
        invalidate();

    }

    public void initAngle(){
        begin = min;
        this.mValue = 0;
        animateValue();
    }

    private void drawInnerCircle(Canvas canvas) {
        int center = getWidth()/2;
        int innerCircle;
        if (innerCircleWidth != 0 && innerCircleWidth <(getWidth()-paddingL-paddingR-PADDING)/2){
            innerCircle = innerCircleWidth;
        }else{
            innerCircle = (getWidth()-paddingL-paddingR)/2-50;
        }
        canvas.drawCircle(center,center,innerCircle,circlrPaint);

        if(isInitCircleProgress){
            RectF rect = new RectF();
            rect.set(center - innerCircle,center-innerCircle,center+innerCircle,center+innerCircle);
            canvas.drawArc(rect, progressStartAngle, plusAngle, false,circlrProgressPaint);
            if (isHead){
                Paint headPaint = new Paint();
                headPaint.setAntiAlias(true);
                headPaint.setStyle(Paint.Style.FILL);
                headPaint.setColor(progressColor);

                int headBallX = center+(int) (innerCircle *Math.cos((double) (plusAngle+270)/180 *Math.PI));
                int headBallY = center+(int) (innerCircle *Math.sin((double) (plusAngle+270)/180 *Math.PI));
                canvas.drawCircle(headBallX,headBallY,headRadius,headPaint);
            }
        }

    }

    /**
     * 设置进度值 重新开始绘制
     *
     * @param mValue
     */
    public void setmValue(float mValue) {
        if(isContinue){
            if(this.mValue>=max){
                Log.e(TAG,"最大值超出");
                return;
            }
            this.mValue+=mValue;
        }else {
            begin=min;
            this.mValue = mValue;
        }

        //最大值 最小值的限制
        this.mValue=this.mValue>max?max:this.mValue;

        this.mValue=this.mValue<min?min:this.mValue;

        isComplete = false;
        invalidate();
        if (mValue <= max || mValue >= min) {
            animateValue();
        }
    }

    public void setmValue(float mValue,int animationDuration){
        setmValue(mValue);
        duration = animationDuration;
    }


    /**
     * 属性动画开始
     */
    private void animateValue() {
        if (isAnimation){
            if (valueAnimator != null) {
                valueAnimator.setFloatValues(begin, mValue);
                valueAnimator.setDuration(duration);
                valueAnimator.start();

            }
        }else {
            isComplete = true;
            plusAngle = (baseFinishAngle*mValue)/max;
        }
    }

    public void setOnValueChangeListener(OnValueChangeListener valueChangeListener) {
        this.valueChangeListener = valueChangeListener;
    }

    public void setInterpolator(Interpolator interpolator) {
        this.interpolator = interpolator;
        if (valueAnimator != null) {
            valueAnimator.setInterpolator(interpolator);
        }
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;

    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    /**
     * 开启连续模式 每次在原有的进度上增加进度
     * @param b
     */
    public void beginContinue(boolean b) {
        isContinue=b;
    }

    /**
     * 动画开始 更新进度显示
     */
    private class ValueAnimatorListenerImp implements ValueAnimator.AnimatorUpdateListener {
        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            Float value = (Float) valueAnimator.getAnimatedValue();

            plusAngle = (baseFinishAngle * value) / max;

            if (valueChangeListener != null) {
                valueChangeListener.onValueChange(value);
            }

            begin = value;

            if (begin == mValue) {
                isComplete = true;
            }
        }


    }

    public interface OnValueChangeListener {
        void onValueChange(float value);
    }

    public void reset() {
        begin = min;
    }

    public int getProgressColor() {
        return progressColor;
    }

    public void setProgressColor(int progressColor) {
        this.progressColor = progressColor;
    }

    public int getdBaserPainterColor() {
        return dBaserPainterColor;
    }

    public void setdBaserPainterColor(int dBaserPainterColor) {
        this.dBaserPainterColor = dBaserPainterColor;
    }

    public boolean isInitCircleProgress() {
        return isInitCircleProgress;
    }

    public void setInitCircleProgress(boolean initCircleProgress) {
        isInitCircleProgress = initCircleProgress;
    }

    public int getProgressStrokeWidth() {
        return progressStrokeWidth;
    }

    public void setProgressStrokeWidth(int progressStrokeWidth) {

        this.progressStrokeWidth = DensityUtil.dip2px(mContext,progressStrokeWidth);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public boolean isInnerCircle() {
        return isInnerCircle;
    }

    public void setInnerCircle(boolean innerCircle) {
        isInnerCircle = innerCircle;
    }

    public boolean isAnimation() {
        return isAnimation;
    }

    public void setAnimation(boolean animation) {
        isAnimation = animation;
    }

    public boolean isHead() {
        return isHead;
    }

    public void setHead(boolean head) {
        isHead = head;
    }
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        valueAnimator.cancel();
    }
}
