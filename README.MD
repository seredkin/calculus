### Get sources & Build with Maven
``` git clone https://github.com/seredkin/calculus.git ```

``` cd calculus ```

``` mvn package ```

### Run from Maven

``` mvn spring-boot:run ```

### Run from command line

``` java -jar  ./target/calculus-0.0.1-SNAPSHOT.jar```

### Check manually

Default scaling==14, should be JavaScript-compatible

http://localhost:8080/calculus?query=MiAqICgyMy8oMyozKSktIDIzICogKDIqMyk=

Scaling to 2 decimals
 
http://localhost:8080/calculus?query=MiAqICgyMy8oMyozKSktIDIzICogKDIqMyk=&scale=2