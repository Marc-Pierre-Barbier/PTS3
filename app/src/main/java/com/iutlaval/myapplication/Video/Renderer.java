package com.iutlaval.myapplication.Video;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.iutlaval.myapplication.Game.GameLogicThread;

import java.util.ArrayList;
import java.util.List;

public class Renderer extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private Paint p;
    private DrawingThread drawingThread;
    private GameLogicThread engine;


    private List<Drawable> toDraw;

    public Renderer(Context context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);

        drawingThread = new DrawingThread();

        p = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL_AND_STROKE);

        toDraw = new ArrayList<>();
    }

    /**
     * cette fonction demare le thread graphique
     * @param holder
     */
    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        this.holder=holder;
        if(drawingThread.getState() == Thread.State.TERMINATED)
        {
            drawingThread = new DrawingThread();
        }
        if(!drawingThread.isAlive()){
            drawingThread.start();
        }
    }

    /**
     * cette fonction est appeler a chaque fois que l'utilisateur tourne son ecran
     * this function is unused since the screen is locked
     * @param surfaceHolder
     * @param i
     * @param i1
     * @param i2
     */
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    /**
     * on surface destruction stop the thread
     * la surface ce fait detruire aussi quand le jeu est en pause
     * TODO kill proprement le thread et ajouter une relance sur le GameActivity
     * @param surfaceHolder
     */
    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        drawingThread.keepDrawing = false;
        try {
            drawingThread.join();
        } catch (InterruptedException e) {}
    }

    /**
     * function ran for each frame to draw all the drawables
     * @param canvas canevas on wich to draw on
     */
    @Override
    protected void onDraw(Canvas canvas) {
        //le to Array patch les crash
        for(Drawable d : toDraw.toArray(new Drawable[0]))
        {
            d.drawOn(canvas,p);
        }
    }

    /**
     * main drawing thread render each frame
     */
    private class DrawingThread extends Thread {

        public boolean keepDrawing = true;

        @SuppressLint("WrongCall")
        @Override
        public void run() {
            while (keepDrawing) {
                Canvas canvas = null;

                long time = System.nanoTime();

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
                //sauve de la baterie
                FpsTime.waitFrameTime(time);
                if(engine != null && engine.isReady())engine.onFrameDoneRendering();
            }
        }
    }

    /**
     * permet d'ajouter un element a afficher
     * @return retourne faux si l'element existe deja
     */
    public boolean addToDraw(Drawable newElement){
        if(newElement == null)return false;
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
    public void moveToDraw(float x, float y, String name) throws DrawableNotFoundException {
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
     * supprime le drawable possedant le nom indique
     * @param name le nom
     */
    public void removeToDraw(String name){
        Drawable d = getDrawAble(name);
        if(d != null)
            toDraw.remove(d);
    }
    /**
     * remove the element from the draw list
     * @param toRemove the element toRemove
     */
    public void removeToDraw(Drawable toRemove){
        toRemove.onDeletion(this);
        toDraw.remove(toRemove);
    }


    /**
     * retourne le drawable possedant le nom indiqué
     * @param name le nom du drawable
     * @return
     */
    public Drawable getDrawAble(String name){
        Drawable drawable = null;
        for(Drawable d : toDraw)
        {
            if(d.getName().equals(name))
            {
                drawable=d;
                break;
            }
        }
        return drawable;
    }

    public void setEngine(GameLogicThread engine) {
        this.engine = engine;
    }
}
