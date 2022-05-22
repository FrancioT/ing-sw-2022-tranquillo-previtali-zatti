package it.polimi.ingsw.ClientsHandler;

import it.polimi.ingsw.ClientsHandler.Messages.Message;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import it.polimi.ingsw.Controller.Controller;
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
    private final ObjectInputStream in_stream;
    private final ObjectOutputStream out_stream;
    private final String clientIP;
    private Controller controller;

    public ClientHandler(Socket socket, DataBuffer dataBuffer) throws IOException
    {
        if(socket==null || dataBuffer==null)
            throw new NullPointerException();
        this.dataBuffer=dataBuffer;
        this.socket=socket;
        try {
            out_stream= new ObjectOutputStream(socket.getOutputStream());
            in_stream= new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            try{ socket.close(); } catch (IOException ignored){}
            throw e;
        }
        clientIP=socket.getRemoteSocketAddress().toString();
        controller=null;
    }
    public void setController(Controller controller)
    {
        if(this.controller==null)
            this.controller= controller;
        return;
    }
    private void receive()
    {
        Object message;
        try{
            message= in_stream.readObject();
            if(message instanceof Message)
                ((Message)message).handle(dataBuffer);
        } catch (IOException | ClassNotFoundException e)
        {
            System.out.println(getIP()+" disconnected");
            try{ close(); }catch(IOException ignored){}
        }
    }
    public synchronized void send(ModelMessage message) throws IOException
    {
        out_stream.writeObject(message);
        out_stream.flush();
        out_stream.reset();
    }
    @Override
    public synchronized void close() throws IOException
    {
        controller.notifyDisconnection();
        socket.close();
    }
    @Override
    public void run()
    {
        if(controller==null)
            throw new IllegalArgumentException("The controller wasn't set");
        while(!socket.isClosed())
            receive();
        System.out.println("Closing connection with "+getIP());
    }
    public String getIP() { return clientIP; }
}
