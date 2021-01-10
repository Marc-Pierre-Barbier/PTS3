package com.iutlaval.myapplication.Game;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.iutlaval.myapplication.R;

/**
 * cette classe permet de faire des toast en dehors de du thread de l'activité
 */
public class PopupRunable implements Runnable{
    String recivedMessage;
    Context context;

    private PopupRunable(Activity cont, String recivedMessage)
    {
        this.recivedMessage=recivedMessage;
        this.context=cont;
    }

    @Override
    public void run() {
        Toast popupToast = Toast.makeText(context,recivedMessage,Toast.LENGTH_LONG);
        popupToast.show();
    }

    /**
     * affiche un toast avec le texte provenant de l'id donné (R.string.*)
     * @param activity
     * @param stringId
     */
    public static void makePopup(Activity activity,int stringId)
    {
        String recivedMessage = activity.getResources().getString(stringId);
        activity.runOnUiThread(new PopupRunable(activity,recivedMessage));
    }

    /**
     * affiche un toast avec le texte passé en argument
     * @param activity
     * @param message
     */
    public static void makePopup(Activity activity,String message)
    {
        activity.runOnUiThread(new PopupRunable(activity,message));
    }
}
