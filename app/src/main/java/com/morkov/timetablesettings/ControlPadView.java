package com.morkov.timetablesettings;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import androidx.annotation.NonNull;

public class ControlPadView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {

    private float centerX;
    private float centerY;
    private float baseRadius;
    private float hatRadius;
    private PadListener padCallback;

    private void setupDimensions() {
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        baseRadius = Math.min(getWidth(), getHeight()) / 4;
        hatRadius = Math.min(getWidth(), getHeight()) / 7;
    }

    public ControlPadView(Context context) {
        super(context);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof PadListener)
            padCallback = (PadListener) context;

    }

    public ControlPadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof PadListener)
            padCallback = (PadListener) context;

    }

    public ControlPadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof PadListener)
            padCallback = (PadListener) context;
    }

    public ControlPadView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getHolder().addCallback(this);
        setOnTouchListener(this);
        if (context instanceof PadListener)
            padCallback = (PadListener) context;
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        setupDimensions();
        setZOrderOnTop(true);
        getHolder().setFormat(PixelFormat.TRANSPARENT);
        drawJoystick(centerX, centerY);
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {

    }

    private void drawJoystick(float newX, float newY) {
        if (getHolder().getSurface().isValid()) {

            Canvas canvas = this.getHolder().lockCanvas();
            Paint colors = new Paint();

            canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

            colors.setARGB(255, 50, 50, 50);
            canvas.drawCircle(centerX, centerY, baseRadius, colors);
            colors.setARGB(255, 255, 0, 0);
            canvas.drawCircle(newX, newY, hatRadius, colors);
            getHolder().unlockCanvasAndPost(canvas);

        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (view.equals(this)) {
            if (motionEvent.getAction() != MotionEvent.ACTION_UP) {

                float displacement = (float) Math.sqrt((Math.pow(motionEvent.getX() - centerX, 2)) + Math.pow(motionEvent.getY() - centerY, 2));
                if (displacement < baseRadius) {
                    drawJoystick(motionEvent.getX(), motionEvent.getY());
                    padCallback.onPadMoved((motionEvent.getX() - centerX) / baseRadius, (motionEvent.getY() - centerY) / baseRadius, getId());
                } else {
                    float ratio = baseRadius / displacement;
                    float constrainedX = centerX + (motionEvent.getX() - centerX) * ratio;
                    float constrainedY = centerY + (motionEvent.getY() - centerY) * ratio;
                    drawJoystick(constrainedX, constrainedY);
                    padCallback.onPadMoved((constrainedX - centerX) / baseRadius, (constrainedY - centerY) / baseRadius, getId());

                }
            } else {
                padCallback.onPadMoved(0, 0, getId());
                drawJoystick(centerX, centerY);
            }
        }
        return true;
    }


    public interface PadListener {
        void onPadMoved(float xPercent, float yPercent, int id);
    }
}
