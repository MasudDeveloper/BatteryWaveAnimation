package com.mrdeveloper.batterywaveanimation;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;

public class BatteryLevelView extends View {

    private Paint wavePaint;
    private Path wavePath;
    private float waveOffset = 0f; // ঢেউয়ের চলমান অবস্থান
    private int batteryLevel = 50; // ডিফল্ট ব্যাটারি লেভেল
    private Handler handler;
    private int waveAmplitude = 10; // ঢেউয়ের উচ্চতা
    private int waveFrequency = 200; // ঢেউয়ের ফ্রিকোয়েন্সি (ঘনত্ব)
    private float waveSpeed = 1f; // ঢেউ চলার গতি

    public BatteryLevelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        // ঢেউয়ের পেইন্ট সেটআপ
        wavePaint = new Paint();
        wavePaint.setColor(Color.parseColor("#4CAF50")); // ঢেউয়ের রঙ
        wavePaint.setStyle(Paint.Style.FILL);
        wavePaint.setAntiAlias(true); // ঢেউকে স্মুথ করতে

        wavePath = new Path();

        // ঢেউ চলমান রাখার জন্য হ্যান্ডলার
        handler = new Handler();
        handler.post(updateWaveRunnable);
    }

    public void setBatteryLevel(int level) {
        batteryLevel = level;
        if (batteryLevel > 50) {
            wavePaint.setColor(Color.parseColor("#4CAF50")); // সবুজ
        } else if (batteryLevel > 20) {
            wavePaint.setColor(Color.parseColor("#FFC107")); // হলুদ
        } else {
            wavePaint.setColor(Color.parseColor("#F44336")); // লাল
        }
        invalidate();
    }

    private Runnable updateWaveRunnable = new Runnable() {
        @Override
        public void run() {
            waveOffset += waveSpeed; // ঢেউ এগোনোর গতি
            if (waveOffset > getWidth()) waveOffset = 0f;
            invalidate();
            handler.postDelayed(this, 16); // প্রতি ১৬ms-এ রিফ্রেশ (৬০ FPS)
        }
    };

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        // ব্যাটারি লেভেলের উচ্চতা নির্ধারণ
        float levelHeight = height * (batteryLevel / 100f);

        // সাইন ওয়েভের পথ তৈরি
        wavePath.reset();
        wavePath.moveTo(0, height);

        for (int x = 0; x <= width; x++) {
            float y = (float) (height - levelHeight - waveAmplitude * Math.sin((x + waveOffset) * 2 * Math.PI / waveFrequency));
            wavePath.lineTo(x, y);
        }

        wavePath.lineTo(width, height);
        wavePath.lineTo(0, height);
        wavePath.close();

        // ঢেউ আঁকা
        canvas.drawPath(wavePath, wavePaint);
    }

}

