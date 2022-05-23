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

    public void init() throws IOException
    {
        serverSocket = new ServerSocket(port);
        System.out.println(">>> Listening on " + port);
    }

    public Socket acceptConnection() throws IOException
    {
        // blocking call
        Socket accepted = serverSocket.accept();
        System.out.println("Connection accepted: " + accepted.getRemoteSocketAddress());
        return accepted;
    }

    public void gameModeAndWaitPlayers(Socket clientConnection) throws IOException
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
        int mode= Integer.parseInt(in.readLine());
        GameQueue queue= new GameQueue(mode%10==1, mode/10, clientConnection,
                                       nickname, this);
        games.add(queue.waitForClients());
    }

    public void run()
    {
        try {
            init();
        } catch (IOException ignored)
        {
            System.out.println("Error during in initialization of Server socket");
        }
        try {
            while(true)
            {
                Socket socket = acceptConnection();
                gameModeAndWaitPlayers(socket);
            }
        } catch (IOException ignored)
        {
            System.out.println("Error during game creation");
        }
    }

    public void close() throws IOException
    { serverSocket.close(); }
}
