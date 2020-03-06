/*
 * Copyright (C) 2011 The Android Open Source Project
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
package com.zuomei.widget;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.util.AttributeSet;
import android.view.View;

import com.txsh.R;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is the custom view where all of the Droidflakes are drawn. This
 * class has all of the logic for adding, subtracting, and rendering
 * Droidflakes.
 */
@SuppressLint("NewApi")
public class FlakeView extends View {

    Bitmap droid; // The bitmap that all flakes use
    int numFlakes = 0; // Current number of flakes
    ArrayList<Flake> flakes = new ArrayList<Flake>(); // List of current flakes

    // Animator used to drive all separate flake animations. Rather than have
    // potentially
    // hundreds of separate animators, we just use one and then update all
    // flakes for each
    // frame of that single animation.
    ValueAnimator animator = ValueAnimator.ofFloat(0, 1);
    long startTime, prevTime; // Used to track elapsed time for animations and
    // fps
    int frames = 0; // Used to track frames per second
    //	Paint textPaint; // Used for rendering fps text
    float fps = 0; // frames per second
    Matrix m = new Matrix(); // Matrix used to translate/rotate each flake
    // during rendering
    String fpsString = "";
    String numFlakesString = "";

    Context mContext;

    /**
     * Constructor. Create objects used throughout the life of the View: the
     * Paint and the animator
     */
    public FlakeView(Context context) {
        super(context);
        mContext = context;
        init();
    }


    public FlakeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }


    public FlakeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }


    int getNumFlakes() {
        return numFlakes;
    }

    private void init() {
        droid = BitmapFactory.decodeResource(getResources(), R.drawable.flake_money);
    /*	textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		textPaint.setColor(Color.WHITE);
		textPaint.setTextSize(24);
*/
        // This listener is where the action is for the flak animations. Every
        // frame of the
        // animation, we calculate the elapsed time and update every flake's
        // position and rotation
        // according to its speed.
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                long nowTime = System.currentTimeMillis();
                float secs = (float) (nowTime - prevTime) / 1000f;
                prevTime = nowTime;
                for (int i = 0; i < numFlakes; ++i) {
                    Flake flake = flakes.get(i);
                    flake.y += (flake.speed * secs);
                    if (flake.y > getHeight()) {
                        // If a flake falls off the bottom, send it back to the
                        // top
                        flake.y = 0 - flake.height;
                    }
                    flake.rotation = flake.rotation + (flake.rotationSpeed * secs);
                }
                // Force a redraw to see the flakes in their new positions and
                // orientations
                invalidate();
            }
        });

        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setDuration(3000);
    }

    private void setNumFlakes(int quantity) {
        numFlakes = quantity;
        numFlakesString = "numFlakes: " + numFlakes;
    }

    /**
     * Add the specified number of droidflakes.
     */
    public void addFlakes(int quantity) {
        for (int i = 0; i < quantity; ++i) {
            flakes.add(Flake.createFlake(getWidth(), droid));
        }
        setNumFlakes(numFlakes + quantity);
        initSound();
    }

    /**
     * Subtract the specified number of droidflakes. We just take them off the
     * end of the list, leaving the others unchanged.
     */
    void subtractFlakes(int quantity) {
        for (int i = 0; i < quantity; ++i) {
            int index = numFlakes - i - 1;
            flakes.remove(index);
        }
        setNumFlakes(numFlakes - quantity);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // Reset list of droidflakes, then restart it with 8 flakes
        flakes.clear();
        numFlakes = 0;
        //addFlakes(17);
        // Cancel animator in case it was already running
        animator.cancel();
        // Set up fps tracking and start the animation
        startTime = System.currentTimeMillis();
        prevTime = startTime;
        frames = 0;
        animator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // For each flake: back-translate by half its size (this allows it to
        // rotate around its center),
        // rotate by its current rotation, translate by its location, then draw
        // its bitmap
        for (int i = 0; i < numFlakes; ++i) {
            Flake flake = flakes.get(i);
            m.setTranslate(-flake.width / 2, -flake.height / 2);
            m.postRotate(flake.rotation);
            m.postTranslate(flake.width / 2 + flake.x, flake.height / 2 + flake.y);
            canvas.drawBitmap(flake.bitmap, m, null);
        }
        // fps counter: count how many frames we draw and once a second
        // calculate the
        // frames per second
        ++frames;
        long nowTime = System.currentTimeMillis();
        long deltaTime = nowTime - startTime;
        if (deltaTime > 1000) {
            float secs = (float) deltaTime / 1000f;
            fps = (float) frames / secs;
            fpsString = "fps: " + fps;
            startTime = nowTime;
            frames = 0;
        }
	/*	canvas.drawText(numFlakesString, getWidth() - 200, getHeight() - 50, textPaint);
		canvas.drawText(fpsString, getWidth() - 200, getHeight() - 80, textPaint);*/
    }

    public void pause() {
        // Make sure the animator's not spinning in the background when the
        // activity is paused.
        animator.cancel();
    }

    public void resume() {
        animator.start();
    }


    private MediaPlayer mediaPlayer;
    boolean createState = false;

    private void initSound() {

        if (mediaPlayer == null) {
            mediaPlayer = createLocalMp3();
            createState = true;
        }
        //当播放完音频资源时，会触发onCompletion事件，可以在该事件中释放音频资源，
        //以便其他应用程序可以使用该资源:
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();//释放音频资源
            }
        });
        try {
            //在播放音频资源之前，必须调用Prepare方法完成些准备工作
            if (createState) mediaPlayer.prepare();

            mediaPlayer.setVolume(1, 1);
            //设置循环播放
            //	mediaPlayer.setLooping(true);
            //开始播放音频
            mediaPlayer.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public MediaPlayer createLocalMp3() {
        /**
         * 创建音频文件的方法：
         * 1、播放资源目录的文件：MediaPlayer.create(MainActivity.this,R.raw.beatit);//播放res/raw 资源目录下的MP3文件
         * 2:播放sdcard卡的文件：mediaPlayer=new MediaPlayer();
         *   mediaPlayer.setDataSource("/sdcard/beatit.mp3");//前提是sdcard卡要先导入音频文件
         */
        MediaPlayer mp = MediaPlayer.create(mContext, R.raw.diaoluo_xiao);
        mp.stop();
        return mp;
    }

}
