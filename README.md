# The Techcombank Interview Exercise  #

## Build & Run ##
### Fist way
```sh
$ cd tcb-interview
$ sbt
> jetty:start
> browse
```

### Second way
```sh
$ cd tcb-interview
$ sbt run
```

### Build war file in order to deploy
```sh
$ cd tcb-interview
$ sbt package
```

## Run test ##
```sh
$ cd tcb-interview
$ sbt test
```

## API Urls ##
#### First Api
```sh
$ curl -L -X POST 'http://localhost:8080/pools' -H 'Content-Type: application/json' --data-raw '{
    "poolId": 1,
    "poolValues": [
        1001
    ]
}'
```

#### Second Api
```sh
$ curl -L -X POST 'http://localhost:8080/pools/percentile' -H 'Content-Type: application/json' --data-raw '{
    "poolId": 1,
    "percentile": 100
}'
```

### Postman document (Prefer to use these docs)
[TCB Interview](https://documenter.getpostman.com/view/1276756/Tzm6kFZt).


