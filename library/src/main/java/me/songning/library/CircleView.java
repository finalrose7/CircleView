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

    private int circleColor;
    private int textColor;
    private float textSize;
    private String text;
    private boolean randomColor;
    private boolean singleText;
    private int textOrientation;
    private float angle;
    private int rotateOrientation;
    private int speed;

    private ValueAnimator animator;
    private float rotateValues;
    private int[] colors;
    private boolean isShow = true;

    private Paint circlePaint;
    private Paint verticalTextPaint;

    private StaticLayout staticLayout;
    private TextPaint horizontalTextPaint;

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
        circleColor = array.getColor(R.styleable.CircleView_circleColor, DEFAULT_CIRCLE_COLOR);
        textColor = array.getColor(R.styleable.CircleView_textColor, DEFAULT_TEXT_COLOR);
        textSize = array.getDimension(R.styleable.CircleView_textSize, DEFAULT_TEXT_SIZE);
        text = array.getString(R.styleable.CircleView_text);
        singleText = array.getBoolean(R.styleable.CircleView_singleText, false);
        randomColor = array.getBoolean(R.styleable.CircleView_randomColor, false);
        textOrientation = array.getInt(R.styleable.CircleView_textOrientation, TEXT_VERTICAL);
        rotateOrientation = array.getInt(R.styleable.CircleView_rotateOrientation, ROTATE_CLOCKWISE);
        angle = array.getFloat(R.styleable.CircleView_textAngle, DEFAULT_ANGLE);
        speed = array.getInteger(R.styleable.CircleView_speed, DEFAULT_SPEED);
        array.recycle();

        init();
    }

    private void init() {

        //Make the random color initialization here.There are material design colors.
        colors = new int[]{
                0xFFF44336, 0xFFE91E63, 0xFF9C27B0, 0xFF673AB7, 0xFF3F51B5, 0xFF2196F3,
                0xFF03A9F4, 0xFF00BCD4, 0xFF009688, 0xFF4CAF50, 0xFF8BC34A, 0xFFCDDC39,
                0xFFFFEB3B, 0xFFFFC107, 0xFFFF9800, 0xFFFF5722, 0xFF795548, 0xFF9E9E9E,
                0xFF607D8B};

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setDither(true);

        if (randomColor) {
            int random = new Random().nextInt(colors.length);
            circleColor = colors[random];
        }

        circlePaint.setColor(circleColor);

        verticalTextPaint = new Paint();
        verticalTextPaint.setAntiAlias(true);
        verticalTextPaint.setDither(true);
        verticalTextPaint.setTextSize(textSize);
        verticalTextPaint.setColor(textColor);

        if (TextUtils.isEmpty(text)) {
            text = "";
        }

        horizontalTextPaint = new TextPaint(verticalTextPaint);
        staticLayout = new StaticLayout(text, horizontalTextPaint, 1, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
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

        float centerX = paddingLeft + (width * 1f) / 2;
        float centerY = paddingTop + (height * 1f) / 2;

        float radius = (Math.min(width, height) * 1f) / 2;
        canvas.drawCircle(centerX, centerY, radius, circlePaint);

        if (TextUtils.isEmpty(text))
            return;

        canvas.rotate(angle, centerX, centerY);
        drawText(canvas, centerX, centerY);
    }

    private void drawText(Canvas canvas, float centerX, float centerY) {

        if (singleText) {
            String singleText = getFirstText(text);
            drawVerticalText(canvas, centerX, centerY, singleText);
            return;
        }

        if (textOrientation == TEXT_VERTICAL) {
            drawVerticalText(canvas, centerX, centerY, text);
        } else if (textOrientation == TEXT_HORIZONTAL) {
            drawHorizontalText(canvas, centerX, centerY);
        }

    }

    private void drawHorizontalText(Canvas canvas, float centerX, float centerY) {
        canvas.translate(centerX, centerY - (staticLayout.getHeight() * 1f) / 2);
        staticLayout.draw(canvas);
    }

    private void drawVerticalText(Canvas canvas, float centerX, float centerY, String text) {
        Paint.FontMetrics fontMetrics = verticalTextPaint.getFontMetrics();
        float baseLine = -(fontMetrics.ascent + fontMetrics.descent) / 2;
        float textWidth = verticalTextPaint.measureText(text);
        float startX = centerX - textWidth / 2;
        float endY = centerY + baseLine;
        canvas.drawText(text, startX, endY, verticalTextPaint);
    }

    public void startRotateAnimation() {
        if (animator == null) {
            initRotateAnimation();
        }
        if (!animator.isRunning()) {
            animator.start();
        }
    }

    private void initRotateAnimation() {
        if (rotateOrientation == ROTATE_CLOCKWISE) {
            rotateValues = 360f;
        } else if (rotateOrientation == ROTATE_ANTICLOCKWISE) {
            rotateValues = -360f;
        }
        animator = ValueAnimator.ofFloat(0f, rotateValues);
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(speed);
        animator.addUpdateListener(this);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        angle = (float) animation.getAnimatedValue();
        invalidate();
    }

    public void stopRotateAnimation() {
        if (animator != null && animator.isRunning()) {
            animator.cancel();
        }
    }

    public void toggleRotateOrientation() {
        if (animator == null || !animator.isRunning())
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
        rotateOrientation = orientation;
        if (animator != null && animator.isRunning()) {
            animator.removeUpdateListener(this);
            animator.cancel();
            initRotateAnimation();
            animator.start();
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
        speed = speedValues;
        if (animator != null) {
            animator.setDuration(speedValues);
        }
    }

    public int getRotateSpeed() {
        return speed;
    }

    public int getRotateOrientation() {
        int result = ROTATE_CLOCKWISE;
        if (rotateValues == -360f) {
            result = ROTATE_ANTICLOCKWISE;
        }
        return result;
    }


    public void setCircleColor(int circleColor) {
        this.circleColor = circleColor;
        circlePaint.setColor(this.circleColor);
        invalidate();
    }

    public int getCircleColor() {
        return circleColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        verticalTextPaint.setColor(this.textColor);
        horizontalTextPaint.setColor(this.textColor);
        invalidate();
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextSize(float textSize) {
        this.textSize = sp2px(getContext(), textSize);
        verticalTextPaint.setTextSize(this.textSize);
        horizontalTextPaint.setTextSize(this.textSize);
        invalidate();
    }

    public float getTextSize() {
        return textSize;
    }

    public void setText(int id) {
        setText(getContext().getText(id).toString());
    }

    public void setText(String text) {
        if (TextUtils.isEmpty(text))
            return;
        if (singleText) {
            text = getFirstText(text);
        }
        this.text = text;
        invalidate();
    }

    public String getText() {
        return text;
    }

    /**
     * Set use or not random circle view background color.
     *
     * @param randomColor True or false.
     */
    public void setRandomColor(boolean randomColor) {
        this.randomColor = randomColor;
        if (this.randomColor) {
            int random = new Random().nextInt(colors.length);
            circleColor = colors[random];
        }
        circlePaint.setColor(circleColor);
        invalidate();
    }

    /**
     * Set the single text in circle view.If true,no matter how long the text,just display a text.
     *
     * @param singleText True or false.
     */
    public void setSingleText(boolean singleText) {
        this.singleText = singleText;
        invalidate();
    }

    /**
     * Set the text arbitrary angle in circle view .
     *
     * @param angle Float values of the angle.
     */
    public void setAngle(float angle) {
        this.angle = angle;
        invalidate();
    }

    public float getAngle() {
        return angle;
    }

    /**
     * Set the direction of the text,vertical or horizontal.
     *
     * @param orientation TEXT_VERTICAL or TEXT_HORIZONTAL.
     */
    public void setTextOrientation(int orientation) {
        textOrientation = orientation;
        staticLayout = null;
        staticLayout = new StaticLayout(text, horizontalTextPaint, 1, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, false);
        invalidate();
    }

    public String getTextOrientation() {
        if (animator != null && animator.isRunning()) {
            return "rotate";
        }
        if (textOrientation == TEXT_VERTICAL) {
            return "vertical";
        } else if (textOrientation == TEXT_HORIZONTAL) {
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
        if (animator != null) {
            animator.removeUpdateListener(this);
            animator.end();
            animator = null;
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
