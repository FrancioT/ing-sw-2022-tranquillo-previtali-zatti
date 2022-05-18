package it.polimi.ingsw.Client;

import it.polimi.ingsw.Client.Exceptions.BadMessageException;
import it.polimi.ingsw.ClientsHandler.Messages.Message;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Closeable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Receiver extends Thread implements Closeable
{
    private final Socket connection;
    private final ObjectInputStream in_stream;
    private final ObjectOutputStream out_stream;
    protected final PropertyChangeSupport support;

    public Receiver(Socket socket) throws IOException
    {
        support= new PropertyChangeSupport(this);
        connection = socket;
        out_stream = new ObjectOutputStream(connection.getOutputStream());
        in_stream = new ObjectInputStream(connection.getInputStream());
    }
    private void receiveModel() throws BadMessageException
    {
        Object message;
        try{
            message= in_stream.readObject();
            if(message instanceof ModelMessage)
                notify((ModelMessage)message);
        } catch (Exception ignored) {}
        throw new BadMessageException();
    }
    public synchronized void send(Message message) throws IOException
    {
        out_stream.writeObject(message);
        out_stream.flush();
        out_stream.reset();
    }
    @Override
    public synchronized void close() throws IOException
    { connection.close(); }
    @Override
    public void run()
    {
        while(!connection.isClosed())
        {
            try {
                receiveModel();
            }catch (BadMessageException e)
            {
                System.out.println("Server sent a malformed or incomprehensible message");
                try{ connection.close(); }catch(IOException ignored){}
            }
        }
    }
    public synchronized void addPropertyChangeListener(PropertyChangeListener listener)
    {
        if(listener==null)
            throw new NullPointerException();
        support.addPropertyChangeListener(listener);
    }
    private void notify(ModelMessage message)
    {
        if(message==null)
            throw new NullPointerException();
        support.firePropertyChange("ModelModifications", null, message);
    }
}
