package it.polimi.ingsw.Server;

import it.polimi.ingsw.ClientsHandler.ClientHandler;
import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;
import it.polimi.ingsw.RemoteView.RemoteView;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameQueue
{
    private final boolean expertMode;
    private final int players_num;
    private final Map<Socket, String> clients;
    private final Server server;

    /**
     * Constructor of GameQueue
     * @param expertModeFlag true if expert mode, false if standard mode
     * @param playersNum how many players will be in the game
     * @param firstClient the player's socket who created the game
     * @param nickname player's nickname who created the game
     * @param server server on which is based the game
     */
    public GameQueue(boolean expertModeFlag, int playersNum, Socket firstClient, String nickname,
                     Server server)
    {
        this.players_num=playersNum;
        this.expertMode=expertModeFlag;
        clients= new HashMap<>();
        clients.put(firstClient, nickname);
        this.server=server;
    }

    /**
     * Method to wait for players to join the match; when all necessary players have joined, it creates all
     * the necessary things for them to play (e.g. remote views, dataBuffers, ...). Finally, it creates the controller
     * and start the game.
     * @return the thread of the game
     */
    public Thread waitForClients() throws IOException
    {
        // waiting for enough players to join
        for(int i=1; i<players_num; i++)
            acceptConnection();
        // creates the necessary elements to play the game
        List<RemoteView> remoteViews= new ArrayList<>();
        List<ClientHandler> clientHandlerList= new ArrayList<>();
        Map<String, DataBuffer> players= new HashMap<>();
        for(Socket client: clients.keySet())
        {
            DataBuffer dataBuffer= new DataBuffer(clients.get(client));
            players.put(clients.get(client), dataBuffer);
            ClientHandler clientHandler=null;
            try{
                clientHandler= new ClientHandler(client, dataBuffer);
            }catch (IOException e){
                //close connection with everyone
                for(Socket socket: clients.keySet())
                    try{ socket.close(); }catch(IOException ignored){}
                throw e;
            }
            remoteViews.add(new RemoteView(clientHandler));
            clientHandlerList.add(clientHandler);
        }
        // creates the controller and start the game
        Controller game= new Controller(players, expertMode, remoteViews);
        for(ClientHandler c: clientHandlerList)
        {
            c.setController(game);
            c.start();
        }
        game.start();
        return game;
    }

    /**
     * Method that accepts the connection of players; then, it reads their nickname and in case there is another player
     * with the same name, it adds a number to it
     */
    public void acceptConnection() throws IOException
    {
        boolean connectionEstablished=false;
        Socket accepted=null;
        PrintWriter out=null;
        BufferedReader in=null;
        String nickname="";
        while(!connectionEstablished)
        {
            accepted = server.acceptConnection();
            try {
                in = new BufferedReader(new InputStreamReader(accepted.getInputStream()));
                out = new PrintWriter(accepted.getOutputStream());
                out.println("Waiting for players");
                out.flush();
                nickname = in.readLine();
                if (nickname == null)
                    throw new IOException();
                connectionEstablished=true;
            }catch (IOException e){
                try{ accepted.close(); }catch(IOException ignored){}
            }
        }
        boolean changedName=true;
        String newNickname= nickname;
        for(int i=1; i<100 && changedName; i++)
        {
            changedName=false;
            for(String name: clients.values())
                if(newNickname.equals(name))
                {
                    newNickname=nickname+i;
                    changedName=true;
                    break;
                }
        }
        nickname=newNickname;
        out.println(nickname);
        out.flush();
        clients.put(accepted, nickname);
    }
}
