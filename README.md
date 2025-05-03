# Galactic Empire 3000

An investigation into remaking the [classic bbs game] from the late 80s, early 90s

## Project Structure

This is a Maven-based Java project with the following structure:

```
src/main/java/com/jedcn/ge3000/      # Main source code
src/test/java/com/jedcn/ge3000/      # Test code
```

## Requirements

- Java 17+
- Maven 3.8+

## Building the Project

```bash
mvn clean install
```

## Running the Application

```bash
mvn exec:java
```

Or after packaging:

```bash
java -jar target/galactic-empire-3000-1.0-SNAPSHOT.jar
```

## Running Tests

```bash
mvn test
```

[classic bbs game]: https://wiki.mbbsemu.com/doku.php?id=modules:mbmgemp