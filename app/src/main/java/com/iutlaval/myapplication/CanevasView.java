package com.iutlaval.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CanevasView extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private Paint p;
    private DrawingThread drawingThread;


    private List<Drawable> toDraw;
    private Rect fullscreen;

    public CanevasView(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);

        drawingThread = new DrawingThread();

        p = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL_AND_STROKE);

        toDraw = new ArrayList<>();
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        fullscreen = holder.getSurfaceFrame();
        drawingThread.start();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        drawingThread.keepDrawing = false;
        try {
            drawingThread.join();
        } catch (InterruptedException e) {}
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(Drawable d : toDraw)
        {
            d.drawOn(canvas,p);
        }
    }

    private class DrawingThread extends Thread {

        public boolean keepDrawing = true;

        @SuppressLint("WrongCall")
        @Override
        public void run() {
            while (keepDrawing) {
                Canvas canvas = null;

                try {
                    // On récupère le canvas pour dessiner dessus
                    canvas = holder.lockCanvas();
                    // On s'assure qu'aucun autre thread n'accède au holder
                    synchronized (holder) {
                        // Et on dessine
                        onDraw(canvas);
                    }
                } finally {
                    // Notre dessin fini, on relâche le Canvas pour que le dessin s'affiche
                    if (canvas != null)
                        holder.unlockCanvasAndPost(canvas);
                }

                //empecher d'aller plus vite que le taux de rafraichissement de l'ecran
                //sauve de la betterie
                try {
                    float fps = getDisplay().getRefreshRate();
                    Thread.sleep((long)(1.0/fps*1000));
                } catch (InterruptedException e) {}
            }
        }
    }

    /**
     * permet d'ajouter un element a afficher
     * @return retourne faux si l'element existe deja
     */
    public boolean addToDraw(Drawable newElement){
        for(Drawable d : toDraw)
        {
            if(d.equals(newElement))
            {
                return false;
            }
        }
        toDraw.add(newElement);
        return true;
    }

    /**
     * permet de changer les coordoné d'un objet a l'ecran
     * @param x coordonné en x (-1.0 a 1.0)
     * @param y coodonné en y (-1.0 a 1.0)
     * @param name nom de l'element a modifier
     */
    public void ChangeToDraw(float x,float y,String name) throws DrawableNotFoundException {
        for(Drawable d : toDraw)
        {
            if(d.getName().equals(name))
            {
                d.setCoordinates(x,y);
                return;
            }
        }
        throw new DrawableNotFoundException();
    }

    /**
     * remove the element from the draw list
     * @param name the element name
     */
    public void removeToDraw(String name){
        Drawable toRemove = null;
        for(Drawable d : toDraw)
        {
            if(d.getName().equals(name))
            {
                toRemove=d;
                break;
            }
        }
        if(toRemove != null)
        {
            toDraw.remove(toRemove);
        }
    }

    /**
     * remove the element from the draw list
     * @param toRemove the element toRemove
     */
    public void removeToDraw(Drawable toRemove){
        toDraw.remove(toRemove);
    }

    public Rect getFullscreen() {
        return fullscreen;
    }
}
