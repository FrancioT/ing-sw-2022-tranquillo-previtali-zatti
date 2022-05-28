package it.polimi.ingsw.Client.CLI;

import it.polimi.ingsw.ClientsHandler.Messages.*;
import it.polimi.ingsw.ClientsHandler.Messages.CharacterCardMessages.*;
import it.polimi.ingsw.Model.CharacterCard.CharacterCard;
import it.polimi.ingsw.Model.Cloud;
import it.polimi.ingsw.Model.Colour;
import it.polimi.ingsw.Model.Island;
import it.polimi.ingsw.Model.ModelAndDecorators.Phase;
import it.polimi.ingsw.Model.Player;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

public class CLI extends Thread implements PropertyChangeListener
{
    private Socket connection;
    private String nickName;
    private Optional<ModelMessage> game;
    private boolean gameEnded;
    private boolean errorFlag;
    private final Boolean gameLock;
    private Receiver receiver;
    private static final String serverIP="127.0.0.1";
    private static final int serverPort=55790;
    private static final String firstPlayerMessage="Choose game mode";

    public CLI()
    {
        connection=null;
        game= Optional.empty();
        gameEnded=false;
        errorFlag=false;
        receiver=null;
        nickName="";
        gameLock=false;
    }
    public static void main(String[] args) throws InterruptedException
    {
        Thread cliThread= new CLI();
        cliThread.start();
        cliThread.join();
    }
    @Override
    public void run()
    {
        try { beginning(); } catch (IOException e) { return; }
        System.out.println("\nWrite \"help\" to get the commands\n");
        try {
            receiver= new Receiver(connection);
            receiver.addPropertyChangeListener(this);
            receiver.start();
        }catch (IOException e)
        {
            System.out.println("Failed to create a stable connection with the server");
            return;
        }
        synchronized(gameLock)
        {
            while (!game.isPresent())
                try{ gameLock.wait(); }
                catch(InterruptedException e)
                {
                    System.out.println("Error: thread interrupted");
                    try{ receiver.close(); }catch(IOException ignored){}
                    return;
                }
        }
        while(!gameEnded && !errorFlag)
        {
            try {
                handleCommands();
            }
            catch (IOException e)
            {
                System.out.println("Error in communicating with the server");
                try{ receiver.close(); } catch(IOException ignored){}
                return;
            }
        }
        try{ receiver.close(); } catch(IOException ignored){}
    }
    private void beginning() throws IOException
    {
        String mode= new String();
        boolean firstPlayerFlag= true;
        Scanner keyboardInput= new Scanner(System.in);
        System.out.println("Welcome to Eriantys!");
        System.out.println("Your nickname during this game will be: ");
        nickName = keyboardInput.nextLine();
        try {
            firstPlayerFlag= connect();
        }catch (IOException e)
        {
            System.out.println("Unable to connect to the server");
            throw e;
        }
        if(firstPlayerFlag)
        {
            System.out.println("Ok " + nickName + " with how many players do you want this game to be?");
            System.out.println("1) 2 players ");
            System.out.println("2) 3 players ");
            System.out.println("3) 4 players ");
            mode = Integer.toString(Integer.parseInt(keyboardInput.nextLine())+1);
            System.out.println("Will it be in expert or simple mode?");
            System.out.println("1) expert mode");
            System.out.println("2) simple mode");
            mode=mode.concat(keyboardInput.nextLine());
        }
        try {
            String newNickName= sendInfo(nickName, mode);
            if(!newNickName.equals(nickName))
            {
                nickName= newNickName;
                System.out.println("Your nickName was already taken, you now are "+ nickName);
            }
        }catch (IOException e)
        {
            System.out.println("Failed to communicate with the server");
            throw e;
        }
        System.out.println("Waiting for the other players to join...");
    }
    private void handleCommands() throws IOException
    {
        BufferedReader keyboardInput= new BufferedReader(new InputStreamReader(System.in));
        String[] command= keyboardInput.readLine().toLowerCase().split(" ");
        if(gameEnded || errorFlag)
            return;
        int pos=0;
        Colour colour;
        List<Colour> colourList= new ArrayList<>();
        Phase phase= game.orElse(null).getPhase();
        switch(command[0])
        {
            case "choosecard":
                if(phase!=Phase.choose_card)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(command.length==2)
                {
                    try{ pos= Integer.parseInt(command[1]); }
                    catch(NumberFormatException e){ pos=-1; }
                    if(pos>0)
                    {
                        pos--;
                        receiver.send(new ChooseCard(nickName, pos));
                        break;
                    }
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "choosecloud":
                if(phase!=Phase.choose_cloud)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(command.length==2)
                {
                    try{ pos= Integer.parseInt(command[1]); }
                    catch(NumberFormatException e){ pos=-1; }
                    if(pos>0)
                    {
                        pos--;
                        receiver.send(new ChooseCloud(nickName, pos));
                        break;
                    }
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "movemn":
                if(phase!=Phase.move_mother_nature)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(command.length==2)
                {
                    try{ pos= Integer.parseInt(command[1]); }
                    catch(NumberFormatException e){ pos=-1; }
                    if(pos>0)
                    {
                        pos--;
                        receiver.send(new MoveMN(nickName, pos));
                        break;
                    }
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "studenttodashboard":
                if(phase!=Phase.move_students)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(command.length==2)
                {
                    colour= toColour(command[1]);
                    if(colour!=null)
                    {
                        receiver.send(new StudentToDashboard(nickName, colour));
                        break;
                    }
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "studenttoisland":
                if(phase!=Phase.move_students)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(command.length==3)
                {
                    colour= toColour(command[1]);
                    if(colour!=null)
                    {
                        try{ pos= Integer.parseInt(command[1]); }
                        catch(NumberFormatException e){ pos=-1; }
                        if(pos>0)
                        {
                            pos--;
                            receiver.send(new StudentToIsland(nickName, colour, pos));
                            break;
                        }
                    }
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "card1effect":
                if(phase==Phase.choose_card)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(!checkCharacterCardPresence(1))
                {
                    System.out.println("This character card ID doesn't correspond to any card!");
                    break;
                }
                if(command.length==3)
                {
                    colour= toColour(command[1]);
                    if(colour!=null)
                    {
                        try{ pos= Integer.parseInt(command[1]); }
                        catch(NumberFormatException e){ pos=-1; }
                        if(pos>0)
                        {
                            pos--;
                            receiver.send(new Card1Data(nickName, 1, pos, colour));
                            break;
                        }
                    }
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "card3effect":
                if(phase==Phase.choose_card)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(!checkCharacterCardPresence(3))
                {
                    System.out.println("This character card ID doesn't correspond to any card!");
                    break;
                }
                if(command.length==2)
                {
                    try{ pos= Integer.parseInt(command[1]); }
                    catch(NumberFormatException e){ pos=-1; }
                    if(pos>0)
                    {
                        pos--;
                        receiver.send(new Card3_5Data(nickName, 3, pos));
                        break;
                    }
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "card5effect":
                if(phase==Phase.choose_card)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(!checkCharacterCardPresence(5))
                {
                    System.out.println("This character card ID doesn't correspond to any card!");
                    break;
                }
                if(command.length==2)
                {
                    try{ pos= Integer.parseInt(command[1]); }
                    catch(NumberFormatException e){ pos=-1; }
                    if(pos>0)
                    {
                        pos--;
                        receiver.send(new Card3_5Data(nickName, 5, pos));
                        break;
                    }
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "card7effect":
                if(phase==Phase.choose_card)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(!checkCharacterCardPresence(7))
                {
                    System.out.println("This character card ID doesn't correspond to any card!");
                    break;
                }
                if(command.length%2==1 && command.length<=7)
                {
                    boolean commandError=false;
                    for(int i=1; i<command.length && !commandError; i++)
                    {
                        colour= toColour(command[i]);
                        if(colour==null)
                        {
                            System.out.println("Malformed command, write /help to get all available commands");
                            commandError=true;
                        }
                        colourList.add(colour);
                    }
                    if(commandError)
                        break;
                    receiver.send(new Card7_10Data(nickName, 7, colourList));
                    break;
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "card10effect":
                if(phase==Phase.choose_card)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(!checkCharacterCardPresence(10))
                {
                    System.out.println("This character card ID doesn't correspond to any card!");
                    break;
                }
                if(command.length%2==1 && command.length<=5)
                {
                    boolean commandError=false;
                    for(int i=1; i<command.length && !commandError; i++)
                    {
                        colour= toColour(command[i]);
                        if(colour==null)
                        {
                            System.out.println("Malformed command, write /help to get all available commands");
                            commandError=true;
                        }
                        colourList.add(colour);
                    }
                    if(commandError)
                        break;
                    receiver.send(new Card7_10Data(nickName, 10, colourList));
                    break;
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "card9effect":
                if(phase==Phase.choose_card)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(!checkCharacterCardPresence(9))
                {
                    System.out.println("This character card ID doesn't correspond to any card!");
                    break;
                }
                if(command.length==2)
                {
                    colour= toColour(command[1]);
                    if(colour!=null)
                    {
                        receiver.send(new Card9_11_12Data(nickName, 9, colour));
                        break;
                    }
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "card11effect":
                if(phase==Phase.choose_card)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(!checkCharacterCardPresence(11))
                {
                    System.out.println("This character card ID doesn't correspond to any card!");
                    break;
                }
                if(command.length==2)
                {
                    colour= toColour(command[1]);
                    if(colour!=null)
                    {
                        receiver.send(new Card9_11_12Data(nickName, 11, colour));
                        break;
                    }
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "card12effect":
                if(phase==Phase.choose_card)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(!checkCharacterCardPresence(12))
                {
                    System.out.println("This character card ID doesn't correspond to any card!");
                    break;
                }
                if(command.length==2)
                {
                    colour= toColour(command[1]);
                    if(colour!=null)
                    {
                        receiver.send(new Card9_11_12Data(nickName, 12, colour));
                        break;
                    }
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "card2effect":
                if(phase==Phase.choose_card)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(!checkCharacterCardPresence(2))
                {
                    System.out.println("This character card ID doesn't correspond to any card!");
                    break;
                }
                if(command.length==1)
                {
                    receiver.send(new Card2_4_6_8(nickName, 2));
                    break;
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "card4effect":
                if(phase==Phase.choose_card)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(!checkCharacterCardPresence(4))
                {
                    System.out.println("This character card ID doesn't correspond to any card!");
                    break;
                }
                if(command.length==1)
                {
                    receiver.send(new Card2_4_6_8(nickName, 4));
                    break;
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "card6effect":
                if(phase==Phase.choose_card)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(!checkCharacterCardPresence(6))
                {
                    System.out.println("This character card ID doesn't correspond to any card!");
                    break;
                }
                if(command.length==1)
                {
                    receiver.send(new Card2_4_6_8(nickName, 6));
                    break;
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "card8effect":
                if(phase==Phase.choose_card)
                {
                    System.out.println("It's not the moment for this action!");
                    return;
                }
                if(!checkCharacterCardPresence(8))
                {
                    System.out.println("This character card ID doesn't correspond to any card!");
                    break;
                }
                if(command.length==1)
                {
                    receiver.send(new Card2_4_6_8(nickName, 8));
                    break;
                }
                System.out.println("Malformed command, write help to get all available commands");
                break;
            case "help":
                System.out.println("\n\n\nList of commands:");
                System.out.println("chooseCard [position of the card]");
                System.out.println("chooseCloud [position of the cloud]");
                System.out.println("studentToDashboard [colour of the student]");
                System.out.println("studentToIsland [colour of the student] [position of the island]");
                System.out.println("moveMN [new position of mother nature]");
                System.out.println("card[num]Effect [[parameters specified by the card]]");
                System.out.println("\nMoves order:");
                System.out.println("1) choose the card you want to play");
                System.out.println("2) move the students from your entrance");
                System.out.println("3) move mother nature");
                System.out.println("4) choose the cloud with which you want to refill your entrance");
                break;
            default:
                System.out.println("Malformed command, write help to get all available commands");
                break;
        }
    }
    private Colour toColour(String colour)
    {
        Map<String, Colour> toColour= new HashMap<>();
        toColour.put("blue", Colour.blue);
        toColour.put("red", Colour.red);
        toColour.put("pink", Colour.pink);
        toColour.put("yellow", Colour.yellow);
        toColour.put("green", Colour.green);
        return toColour.get(colour);
    }
    private boolean connect() throws IOException
    {
        connection = new Socket(serverIP, serverPort);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return in.readLine().equals(firstPlayerMessage);
    }
    private String sendInfo(String nickname, String mode) throws IOException
    {
        PrintWriter out = new PrintWriter(connection.getOutputStream(), true);
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        out.println(nickname);
        if(!mode.isEmpty())
            out.println(mode);
        String newNickName= in.readLine();
        return newNickName;
    }
    @Override
    public void propertyChange(PropertyChangeEvent event)
    {
        String eventName=event.getPropertyName();
        if("ModelModifications".equals(eventName))
        {
            ModelMessage message = (ModelMessage) event.getNewValue();
            if(message.errorStatus())
            {
                if(message.getErrorMessage().isFatal())
                {
                    if(!gameEnded)
                    {
                        System.out.println(message.getErrorMessage().getMessage());
                        errorFlag= true;
                    }
                    this.interrupt();
                }
                else
                {
                    synchronized(gameLock)
                    {
                        if(game.orElse(null).getCurrPlayerNickname().equals(nickName))
                            System.out.println(message.getErrorMessage().getMessage());
                    }
                }
                return;
            }
            synchronized(gameLock)
            {
                if (game.isPresent()) {
                    List<Island> islandList = game.orElse(null).getIslandList();
                    List<Cloud> cloudList = game.orElse(null).getCloudList();
                    List<Player> playerList = game.orElse(null).getPlayerList();
                    List<CharacterCard> characterCardList = game.orElse(null).getCharacterCardList();
                    if (message.getIslandList().size() != 0)
                        islandList = message.getIslandList();
                    if (message.getCloudList().size() != 0)
                        cloudList = message.getCloudList();
                    if (message.getPlayerList().size() != 0) {
                        for (Player newPlayer : message.getPlayerList())
                            for (Player oldPlayer : playerList)
                                if (oldPlayer.getuID().equals(newPlayer.getuID()))
                                    playerList.set(playerList.indexOf(oldPlayer), newPlayer);
                    }
                    if (message.getCharacterCardList().size() != 0) {
                        for (CharacterCard newCard : message.getCharacterCardList())
                            for (CharacterCard oldCard : characterCardList)
                                if (oldCard.getCardID() == newCard.getCardID())
                                    characterCardList.set(characterCardList.indexOf(oldCard), newCard);
                    }
                    game = Optional.of(new ModelMessage(message.isExpertMode(), islandList, cloudList, playerList,
                                                        characterCardList, message.getCurrPlayerNickname(),
                                                        message.getUnusedCoins(), message.hasGameEnded(),
                                                        message.getPhase()));
                    gameEnded=message.hasGameEnded();
                } else {
                    game = Optional.of(new ModelMessage(message.isExpertMode(), message.getIslandList(),
                                message.getCloudList(), message.getPlayerList(), message.getCharacterCardList(),
                                message.getCurrPlayerNickname(), message.getUnusedCoins(), message.hasGameEnded(),
                                message.getPhase()));
                }
                printGame();
                gameLock.notifyAll();
            }
        }
    }
    private void printGame()
    {
        synchronized(gameLock)
        {
            try {
                System.out.println("\n\n");
                String operatingSystem = System.getProperty("os.name"); //Check the current operating system

                if (operatingSystem.contains("Windows")) {
                    new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
                } else {
                    new ProcessBuilder("clear").inheritIO().start().waitFor();
                }
            } catch (IOException | InterruptedException ignored) {}
            System.out.flush();
            if (game.orElse(null).isExpertMode()) {
                System.out.println("\nCharacter cards: ");
                for (CharacterCard card : game.orElse(null).getCharacterCardList())
                {
                    System.out.println();
                    card.ccPrinter();
                }
                System.out.println("\nTotal available coins: " + game.orElse(null).getUnusedCoins());
            }
            System.out.println("Clouds: ");
            int i = 1;
            for (Cloud cloud : game.orElse(null).getCloudList()) {
                System.out.print("cloud " + i + "   ");
                i++;
                cloud.cloudPrinter();
            }
            i = 1;
            System.out.println("\nIslands: ");
            for (Island island : game.orElse(null).getIslandList()) {
                System.out.print("Island " + i + "   ");
                i++;
                island.islandPrinter(game.orElse(null).isExpertMode());
            }
            System.out.println("\nPlayers: ");
            for (Player player : game.orElse(null).getPlayerList())
            {
                if(player.getuID().equals(nickName))
                    System.out.print("You are ");
                player.playerPrinter(game.orElse(null).isExpertMode());
            }
            if(gameEnded)
            {
                System.out.println();
                printWinner();
                System.out.println("\n\nGame finished!");
            }
            else
            {
                String currPlayer= game.orElse(null).getCurrPlayerNickname();
                System.out.println("It's the turn of: "+currPlayer);
                if(currPlayer.equals(nickName))
                {
                    String[] phase= game.orElse(null).getPhase().toString().split("_");
                    System.out.println("\nYou have to ");
                    for(String s: phase)
                    {
                        System.out.print(s+" ");
                    }
                    System.out.println();
                }
            }
        }
    }
    private boolean checkCharacterCardPresence(int ID)
    {
        if(!game.orElse(null).isExpertMode())
            return false;
        for(CharacterCard card: game.orElse(null).getCharacterCardList())
            if(card.getCardID()==ID)
                return true;
        return false;
    }
    private void printWinner()
    {
        int maxScore=0;
        List<String> winners= new ArrayList<>();
        ModelMessage model= game.orElse(null);
        if(model==null)
            return;

        // calculating the number of towers with which a player start the game
        int mode= model.getPlayerList().size()%2;  // mode==0 => 8 towers
                                                   // mode==1 => 6 towers
        final int maxTowers= (mode)*6 + (1-mode)*8;
        for(Player player: model.getPlayerList())
        {
            int score= 0;
            for(Colour c: Colour.values())
                if(player.checkTeacherPresence(c))
                    score++;
            score+= (maxTowers - player.getTowers().availabilityChecker())*10;
            if(score==maxScore)
                winners.add(player.getuID());
            else if(score>maxScore)
            {
                maxScore=score;
                winners.clear();
                winners.add(player.getuID());
            }
        }
        if(winners.size()>1)
        {
            System.out.print("The winners are: ");
            for(String winner: winners)
                System.out.print(winner+" ");
            System.out.println();
        }
        else
            System.out.println("The winner is: "+winners.get(0));
    }
}
