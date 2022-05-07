package it.polimi.ingsw.ClientsHandler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread
{
    private final Socket socket;
    private ObjectInputStream in_stream;
    private ObjectOutputStream out_stream;

    public ClientHandler(Socket socket)
    {
        this.socket=socket;
        try {
            in_stream= new ObjectInputStream(socket.getInputStream());
            out_stream= new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) { /*//////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////// send error message /////////////////////////////////*/
            try{ socket.close(); } catch (IOException ignored){}
        }
    }
    @Override
    public void run()
    {

    }
}
