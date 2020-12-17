package com.iutlaval.myapplication.Video;

import android.view.Display;

/**
 * cette class permet d'attendre le temps qui s'écoule entre 2image a l'écran plutot que sur dessiné et gaspillier la batterie
 */
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
     * prend en argument un offset qui permet de reduire l'attente si le systeme a pris beaucoup de temps a génerer une image
     */
    public static void waitFrameTime(long time){
        time = (System.nanoTime()-time)/1000;
        if(time >= frametime)return;
        try {
            Thread.sleep((frametime-time));
        } catch (InterruptedException e) {}
    }
}
