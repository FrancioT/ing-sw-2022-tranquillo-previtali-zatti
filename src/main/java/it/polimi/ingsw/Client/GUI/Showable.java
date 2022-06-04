package it.polimi.ingsw.Client.GUI;

import java.io.Closeable;

abstract class Showable implements Closeable
{
     abstract void show();

     void removeShowable()
     {
          GUI.getInstance().removeShowableStage(this);
     }

     @Override
     abstract public void close();
}
