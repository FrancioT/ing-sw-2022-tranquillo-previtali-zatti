package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.Client.Exceptions.BadMessageException;
import it.polimi.ingsw.ClientsHandler.Messages.ExceptionMessage;
import it.polimi.ingsw.ClientsHandler.Messages.Message;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import it.polimi.ingsw.ClientsHandler.PingWaiter;
import it.polimi.ingsw.ClientsHandler.PongWaiting;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Receiver extends Thread implements PingWaiter
{
    private final Socket connection;
    private final ObjectInputStream in_stream;
    private final ObjectOutputStream out_stream;
    private final PropertyChangeSupport support;
    private boolean pingReceived;
    private final PongWaiting waitingPong;

    public Receiver(Socket socket) throws IOException
    {
        support= new PropertyChangeSupport(this);
        connection = socket;
        out_stream = new ObjectOutputStream(connection.getOutputStream());
        in_stream = new ObjectInputStream(connection.getInputStream());
        pingReceived= false;
        waitingPong= new PongWaiting(this);
    }
    /**
     * Method that receives the ModelMessage and checks if there are errors or game has ended; it also manages the
     * ping-pong exchange with the server
     */
    private void receiveModel() throws BadMessageException, IOException
    {
        Object message;
        try{
            message= in_stream.readObject();
            if(message instanceof ModelMessage)
            {
                notify((ModelMessage)message);
                // check if the game ended
                ModelMessage mes= (ModelMessage) message;
                if(mes.errorStatus())
                    if(mes.getErrorMessage().isFatal())
                        try{ close(); }catch (IOException ignored){}
                else
                    if(mes.hasGameEnded())
                        try{ close(); }catch (IOException ignored){}
            }
            else
            {   // ping received
                pingReceived= true;
                // pong
                out_stream.writeObject(1);
            }
        } catch (ClassNotFoundException e) { throw new BadMessageException(); }
    }
    public synchronized void send(Message message) throws IOException
    {
        out_stream.writeObject(message);
        out_stream.flush();
        out_stream.reset();
    }
    @Override
    public synchronized void close() throws IOException
    {
        waitingPong.interrupt();
        connection.close();
    }

    /**
     * Function called by the pong waiter thread
     * @return true if there was a response to a ping sent between the last call of this function and this call
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
     * method that manages the receipt of the ModelMessage until the connection is closed
     */
    @Override
    public void run()
    {
        waitingPong.start();
        while(!connection.isClosed())
        {
            try {
                receiveModel();
            }catch (BadMessageException e)
            {
                try{ connection.close(); }catch(IOException ignored){}
                notify(new ModelMessage(new ExceptionMessage("Server sent a malformed or incomprehensible " +
                        "message", true)));
            }
            catch (IOException e1)
            {
                try{ connection.close(); }catch(IOException ignored){}
                notify(new ModelMessage(new ExceptionMessage("Connection error", true)));
            }
        }
    }
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener)
    {
        if(listener==null)
            throw new NullPointerException();
        support.addPropertyChangeListener(listener);
    }
    protected void notify(ModelMessage message)
    {
        if(message==null)
            throw new NullPointerException();
        support.firePropertyChange("ModelModifications", null, message);
    }
}
