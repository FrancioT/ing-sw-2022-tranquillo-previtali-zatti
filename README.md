# Progetto Ingegneria del Software 2021-2022

Implementazione del gioco da tavolo "Eriantys".\
Il progetto consiste nella realizzazione digitale e multiplayer del gioco.\
L'architettura generale consiste in un applicativo "SERVER" che ospita diverse partite ognuna con multipli "CLIENTS".\
L'approccio utilizzato per realizzare l'app è del tipo MVC (ModelViewControl) in cui appunto i dati, la vista e la logica sono concettualmente separate.

A livello di gameplay invece gli utenti potranno disporre di un client CLI (terminale) oppure di un client GUI (interfaccia grafica).

## Authors

* [Francesco Maria Tranquillo](https://github.com/FrancioT)
* [Denis Previtali](https://github.com/Denis-Previtali)
* [Andrea Zatti](https://github.com/ZattiAndrea)

### Copyright
The CODE is opensource, Graphical resources (Images, GameBoard, Cards...) instead are under copyright of  [Cranio Creations](http://www.craniocreations.it)

## Documentazione

### UML

* [Initial]()
* [Final]()
* [HighLevel Summary]()
* [Network Architecture]()

### Librerie e Plugins

| Libreria/Plugin  | Descrizione |
| -------------    | ------------- |
| Maven   | Tool to organize, simplify and automatize all JAVA app lifeCyle operations   |
| JavaFX-Media   | JavaFX plugin to implement music in game                             |
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
[Download JAR]()
## Server
### Excecution:
To execute Server please duble click the jar file or run the following command
```
java -jar ServerGC37.jar 
```

## GUI
### Excecution:
To execute GUI please duble click the jar file or run the following command
```
java -jar GUIGC37.jar 
```

## CLI
### Excecution:
To execute CLI please duble click the jar file or run the following command
```
java -jar CLIGC37.jar 
```

## Coverage
### Model
| Class coverage  | Methods coverage | Lines coverage |
| -------------    | ------------- | ------------- | 
| 100% | 99% | 96% |

--Class coverage: 
--Methods coverage: 
--Lines coverage: 

### Controller
--Class coverage: 100% 
--Methods coverage: 100%
--Lines coverage: 91%
