# GAMEOFTHREE

## API

* Root URI: http://localhost:8080/81/

* /api/player/details/mode/{mode} 
    * Method: POST
    * mode: automatic or manual. By default automatic.
    * Description: API to change the game mode. Set manual to play manual games. Call the pending games api to fetch pending 
                    games from other players.

* /api/player/details/pending
    * Method: GET
    * Description: If in manual mode, get all the pending games to play. Returns a list of pending games object. 
                    Contains the number played by opponent.

* /api/game/play
    * Method: POST
    * Request Payload: {
                       		"toPlayer":"<<player-id of opponent>>",
                       		"fromPlayer":"<<your player-id>>",
                       	    "gameId":"<<UUID received from pending games>>",
                       	    "number":"<<number to play>>"
                       	}
    * Description: If manual mode, play turns for pending games. All the properties are needed.

* /api/game/new
    * Method: POST
    * Request Payload: {"toPlayer":""}
    * Description: Start a new game with player. Provide the playerId.

* /api/player/details/games
    * Method: GET
    * Description: Check all the games played and the results.
    
## Installation

### Prerequisites
* Java 11
* Docker
* Docker compose
* [Mosquitto MQTT broker](https://mosquitto.org/download/) (optional)

Perform the following steps:

```bash
git clone https://github.com/shibasish/GameOfThree.git
docker network create gameofthree
docker run -it -d -p 1883:1883 --net gameofthree --name mosquitto eclipse-mosquitto
cd GameofThree
// windows build command
mvnw.cmd package spring-boot:repackage -Dmaven.test.skip=true
// Unix based build command
./mvnw package spring-boot:repackage -Dmaven.test.skip=true
```

## Usage

```bash
docker-compose -f docker-compose-p1.yml -p player1 up -d
docker-compose -f docker-compose-p2.yml -p player2 up -d
```

Initialize player1 and player2 in database. 

## Check Database
To check game details for eg. winner/looser, bash into the db containers. Following are the instructions:

```bash
docker exec -it Player2-db/Player1-db bash
mysql -u root -p <<root password defined within the compose files>>
use takeaway_db
```

### SQL
```sql
select * from takeaway_db.game;
select * from takeaway_db.player;
select * from takeaway_db.player_games;
```

## MQTT Broker

```bash
// subscribe to a topic
mosquitto_sub -h localhost -p 1883 -t from/+/to/player2 -i mqtt-subscriber --debug

// publish to a topic
mosquitto_pub -h localhost -p 1883 -t from/player2/to/player1 --debug -m "{\"gameId\":\"<value>\",\"toPlayer\":\"<value>\",\"fromPlayer\":\"<value>\",\"number\":\"<value>\"}"
```

## Testing


```bash
cd GameofThree
mvn package spring-boot:repackage
```
Ensure database container and message container are running. Port 8080/81 should be inactive. Please comment/uncomment required properties
in application.properties file for player 1/2. 