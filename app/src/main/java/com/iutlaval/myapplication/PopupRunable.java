package com.iutlaval.myapplication;

import android.content.Context;
import android.widget.Toast;

public class PopupRunable implements Runnable{
    String recivedMessage;
    Context context;

    public PopupRunable(String recivedMessage, Context context)
    {
        this.recivedMessage=recivedMessage;
        this.context=context;
    }

    @Override
    public void run() {
        Toast popupToast = new Toast(context);
        popupToast.setText(recivedMessage);
        popupToast.setDuration(Toast.LENGTH_LONG);
        popupToast.show();
    }
}
