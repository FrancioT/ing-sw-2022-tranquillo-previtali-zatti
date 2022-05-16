package it.polimi.ingsw.Client;

import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import it.polimi.ingsw.Model.CharacterCard.CharacterCard;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.Player;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CLI extends Thread implements PropertyChangeListener
{
    private Socket connection;
    private ModelMessage game;
    private static final String serverIP="127.0.0.1";
    private static final int serverPort=12345;
    private static final String firstPlayerMessage="Choose game mode";

    public CLI()
    {
        connection=null;
        game= new ModelMessage(false, new ArrayList<Island>(), new ArrayList<Cloud>(), new ArrayList<Player>(),
                               new ArrayList<CharacterCard>(), 0, false);
    }
    public static void main(String[] args) throws InterruptedException
    {
        Thread cliThread= new CLI();
        cliThread.start();
        cliThread.join();
    }
    @Override
    public void run()
    {
        try { beginning(); } catch (IOException e) { return; }
        System.out.println("\nWrite /help to get the commands\n");
        try {
            Receiver receiver= new Receiver(connection);
            receiver.start();
            receiver.addPropertyChangeListener(this);
        }catch (IOException e)
        {
            System.out.println("Failed to create a stable connection with the server");
            return;
        }
        while(!game.hasGameEnded())
            handleCommands();
        System.out.println("Game finished!");
    }
    private void beginning() throws IOException
    {
        String mode= new String();
        boolean firstPlayerFlag= true;
        Scanner keyboardInput = new Scanner(System.in);
        System.out.println("Welcome to Eriantys!");
        System.out.println("Your nickname during this game will be: ");
        String name = keyboardInput.nextLine();
        try {
            firstPlayerFlag= connect();
        }catch (IOException e)
        {
            System.out.println("Unable to connect to the server");
            throw e;
        }
        if(firstPlayerFlag)
        {
            System.out.println("Ok " + name + " with how many players do you want this game to be?");
            System.out.println("1) 2 players ");
            System.out.println("2) 3 players ");
            System.out.println("3) 4 players ");
            mode = keyboardInput.nextLine();
            System.out.println("Will it be in expert or simple mode?");
            System.out.println("1) expert mode");
            System.out.println("2) simple mode");
            mode.concat(keyboardInput.nextLine());
        }
        try {
            sendInfo(name, mode);
        }catch (IOException e)
        {
            System.out.println("Failed to communicate with the server");
            throw e;
        }
    }
    private void handleCommands()
    {

    }
    private boolean connect() throws IOException
    {
        connection = new Socket(serverIP, serverPort);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return in.readLine().equals(firstPlayerMessage);
    }
    private void sendInfo(String nickname, String mode) throws IOException
    {
        PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
        out.println(nickname);
        if(!mode.isEmpty())
            out.println(mode);
    }
    @Override
    public synchronized void propertyChange(PropertyChangeEvent event)
    {
        String eventName=event.getPropertyName();
        if("ModelModifications".equals(eventName))
        {

        }
    }
}
