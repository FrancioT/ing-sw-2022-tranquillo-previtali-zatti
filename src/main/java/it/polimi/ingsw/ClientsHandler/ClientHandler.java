package it.polimi.ingsw.ClientsHandler;

import it.polimi.ingsw.ClientsHandler.Messages.Message;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import it.polimi.ingsw.Controller.DataBuffer;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread implements Closeable
{
    private final DataBuffer dataBuffer;
    private final Socket socket;
    private ObjectInputStream in_stream;
    private ObjectOutputStream out_stream;

    public ClientHandler(Socket socket, DataBuffer dataBuffer)
    {
        if(socket==null || dataBuffer==null)
            throw new NullPointerException();
        this.dataBuffer=dataBuffer;
        this.socket=socket;
        try {
            in_stream= new ObjectInputStream(socket.getInputStream());
            out_stream= new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) { /*//////////////////////////////////////////////////////////////////////////////////////
            ////////////////////////////////////////////////////// send error message /////////////////////////////////*/
            try{ socket.close(); } catch (IOException ignored){}
        }
    }
    private void receive()
    {
        Message message;
        try{
            message= (Message) in_stream.readObject();
            message.handle(dataBuffer);
        } catch (Exception ignored) {}
    }
    public void send(ModelMessage message)
    {
        try {
            out_stream.writeObject(message);
            out_stream.flush();
            out_stream.reset();
        } catch (IOException ignored){}
    }
    @Override
    public void close() throws IOException
    { socket.close(); }
    @Override
    public void run()
    {
        while(!socket.isClosed())
            receive();
    }
}
