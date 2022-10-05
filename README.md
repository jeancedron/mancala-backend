## Mancala Game

This is an implementation of the Mancala game for a challenge in some-random-company.com. In this game, there are 
two players with 6 small and 1 big pit each one. Each of the small pits are filled with six stones. 
Every turn, each player choose one of their small pits to sow its stones into the next sow sequentially.
The game finishes when a player doesn't have any move available. The winner is the player with the most quantity of 
stones into its big pit.

### Built With

* [Java 1.8](https://www.oracle.com/java/technologies/javase-downloads.html)
* [Spring boot](https://spring.io/projects/spring-boot)
* [Project Lombok](https://projectlombok.org/)
* [MongoDB](https://www.mongodb.com/)

## Getting Started

To get a local copy up and running follow these simple steps.

### Installation

1. Clone the repo
   ```sh
   git clone https://gitlab.com/bolcom/jean-pierre-cedron.git
   ```
2. Start with Maven spring-boot plugin
   ```sh
   ./mvnw spring-boot:run
   ```

## Architecture

This project has been developed using an **Hexagonal Architecture** with ports and adapters to inverse the dependencies
around the domain. This architecture avoids having [anemic domain models](https://martinfowler.com/bliki/AnemicDomainModel.html) 
and allows scalability as the core business logic doesn't care anything about the infrastructure used. We can easily change 
framework, database, REST API, etc. without touching the domain.

_For more reference, please refer to this [Netflix blog post](https://netflixtechblog.com/ready-for-changes-with-hexagonal-architecture-b315ec967749)_

## Usage

This application exposes 4 endpoints to create and play a Mancala game. You can: create game, get a game, list movements 
available and execute a movement for a player.

### Create game

To create a game, the API requests for two players to begin. The response is a `Game` entity.

`POST /games`

#### Request

```json
{
    "player_a": "Jean",
    "player_b": "some-random-company.com"
}
```
#### Response
```json
{
    "id": "613e9feed346aa32900cf81c",
    "players": [
        {
            "name": "Jean",
            "position": 1
        },
        {
            "name": "some-random-company.com",
            "position": 2
        }
    ],
    "state": "STARTED",
    "pits": [
        7, 7, 7, 7, 7, 0, 1, 7, 7, 7, 7, 7, 0, 1
    ],
    "next_player": {
        "name": "Jean",
        "position": 1
    },
   "winner": null
}
```

### Get a game

To get a game, the API requests the id of the game as path variable. The response is a `Game` entity.

`POST /games/:id`

#### Response
```json
{
    "id": "613e9feed346aa32900cf81c",
    "players": [
        {
            "name": "Jean",
            "position": 1
        },
        {
            "name": "some-random-company.com",
            "position": 2
        }
    ],
    "state": "STARTED",
    "pits": [
        7, 7, 7, 7, 7, 0, 1, 7, 7, 7, 7, 7, 0, 1
    ],
    "next_player": {
        "name": "Jean",
        "position": 1
    },
   "winner": null
}
```

### List available moves

This endpoint returns the available moves for the current player. The response is a list of `Move` entity.

`GET /games/:id/moves`

#### Response
```json
{
   "moves": [
      {
         "pit": 7,
         "stones_available": 7
      },
      {
         "pit": 8,
         "stones_available": 7
      },
      {
         "pit": 9,
         "stones_available": 7
      },
      {
         "pit": 10,
         "stones_available": 7
      },
      {
         "pit": 11,
         "stones_available": 7
      },
      {
         "pit": 12,
         "stones_available": 6
      }
   ],
   "player": {
      "name": "some-random-company.com",
      "position": 2
   }
}
```

### Execute movement

Executing a movement only requires the pit index to begin sowing the stones into the next pits. The response is a
`MoveResult` entity.

`POST /games/:id/moves`

#### Request

```json
{
   "pit": 12
}
```
#### Response
```json
{
   "winner": null,
   "play_again": false,
   "next_player": {
      "name": "some-random-company.com",
      "position": 2
   }
}
```

## Contact
Jean Cedron - jean.cedrons@gmail.com