# Prueba Técnica German Lizondo para Clarivate

Versión Java: 11 <br>
Versión Gradle: 7.4

Compile command: gradle clean build<br>
Execute command: java -jar build/libs/pruebatecnica-0.0.1-SNAPSHOT.jar

<h3>H2 DATABASE </h3>
<hr>
Se usa la base de datos H2, la cual es una bbdd in memory, 
además hay un archivo llamado data.sql que inserta los datos en la base de datos<br>
Para poder comprobar los datos mediante el gestor incorporado en h2:<br>

url: http://localhost:8080/h2-console <br>
driver class: org.h2.Driver<br>
jdbc url: jdbc:h2:mem:testdb <br>
user name: user<br>
password: user<br>

<h3>Endpoints </h3>
<hr>
Login curl: 

```curl
curl --location --request POST 'localhost:8080/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username": "German",
    "password": "12345"
}'
```
Response: 
```text
eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiZXhwIjoxNjQ0ODc3MjAwfQ.T2RP9Y0muvYGqYjZjqLsDTQrsRDAediLBYk-2b7jcyM
```

Ahora se agarra el token devuelto por el login  y se añade en el header Session-Key

AddScore:

```curl
curl --location --request PUT 'localhost:8080/level/3/score/17' \
--header 'Session-Key: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiZXhwIjoxNjQ0ODc3MjAwfQ.T2RP9Y0muvYGqYjZjqLsDTQrsRDAediLBYk-2b7jcyM'
```

Get HighestScore:

```curl
curl --location --request GET 'localhost:8080/level/3/score?filter=17' \
--header 'Session-Key: eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyIiwiZXhwIjoxNjQ0ODc3MjAwfQ.T2RP9Y0muvYGqYjZjqLsDTQrsRDAediLBYk-2b7jcyM'
```
Response:
```json
[
    {
        "user_id": 2,
        "score": 17
    },
    {
        "user_id": 2,
        "score": 17
    }
]
```