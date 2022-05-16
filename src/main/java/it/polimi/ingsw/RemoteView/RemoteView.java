package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.ClientsHandler.ClientHandler;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class RemoteView implements PropertyChangeListener
{
    private final ClientHandler client;
    public RemoteView(ClientHandler client)
    {
        this.client=client;
    }
    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        String eventName=event.getPropertyName();
        if("ModelModifications".equals(eventName))
        {
            try {
                client.send((ModelMessage) event.getNewValue());
            } catch (IOException e)
            {
                System.out.println("Failed to send Model modifications to client: " + client.getIP().toString());
            }
        }
    }
    public void close() throws IOException
    { client.close(); }
}
