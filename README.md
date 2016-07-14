### Key Features
 - rest api
 - spring boot magic
 - jpa, hibernate
 - h2 database
 - asynchronous transfer

### Usage
 - `mvn clean install`
 - `java -jar target/account-0.1-SNAPSHOT.jar`
 - open view `http://localhost:8080/index.htm`
 - open rest api `http://localhost:8080/`

### Benchmark
 - test transfer performance `ab -n 10000 -c 10 -m POST -l "http://localhost:8080/transfer?from=1&to=2&amount=1"`
 - test transfer with insufficient balance `ab -n 100 -c 10 -m POST -l "http://localhost:8080/transfer?from=1&to=2&amount=10000"`

### TODO
 - transactions pagination