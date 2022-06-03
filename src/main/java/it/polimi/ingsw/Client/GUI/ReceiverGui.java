package it.polimi.ingsw.Client.GUI;

import it.polimi.ingsw.Client.CLI.Receiver;
import it.polimi.ingsw.ClientsHandler.Messages.ModelMessage;
import javafx.application.Platform;

import java.io.IOException;
import java.net.Socket;

public class ReceiverGui extends Receiver
{
    public ReceiverGui(Socket socket) throws IOException
    {
        super(socket);
    }
    @Override
    protected void notify(ModelMessage message)
    {
        Platform.runLater(() -> super.notify(message));
    }
}
