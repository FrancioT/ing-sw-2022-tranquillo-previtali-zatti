package it.polimi.ingsw.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server extends Thread implements Closeable{
    private static final int port=55790;
    private ServerSocket serverSocket;
    List<Thread> games;

    /**
     * Constructor of server
     */
    public Server()
    {
        serverSocket=null;
        games=new ArrayList<>();
    }

    public static void main(String[] args) throws InterruptedException
    {
        Thread server= new Server();
        server.start();
        server.join();
    }

    /**
     * Method that creates the ServerSocket and starts listening
     */
    private void init() throws IOException
    {
        serverSocket = new ServerSocket(port);
        System.out.println(">>> Listening on " + port);
    }

    /**
     * Method that accepts the players' connection to the server
     * @return the players' socket
     */
    public Socket acceptConnection() throws IOException
    {
        // blocking call
        Socket accepted = serverSocket.accept();
        System.out.println("Connection accepted: " + accepted.getRemoteSocketAddress());
        return accepted;
    }

    /**
     * Method that is called when a player connects to the server; if there are no games available it asks the
     * player the settings he wants to use in the game, and then it waits until there are enough players
     * @param clientConnection the player's socket
     */
    private void gameModeAndWaitPlayers(Socket clientConnection) throws IOException
    {
        InputStream is = null;
        OutputStream os = null;
        // getting the streams for communication
        is = clientConnection.getInputStream();
        os = clientConnection.getOutputStream();

        // BufferedReader is useful because it offers readLine
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        PrintWriter out = new PrintWriter(os);

        out.println("Choose game mode");
        out.flush();

        String nickname= in.readLine();
        out.println(nickname);
        out.flush();
        int mode;
        try {
            mode = Integer.parseInt(in.readLine());
        }catch (NumberFormatException e){
            clientConnection.close();
            throw new IOException();
        }
        GameQueue queue= new GameQueue(mode%10==1, mode/10, clientConnection,
                                       nickname, this);
        games.add(queue.waitForClients());
    }

    @Override
    public void run()
    {
        try {
            init();
        } catch (IOException ignored)
        {
            System.out.println("Error during in initialization of Server socket");
        }
        Socket clientSocket=null;
        while(true)
        {
            try {
                clientSocket = acceptConnection();
                gameModeAndWaitPlayers(clientSocket);
            }catch(IOException e) {
                try{ clientSocket.close(); }catch(IOException ignored){}
                System.out.println("Error during game creation");
            }
        }
    }

    @Override
    public void close() throws IOException
    { serverSocket.close(); }
}
