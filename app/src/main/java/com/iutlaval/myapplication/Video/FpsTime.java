package com.iutlaval.myapplication.Video;

import android.view.Display;

public class FpsTime {
    private static long frametime;

    public static void init(Display d)
    {
        float fps = d.getRefreshRate();
        frametime = (long)(1.0F/fps*1000);
    }

    public static void waitFrameTime(){
        try {
            Thread.sleep((frametime));
        } catch (InterruptedException e) {}
    }
}
