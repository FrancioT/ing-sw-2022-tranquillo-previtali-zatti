package it.polimi.ingsw.ClientsHandler;

import it.polimi.ingsw.ClientsHandler.Messages.Message;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import it.polimi.ingsw.Controller.DataBuffer;

import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class ClientHandler extends Thread implements Closeable
{
    private final DataBuffer dataBuffer;
    private final Socket socket;
    private ObjectInputStream in_stream;
    private ObjectOutputStream out_stream;

    public ClientHandler(Socket socket, DataBuffer dataBuffer) throws IOException
    {
        if(socket==null || dataBuffer==null)
            throw new NullPointerException();
        this.dataBuffer=dataBuffer;
        this.socket=socket;
        try {
            in_stream= new ObjectInputStream(socket.getInputStream());
            out_stream= new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            try{ socket.close(); } catch (IOException ignored){}
            throw e;
        }
    }
    private void receive()
    {
        Object message;
        try{
            message= in_stream.readObject();
            if(message instanceof Message)
                ((Message)message).handle(dataBuffer);
        } catch (IOException | ClassNotFoundException ignored) {}
    }
    public synchronized void send(ModelMessage message) throws IOException
    {
        out_stream.writeObject(message);
        out_stream.flush();
        out_stream.reset();
    }
    @Override
    public synchronized void close() throws IOException
    { socket.close(); }
    @Override
    public void run()
    {
        while(!socket.isClosed())
            receive();
    }
    public SocketAddress getIP()
    { return socket.getRemoteSocketAddress(); }
}
