package com.txsh.utils;

import cn.finalteam.galleryfinal.PauseOnScrollListener;
import com.bumptech.glide.Glide;

/**
 * Desction:
 * Author:pengjianbo
 * Date:2016/1/9 0009 18:18
 */
public class GlidePauseOnScrollListener extends PauseOnScrollListener {

    public GlidePauseOnScrollListener(boolean pauseOnScroll, boolean pauseOnFling) {
        super(pauseOnScroll, pauseOnFling);
    }

    @Override
    public void resume() {
        Glide.with(getActivity()).resumeRequests();
    }

    @Override
    public void pause() {
        Glide.with(getActivity()).pauseRequests();
    }
}
