package com.iutlaval.myapplication.Video;

import android.view.Display;

public class FpsTime {
    private static long frametime;

    /**
     * initialise le frame time pour pouvoir utliser le delais
     * @param d ecran
     */
    public static void init(Display d)
    {
        float fps = d.getRefreshRate();
        frametime = (long)(1.0F/fps*1000);
    }

    /**
     * atten exactement la durée d'une image a l'ecran a la vitesse optimal de l'ecran
     */
    public static void waitFrameTime(long time){
        time = (System.nanoTime()-time)/1000;
        if(time >= frametime)return;
        try {
            Thread.sleep((frametime-time));
        } catch (InterruptedException e) {}
    }

    public static void waitFrameTime(){
        try {
            Thread.sleep((frametime));
        } catch (InterruptedException e) {}
    }
}
