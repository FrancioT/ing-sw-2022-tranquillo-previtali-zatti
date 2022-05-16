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

    public GameQueue(boolean expertModeFlag, int playersNum, Socket firstClient, String nickname,
                     Server server)
    {
        this.players_num=playersNum;
        this.expertMode=expertModeFlag;
        clients= new HashMap<>();
        clients.put(firstClient, nickname);
        this.server=server;
    }
    public Thread waitForClients() throws IOException
    {
        for(int i=1; i<players_num; i++)
            acceptConnection();

        List<RemoteView> remoteViews= new ArrayList<>();
        Map<String, DataBuffer> players= new HashMap<>();
        for(Socket client: clients.keySet())
        {
            DataBuffer dataBuffer= new DataBuffer(clients.get(client));
            players.put(clients.get(client), dataBuffer);
            ClientHandler clientHandler= new ClientHandler(client, dataBuffer);
            remoteViews.add(new RemoteView(clientHandler));
            clientHandler.start();
        }
        Thread game= new Controller(players, expertMode, remoteViews);
        game.start();
        return game;
    }

    public void acceptConnection() throws IOException
    {
        Socket accepted = server.acceptConnection();
        OutputStream os = null;
        InputStream is = null;
        is = accepted.getInputStream();
        os = accepted.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        PrintWriter out = new PrintWriter(os);
        out.println("Waiting for players");
        out.flush();
        String nickname= in.readLine();
        clients.put(accepted, nickname);
    }
}
