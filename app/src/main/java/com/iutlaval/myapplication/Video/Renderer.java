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
import com.iutlaval.myapplication.GameActivity;
import com.iutlaval.myapplication.Video.Drawables.Drawable;
import com.iutlaval.myapplication.Video.Drawables.DrawableCard;
import com.iutlaval.myapplication.Video.Drawables.DrawableSelfRemoving;

import java.util.ArrayList;
import java.util.List;

public class Renderer extends SurfaceView implements SurfaceHolder.Callback {

    private SurfaceHolder holder;
    private Paint p;
    private DrawingThread drawingThread;
    private GameLogicThread engine;
    private GameActivity gameActivity;
    private boolean needToUpdate;

    private List<Drawable> toDraw;

    public Renderer(Context context, GameActivity gameActivity) {
        super(context);
        this.gameActivity=gameActivity;

        holder = getHolder();
        holder.addCallback(this);

        drawingThread = new DrawingThread(this);

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
            drawingThread = new DrawingThread(this);
        }
        if(!drawingThread.isAlive()){
            drawingThread.start();
        }
    }

    /**
     * cette fonction est appeler a chaque fois que l'utilisateur tourne son ecran
     * this function is unused since the screen is locked
     */
    //TODO : a supprimer si elle set a rien (surment importante avec la veille)
    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    /**
     * on surface destruction stop the thread
     * la surface ce fait detruire aussi quand le jeu est en pause
     * TODO ajouter une relance sur le GameActivity
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
    List<Drawable> toremove = new ArrayList<>();
    @Override
    protected void onDraw(Canvas canvas) {
        //le to Array patch les crash

        for(Drawable d : toDraw.toArray(new Drawable[0]))
        {
            if(d instanceof DrawableSelfRemoving)
            {
                if(((DrawableSelfRemoving)d).isDone())
                {
                    toremove.add(d);
                }
            }
            d.drawOn(canvas,p);
        }
        for(Drawable d : toremove)
        {
            removeToDrawWithoutUpdate(d);
        }
        if(toremove.size() > 0)updateFrame();
        toremove.clear();

    }

    /**
     * le moteur arrete toute forme de rendu si rien ne change MAIS il ne peut pas detecter des changement qui
     * ne passe pas par la classe Renderer donc si vous modifier un drawable il faut appeler updateFrame() pour
     * demander au moteur de faire sont travaille
     */
    public void updateFrame() {
        needToUpdate =true;
    }

    /**
     * main drawing thread render each frame
     */
    private class DrawingThread extends Thread {
        private  Renderer renderer;

        public DrawingThread(Renderer renderer)
        {
            super();
            this.renderer=renderer;
        }

        public boolean keepDrawing = true;

        @SuppressLint("WrongCall")
        @Override
        public void run() {
            while(getWidth()==0);
            GameActivity.screenHeight=renderer.getHeight() < renderer.getWidth() ? renderer.getHeight() : renderer.getWidth();
            GameActivity.screenWidth=renderer.getHeight() > renderer.getWidth() ? renderer.getHeight() : renderer.getWidth();
            engine = new GameLogicThread(gameActivity,renderer);
            engine.start();

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
                while(!needToUpdate)
                {
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                needToUpdate =false;
            }
        }
    }

    /**
     * permet d'ajouter un element a afficher
     * @return retourne faux si l'element existe deja
     */
    public boolean addToDraw(Drawable newElement){
        boolean returnValue = addToDrawWithoutUpdate(newElement);
        updateFrame();
        return returnValue;
    }

    /**
     * permet d'ajouter un element a afficher
     * @return retourne faux si l'element existe deja
     */
    public boolean addToDrawWithoutUpdate(Drawable newElement){
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
                updateFrame();
                return;
            }
        }
        throw new DrawableNotFoundException();
    }

    /**
     * supprime le drawable possedant le nom indique mais n'upate pas le renderer
     * @param name le nom
     */
    public void removeToDrawWithoutUpdate(String name){
        Drawable d = getDrawAble(name);
        if(d != null)
            toDraw.remove(d);
    }

    /**
     * supprime le drawable possedant le nom indique mais n'upate pas le renderer
     * @param drawable le drawable
     */
    public void removeToDrawWithoutUpdate(Drawable drawable){
        removeToDrawWithoutUpdate(drawable.getName());
    }

    /**
     * supprime le drawable possedant le nom indique
     * @param name le nom
     */
    public void removeToDraw(String name){
        removeToDrawWithoutUpdate(name);
        updateFrame();
    }
    /**
     * remove the element from the draw list
     * @param toRemove the element toRemove
     */
    public void removeToDraw(Drawable toRemove){
        toDraw.remove(toRemove);
        updateFrame();
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

    /**
     * retourne une carte au coodonés donné
     * MAIS ne met pas ajour le rendu
     * @param x coordoné en x
     * @param y coodoné en y
     * @return
     */
    public DrawableCard getCardOn(float x, float y) {
        for(Drawable d : toDraw)
        {
            //on n'a pas d'objet non card a deplacer
            //si on venais a en avoir besoin crée une interface draggable s'avererais utile
            if(d instanceof DrawableCard && d.isDraggable())
            {
                DrawableCard card = ((DrawableCard)d);
                if(x >= card.getX() && x <= card.getX()+card.getCardWith())
                {
                    if(y >= card.getY() && y <= card.getY()+card.getCardHeight())
                    {
                        return card;
                    }
                }
            }
        }
        return null;

    }
}
