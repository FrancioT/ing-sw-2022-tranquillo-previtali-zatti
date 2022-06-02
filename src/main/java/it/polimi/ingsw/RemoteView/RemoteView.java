package it.polimi.ingsw.RemoteView;

import it.polimi.ingsw.ClientsHandler.ClientHandler;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;

public class RemoteView implements PropertyChangeListener
{
    private final ClientHandler client;

    /**
     * Constructor of RemoteView
     * @param client the clientHandler to which this remoteView is associated
     */
    public RemoteView(ClientHandler client)
    {
        this.client=client;
    }

    /**
     * Method to send messages to the players, and in case of fatal errors or endGame it closes the game
     */
    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        String eventName=event.getPropertyName();
        if("ModelModifications".equals(eventName))
        {
            ModelMessage message= (ModelMessage) event.getNewValue();
            try {
                client.send(message);
            } catch (IOException e)
            {
                System.out.println("Failed to send Model modifications to client: " + client.getIP());
                try{ close(); } catch(IOException ignored){}
            }
            if(message.errorStatus())
            {
                if(message.getErrorMessage().isFatal())
                    try{ close(); } catch(IOException ignored){}
            }
            else if(message.hasGameEnded())
                    try{ close(); } catch(IOException ignored){}
        }
    }
    private void close() throws IOException
    { client.close(); }
}
