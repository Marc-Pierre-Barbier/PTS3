package com.iutlaval.myapplication.Game;

import android.util.Log;

import com.iutlaval.myapplication.exception.NotAnIntegerException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

public class Communication {
    private static final short MAX_ACCEPTABLE_PING = 500;
    public static final String TIMEOUT = "timeout";
    public static final String ERROR = "ERROR";

    private ObjectInputStream clientIn;
    private ObjectOutputStream clientOut;
    private Socket client;

    public Communication(Socket client) throws IOException {
        clientIn = new ObjectInputStream(client.getInputStream());
        clientOut = new ObjectOutputStream(client.getOutputStream());
        this.client=client;
    }

    /**
     * on met un delais infini d'attente de réponse
     * @throws SocketException
     */
    public void setUnlimitedTimeOut() throws SocketException {
        //ce n'est pas réelement infini juste trés grand
        client.setSoTimeout(Integer.MAX_VALUE);
    }

    /**
     * on met le delais défini par la constante MAX_ACCEPTABLE_PING en tant que delais de reponse
     * @throws SocketException
     */
    public void setLimitedTimeOut() throws SocketException {
        client.setSoTimeout(MAX_ACCEPTABLE_PING);
    }

    /**
     * on envoie que des chaines de caractére car on na pas euh de cours sur le reseau et que l'envoie d'entier a déja causé de nombreux probléme
     * @param message
     */
    public void send(String message) throws IOException {
        clientOut.writeObject(message);
    }

    /**
     * redéfinitoin de send() qui convertie l'entier recu en chaine de caractére
     * @param message
     * @throws IOException
     */
    public void send(int message) throws IOException {
        send(""+message);
    }

    /**
     * recoit un message sous forme de chaine
     * @return retourne ERROR ou TIMEOUT si une erreur ce produit
     */
    public String recieve(){
        try {
            return (String) clientIn.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            //on crash
            return ERROR;
        } catch (IOException e) {
            return TIMEOUT;
        }
    }

    /**
     * retourne un entier recut
     *
     * @return
     */
    public int recieveInt()
    {
        String message = recieve();

        if(message.matches("-?\\d+"))
        {
            return Integer.parseInt(message);
        }else{
            if(!(message.equals(TIMEOUT)||message.equals(ERROR)))
            {
                throw new NotAnIntegerException();
            }
            return Integer.MIN_VALUE;//retourne la valeur la plus petite en guise de message d'erreur
        }
    }

    /**
     * ferme la connection
     * @throws IOException
     */
    public void close() throws IOException {
        if(clientOut != null)clientOut.close();
        if(clientIn != null)clientIn.close();
        if(client != null)client.close();
    }
}
