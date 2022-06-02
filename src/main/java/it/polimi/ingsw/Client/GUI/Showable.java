package it.polimi.ingsw.Client.GUI;

abstract class Showable
{
     abstract void show();

     void removeShowable()
     {
          GUI.getInstance().removeShowableStage(this);
     }
}
