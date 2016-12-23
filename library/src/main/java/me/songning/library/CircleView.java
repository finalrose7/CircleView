/*
 * Copyright (C) 2016 SongNing. https://github.com/backkomyoung
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package me.songning.library;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.Random;

/**
 * A colorful circle view with text.
 *
 * @author SongNing
 */

public class CircleView extends View implements ValueAnimator.AnimatorUpdateListener {

    /**
     * Show the text in the vertical direction.
     */
    public static final int TEXT_VERTICAL = 1;

    /**
     * Show the text in the horizontal direction.
     */
    public static final int TEXT_HORIZONTAL = 0;

    /**
     * Make the rotating animation clockwise rotation.
     */
    public static final int ROTATE_CLOCKWISE = 2;

    /**
     * Make the rotating animation anticlockwise rotation.
     */
    public static final int ROTATE_ANTICLOCKWISE = 3;

    private final int DEFAULT_CIRCLE_SIZE;

    private int mCircleColor;
    private int mTextColor;
    private float mTextSize;
    private String mText;
    private boolean mRandomColor;
    private boolean mSingleText;
    private int mTextOrientation;
    private float mAngle;
    private int mRotateOrientation;
    private int mSpeed;

    private ValueAnimator mAnimator;
    private float mRotateValues;
    private int[] mColors;
    private boolean isShow = true;

    private Paint mCirclePaint;
    private Paint mVerticalTextPaint;

    private StaticLayout mStaticLayout;
    private TextPaint mHorizontalTextPaint;

    public CircleView(Context context) {
        this(context, null);
    }

    public CircleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        DEFAULT_CIRCLE_SIZE = dp2px(context, 48);
        final int DEFAULT_TEXT_SIZE = sp2px(context, 16);
        final int DEFAULT_SPEED = 4000;
        final int DEFAULT_CIRCLE_COLOR = 0xFF03A9F4;
        final int DEFAULT_TEXT_COLOR = 0xFFFFFFFF;
        final float DEFAULT_ANGLE = 0f;

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CircleView, defStyleAttr, 0);
        mCircleColor = array.getColor(R.styleable.CircleView_circleColor, DEFAULT_CIRCLE_COLOR);
        mTextColor = array.getColor(R.styleable.CircleView_textColor, DEFAULT_TEXT_COLOR);
        mTextSize = array.getDimension(R.styleable.CircleView_textSize, DEFAULT_TEXT_SIZE);
        mText = array.getString(R.styleable.CircleView_text);
        mSingleText = array.getBoolean(R.styleable.CircleView_singleText, false);
        mRandomColor = array.getBoolean(R.styleable.CircleView_randomColor, false);
        mTextOrientation = array.getInt(R.styleable.CircleView_textOrientation, TEXT_VERTICAL);
        mRotateOrientation = array.getInt(R.styleable.CircleView_rotateOrientation, ROTATE_CLOCKWISE);
        mAngle = array.getFloat(R.styleable.CircleView_textAngle, DEFAULT_ANGLE);
        mSpeed = array.getInteger(R.styleable.CircleView_speed, DEFAULT_SPEED);
        array.recycle();
        init();
    }

    private void init() {

        //Make the random color initialization here.There are material design colors.
        mColors = new int[]{
                0xFFF44336, 0xFFE91E63, 0xFF9C27B0, 0xFF673AB7, 0xFF3F51B5, 0xFF2196F3,
                0xFF03A9F4, 0xFF00BCD4, 0xFF009688, 0xFF4CAF50, 0xFF8BC34A, 0xFFCDDC39,
                0xFFFFEB3B, 0xFFFFC107, 0xFFFF9800, 0xFFFF5722, 0xFF795548, 0xFF9E9E9E,
                0xFF607D8B};

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setDither(true);

        if (mRandomColor) {
            int random = new Random().nextInt(mColors.length);
            mCircleColor = mColors[random];
        }

        mCirclePaint.setColor(mCircleColor);

        mVerticalTextPaint = new Paint();
        mVerticalTextPaint.setAntiAlias(true);
        mVerticalTextPaint.setDither(true);
        mVerticalTextPaint.setTextSize(mTextSize);
        mVerticalTextPaint.setColor(mTextColor);

        if (TextUtils.isEmpty(mText)) {
            mText = "";
        }

        mHorizontalTextPaint = new TextPaint(mVerticalTextPaint);
        mStaticLayout = new StaticLayout(mText, mHorizontalTextPaint, 1, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int width = measureSpec(widthMeasureSpec);
        final int height = measureSpec(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureSpec(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(DEFAULT_CIRCLE_SIZE, specSize);
        } else {
            result = DEFAULT_CIRCLE_SIZE;
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        float centerX = paddingLeft + width / 2;
        float centerY = paddingTop + height / 2;

        float radius = Math.min(width, height) / 2;
        canvas.drawCircle(centerX, centerY, radius, mCirclePaint);

        if (TextUtils.isEmpty(mText))
            return;

        canvas.rotate(mAngle, centerX, centerY);
        drawText(canvas, centerX, centerY);
    }

    private void drawText(Canvas canvas, float centerX, float centerY) {

        if (mSingleText) {
            String singleText = getFirstText(mText);
            drawVerticalText(canvas, centerX, centerY, singleText);
            return;
        }

        if (mTextOrientation == TEXT_VERTICAL) {
            drawVerticalText(canvas, centerX, centerY, mText);
        } else if (mTextOrientation == TEXT_HORIZONTAL) {
            drawHorizontalText(canvas, centerX, centerY);
        }

    }

    private void drawHorizontalText(Canvas canvas, float centerX, float centerY) {
        canvas.translate(centerX, centerY - mStaticLayout.getHeight() / 2);
        mStaticLayout.draw(canvas);
    }

    private void drawVerticalText(Canvas canvas, float centerX, float centerY, String text) {
        Paint.FontMetrics fontMetrics = mVerticalTextPaint.getFontMetrics();
        float baseLine = -(fontMetrics.ascent + fontMetrics.descent) / 2;
        float textWidth = mVerticalTextPaint.measureText(text);
        float startX = centerX - textWidth / 2;
        float endY = centerY + baseLine;
        canvas.drawText(text, startX, endY, mVerticalTextPaint);
    }

    public void startRotateAnimation() {
        if (mAnimator == null) {
            initRotateAnimation();
        }
        if (!mAnimator.isRunning()) {
            mAnimator.start();
        }
    }

    private void initRotateAnimation() {
        if (mRotateOrientation == ROTATE_CLOCKWISE) {
            mRotateValues = 360f;
        } else if (mRotateOrientation == ROTATE_ANTICLOCKWISE) {
            mRotateValues = -360f;
        }
        mAnimator = ValueAnimator.ofFloat(0f, mRotateValues);
        mAnimator.setInterpolator(new LinearInterpolator());
        mAnimator.setDuration(mSpeed);
        mAnimator.addUpdateListener(this);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        mAngle = (float) animation.getAnimatedValue();
        invalidate();
    }

    public void stopRotateAnimation() {
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.cancel();
        }
    }

    public void toggleRotateOrientation() {
        if (mAnimator == null || !mAnimator.isRunning())
            return;
        int state = getRotateOrientation();
        if (state == ROTATE_CLOCKWISE) {
            setRotateOrientation(ROTATE_ANTICLOCKWISE);
        } else if (state == ROTATE_ANTICLOCKWISE) {
            setRotateOrientation(ROTATE_CLOCKWISE);
        }
    }

    /**
     * Set rotation direction of the rotate animation,clockwise or anti-clockwise.
     *
     * @param orientation ROTATE_CLOCKWISE or ROTATE_ANTICLOCKWISE.
     */
    public void setRotateOrientation(int orientation) {
        mRotateOrientation = orientation;
        if (mAnimator != null && mAnimator.isRunning()) {
            mAnimator.removeUpdateListener(this);
            mAnimator.cancel();
            initRotateAnimation();
            mAnimator.start();
        }
    }

    /**
     * If it can rotate, set the rotation speed.
     *
     * @param speedValues Speed values.
     */
    public void setRotateSpeed(int speedValues) {
        if (speedValues < 0)
            throw new IllegalArgumentException("The speed values must be greater than 0.");
        mSpeed = speedValues;
        if (mAnimator != null) {
            mAnimator.setDuration(speedValues);
        }
    }

    public int getRotateSpeed() {
        return mSpeed;
    }

    public int getRotateOrientation() {
        int result = ROTATE_CLOCKWISE;
        if (mRotateValues == 360f) {
            result = ROTATE_CLOCKWISE;
        } else if (mRotateValues == -360f) {
            result = ROTATE_ANTICLOCKWISE;
        }
        return result;
    }


    public void setCircleColor(int circleColor) {
        mCircleColor = circleColor;
        mCirclePaint.setColor(mCircleColor);
        invalidate();
    }

    public int getCircleColor() {
        return mCircleColor;
    }

    public void setTextColor(int textColor) {
        mTextColor = textColor;
        mVerticalTextPaint.setColor(mTextColor);
        mHorizontalTextPaint.setColor(mTextColor);
        invalidate();
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextSize(float textSize) {
        mTextSize = sp2px(getContext(), textSize);
        mVerticalTextPaint.setTextSize(mTextSize);
        mHorizontalTextPaint.setTextSize(mTextSize);
        invalidate();
    }

    public float getTextSize() {
        return mTextSize;
    }

    public void setText(int id) {
        setText(getContext().getText(id).toString());
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(text))
            return;
        if (mSingleText) {
            text = getFirstText(text);
        }
        mText = text;
        invalidate();
    }

    public String getText() {
        return mText;
    }

    /**
     * Set use or not random circle view background color.
     *
     * @param randomColor True or false.
     */
    public void setRandomColor(boolean randomColor) {
        mRandomColor = randomColor;
        if (mRandomColor) {
            int random = new Random().nextInt(mColors.length);
            mCircleColor = mColors[random];
        }
        mCirclePaint.setColor(mCircleColor);
        invalidate();
    }

    /**
     * Set the single text in circle view.If true,no matter how long the text,just display a text.
     *
     * @param singleText True or false.
     */
    public void setSingleText(boolean singleText) {
        mSingleText = singleText;
        invalidate();
    }

    /**
     * Set the text arbitrary angle in circle view .
     *
     * @param angle Float values of the angle.
     */
    public void setAngle(float angle) {
        mAngle = angle;
        invalidate();
    }

    public float getAngle() {
        return mAngle;
    }

    /**
     * Set the direction of the text,vertical or horizontal.
     *
     * @param orientation TEXT_VERTICAL or TEXT_HORIZONTAL.
     */
    public void setTextOrientation(int orientation) {
        mTextOrientation = orientation;
        mStaticLayout = null;
        mStaticLayout = new StaticLayout(mText, mHorizontalTextPaint, 1, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        invalidate();
    }

    public String getTextOrientation() {
        if (mAnimator != null && mAnimator.isRunning()) {
            return "rotate";
        }
        if (mTextOrientation == TEXT_VERTICAL) {
            return "vertical";
        } else if (mTextOrientation == TEXT_HORIZONTAL) {
            return "horizontal";
        }
        return null;
    }

    public void toggleShow() {
        if (isShow) {
            hide();
        } else {
            show();
        }
    }

    /**
     * Show this view with the scale Animation.
     */
    public void show() {
        if (!isShow) {
            scaleAnimation(0.0f, 1.0f, 200);
            isShow = true;
        }
    }

    /**
     * Hide this view with the scale Animation.
     */
    public void hide() {
        if (isShow) {
            scaleAnimation(1.0f, 0.0f, 200);
            isShow = false;
        }
    }

    /**
     * Show or hide this view,like the FloatingActionButton.show() and FloatingActionButton.hide().
     * It is depending on the param "start" and "end".
     *
     * @param start    The float animated value to control show or hide.
     * @param end      The float animated value to control show or hide.
     * @param duration Animation duration.
     */
    private void scaleAnimation(float start, float end, long duration) {
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(this, "scaleX", start, end);
        ObjectAnimator animatorY = ObjectAnimator.ofFloat(this, "scaleY", start, end);
        animatorX.setInterpolator(new LinearInterpolator());
        animatorY.setInterpolator(new LinearInterpolator());
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(duration);
        animatorSet.playTogether(animatorX, animatorY);
        animatorSet.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAnimator != null) {
            mAnimator.removeUpdateListener(this);
            mAnimator.end();
            mAnimator = null;
        }
    }

    /**
     * Get the first character.
     *
     * @param str A string.
     * @return The first character.
     */
    private String getFirstText(String str) {
        char first = str.charAt(0);
        if (Character.isLetter(first)) {
            return String.valueOf(Character.toUpperCase(first));
        } else {
            return String.valueOf(first);
        }
    }

    private int dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    private int sp2px(Context context, float sp) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * fontScale + 0.5f);
    }

}
