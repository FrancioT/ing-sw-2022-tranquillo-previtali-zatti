package it.polimi.ingsw.Client.GUI;

import java.io.Closeable;

abstract class Showable implements Closeable
{
     abstract void show();

     @Override
     abstract public void close();
     abstract public void pause();
     abstract public void resume();
}
