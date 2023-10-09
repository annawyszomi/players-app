# Players
simple CRUD application

Endpoints descriptions for player
| HTTP Method | URI |Description |
| --- | --- | --- |
| GET | `/players` | Get all players |
| GET | `/players/{id}` | Get player by id |
| POST | `/players` | Add player (fields:username, firstName, lastName, email, accountBalance) |
| PUT | `/players/{id}` | Update player |
| DELETE | `/players/{id}` | Delete player |


## Postman
Postman collection available for testing: https://api.postman.com/collections/20630157-730cf4ad-8e0a-4b2a-a83b-bb5d707804dd?access_key=PMAT-01HBZW8Z2T58CB8VEPPTP3SM5Y

## Technologies
* Java 17
* Spring Boot 
* Spring Data JPA
* H2 DB
* Lombok
* Maven




