# GAMEOFTHREE

## API

### /api/player/details/mode/{mode} ----> POST
Two game modes: automatic and manual. By default automatic.

### /api/game/new -----> POST
Start a new game. Accepts an object of type: {"toPlayer":"<<player-id of opponent>>"}

### /api/player/details/pending ----> GET
If in manual mode, get all the pending games to play. Returns a list of pending games object. Contains the number played by opponent.

### /api/game/play -----> POST
If manual mode, play turns for pending games. Accepts an object of type 
	{
		"toPlayer":"<<player-id of opponent>>",
		"fromPlayer":"<<your player-id>>",
	    "gameId":"<<UUID received from pending games>>",
	    "number":"<<number to play>>"
	}

All the properties are needed.

## Installation

Perform the following steps:

```bash
docker run -it -p 1883:1883 --net foo --name mosquitto eclipse-mosquitto
```

## Usage

```python
import foobar

foobar.pluralize('word') # returns 'words'
foobar.pluralize('goose') # returns 'geese'
foobar.singularize('phenomena') # returns 'phenomenon'
```