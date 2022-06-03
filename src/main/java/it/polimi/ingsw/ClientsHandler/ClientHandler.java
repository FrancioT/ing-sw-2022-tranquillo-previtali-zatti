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
    private final PingSender pingSender;
    private Controller controller;

    /**
     * Constructor of ClientHandler
     * @param socket the client's socket
     * @param dataBuffer the DataBuffer associated with this clientHandler
     */
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
        pingSender= new PingSender(this);
        controller=null;
    }
    public void setController(Controller controller)
    {
        if(this.controller==null)
            this.controller= controller;
    }

    /**
     * Method that analyzes the messages received from the client and, depending on the case, it sets the values
     * received in the client's dataBuffer. It also sends a message in case a player disconnects
     */
    private void receive()
    {
        Object message;
        try{
            message= in_stream.readObject();
            if(message instanceof Message)
                ((Message)message).handle(dataBuffer);
            else
                // pong received (treated as a ping)
                pingReceived= true;
        } catch (IOException | ClassNotFoundException e)
        {
            System.out.println(getIP()+" disconnected");
            try{ close(); }catch(IOException ignored){}
        }
    }

    /**
     * Method that sends messages to the client
     * @param message message to send
     */
    public synchronized void send(ModelMessage message) throws IOException
    {
        out_stream.writeObject(message);
        out_stream.flush();
        out_stream.reset();
    }

    /**
     * Method that is used to send a ping to the client
     */
    public synchronized void ping() throws IOException
    {
        out_stream.writeObject(1);
    }
    @Override
    public synchronized void close() throws IOException
    {
        waitingPong.interrupt();
        pingSender.interrupt();
        if(controller!=null)
            controller.notifyDisconnection();
        socket.close();
    }

    /**
     * @return true if a pong has been received, false if not
     */
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

    /**
     * This method starts the ping-pong exchange between server and client to understand if a client disconnects.
     * Then, it starts listening for datas from the client
     */
    @Override
    public void run()
    {
        pingSender.start();
        waitingPong.start();
        if(controller==null)
            throw new IllegalArgumentException("The controller wasn't set");
        while(!socket.isClosed())
            receive();
        System.out.println("Closing connection with "+getIP());
    }
    public String getIP() { return clientIP; }
}
