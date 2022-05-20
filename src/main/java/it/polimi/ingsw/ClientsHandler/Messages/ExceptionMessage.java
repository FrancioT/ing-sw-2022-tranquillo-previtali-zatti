package it.polimi.ingsw.ClientsHandler.Messages;

import java.io.Serializable;

public class ExceptionMessage implements Serializable
{
    String message;
    boolean fatal;
    static final long serialVersionUID= 31000L;
    public ExceptionMessage(String message, boolean fatalFlag)
    {
        this.message= message;
        this.fatal=fatalFlag;
    }
    public String getMessage() { return message; }
    public boolean isFatal() { return fatal; }
}
