package it.polimi.ingsw.ClientsHandler;

import it.polimi.ingsw.ClientsHandler.Messages.Message;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import it.polimi.ingsw.Controller.Controller;
import it.polimi.ingsw.Controller.DataBuffer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread implements PingWaiter
{
    private final DataBuffer dataBuffer;
    private final Socket socket;
    private final ObjectInputStream in_stream;
    private final ObjectOutputStream out_stream;
    private final String clientIP;
    private boolean pingReceived;
    private final PongWaiting waitingPong;
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
        pingReceived=false;
        waitingPong= new PongWaiting(this);
        controller=null;
    }
    public void setController(Controller controller)
    {
        if(this.controller==null)
            this.controller= controller;
    }
    private void receive()
    {
        Object message;
        try{
            message= in_stream.readObject();
            if(message instanceof Message)
                ((Message)message).handle(dataBuffer);
            else
            {   // ping received
                pingReceived= true;
                // pong
                out_stream.writeObject(1);
            }
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
        waitingPong.interrupt();
        controller.notifyDisconnection();
        socket.close();
    }
    @Override
    public synchronized boolean getPing()
    {
        if(pingReceived)
        {
            pingReceived= false;
            return true;
        }
        else
            return false;
    }
    @Override
    public void run()
    {
        waitingPong.start();
        if(controller==null)
            throw new IllegalArgumentException("The controller wasn't set");
        while(!socket.isClosed())
            receive();
        System.out.println("Closing connection with "+getIP());
    }
    public String getIP() { return clientIP; }
}
