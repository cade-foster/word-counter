# Word Counter README #
This is a Java Spring Boot REST service with a single endpoint that accepts a json message with two fields: "id" and
"message" (example: { "id": "123", "message": "hello world" }) and returns a json document with a single field "count"
that contains the total number of words contained in all the messages received to that point, ignoring messages with
duplicate ids (i.e. ids that have already been processed).

For example, if the first message contains 3 words it will respond with count = 3. If the next message contains 5 words
it will respond with count = 8. 

## Running ##
This project uses Maven. To run the project, clone this repo and execute the following command from the project folder:
```
mvn install
mvn clean spring-boot:run
```

#### Tip: ####
To easily install maven you can use [Homebrew](https://formulae.brew.sh/formula/maven):
```
brew install maven
```
To easily [install Homebrew](https://brew.sh/) on macOS or Linux, paste the following in a terminal or shell prompt:
```
$ /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
```

## Curling ##
Once the project is running you can curl the single available endpoint, `/count`, from a terminal with the following:
```
curl -X POST localhost:8080/count -H 'Content-type:application/json' -d '[YOUR_JSON_HERE]'
```
replacing [YOUR_JSON_HERE] with any valid Json text representing a message, such as:
```json
{ "id": "123", "message": "hello world" }
{ "id": "awesomesauce", "message": "Cade Foster codes faster than fast coders." }
{ "id": "Genesis 1:1", "message": "In the beginning, God created the heavens and the earth." }
{ "id": "Lifeway", "message": "Faithfully serving the church since 1891" }
```
