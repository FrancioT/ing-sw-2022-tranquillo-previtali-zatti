package it.polimi.ingsw.Client.GUI;

public class GuiExecutor
{
    public static void main(String[] args)
    {
        GUI gui= GUI.getInstance();
        gui.execute(args);
    }
}
