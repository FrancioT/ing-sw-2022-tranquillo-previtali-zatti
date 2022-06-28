# Progetto Ingegneria del Software 2021-2022

![alt text](https://github.com/ZattiAndrea/ing-sw-2022-tranquillo-previtali-zatti/blob/master/src/resources/SfondoMenuIniziale.jpg)

"Eriantys" Board game implementation.\
The project concerns the digitization and online gaming of "Eriantys".\
The main architecture consists of a "SERVER" application that hosts several games each with multiple "CLIENTS".\
The approach used to create the app is MVC (ModelViewControl) in which the data, the view and the logic are conceptually separated. 

From a gameplay point of view the users can have either a CLI (Client terminal) or a GUI (Client graphical interface).

## Initial requirements

We also want to leave the official requirements given to us at the begin of the project

* [Requirements](https://github.com/ZattiAndrea/ing-sw-2022-tranquillo-previtali-zatti/blob/master/deliverables/Requirements/requirements.pdf)

## Authors

* [Francesco Maria Tranquillo](https://github.com/FrancioT)
* [Denis Previtali](https://github.com/Denis-Previtali)
* [Andrea Zatti](https://github.com/ZattiAndrea)

## Documentazione

### UML

* [Detailed UML](https://github.com/ZattiAndrea/ing-sw-2022-tranquillo-previtali-zatti/tree/master/deliverables/UML/DetailedUML)
* [HighLevel UML](https://github.com/ZattiAndrea/ing-sw-2022-tranquillo-previtali-zatti/tree/master/deliverables/UML/HighLevelUML)
* [Sequence diagrams](https://github.com/ZattiAndrea/ing-sw-2022-tranquillo-previtali-zatti/tree/master/deliverables/Sequence%20Diagrams)

### Librerie e Plugins

| Libreria/Plugin  | Descrizione |
| -------------    | ------------- |
| Maven   | Tool to organize, simplify and automatize all JAVA app lifeCyle operations   |
| JavaFX-FXML    | JavaFX plugin to implement fxml files for graphics layouts                         |
| JavaFX-Graphics  | Graphic interface library                                                                    | 

## Functionalities

### Base:
* GUI
* CLI
* Simplified rules 
* Socket
### Advanced Functionalities
* MultiMatch
* All 12 character cards 
* Multiplayer till 4 players game 

## JAR 
* .[Download Server]()
* [Download Windows GUI](https://github.com/ZattiAndrea/ing-sw-2022-tranquillo-previtali-zatti/blob/master/deliverables/Jars/GUI_Windows.jar)
* [Download CLI]()

## Server
### Excecution:
To execute Server please double click the jar file or run the following command
```
java -jar ServerGC37.jar 
```

## GUI
### Excecution:
To execute GUI please double click the jar file or run the following command
```
java -jar GUIGC37.jar 
```

## CLI
### Excecution:
To execute CLI please double click the jar file or run the following command
```
java -jar CLIGC37.jar 
```

## Coverage
### Model
| Class coverage  | Methods coverage | Lines coverage |
| -------------    | ------------- | ------------- | 
| 100% | 99% | 96% |

### Controller
| Class coverage  | Methods coverage | Lines coverage |
| -------------    | ------------- | ------------- | 
| 100% | 100% | 91% |

### Copyright
The CODE is opensource, Graphical resources (Images, GameBoard, Cards...) instead are under copyright of  [Cranio Creations](http://www.craniocreations.it)
