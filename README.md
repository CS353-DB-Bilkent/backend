## Internship System Backend

### Run configuration
> Note: You must have docker installed on your system. In case you do not have it, download [here](https://www.docker.com/products/docker-desktop/).

For local backend development, run the following commands
```zsh
$ docker compose -f docker-compose-local.yaml up -d # run the required services
$ ./mvnw spring-boot:run -Dspring-boot.run.profiles=local # run the app with local profile
```
After these commands, services will start on specified addresses.
- Redis: `redis://localhost:6379/`
- MongoDB: `mongodb://localhost:27017/`
- Redis Commander: `http://localhost:8081/`

For frontend development, you just need to compose each service.
Then the backend service will be accessible from `http://localhost:8080/`
```zsh
$ ./mvnw dependency:resolve # install dependencies 
$ ./mvnw package # build the jar file
$ docker compose up -d # run all the services, including this one
```

---

#### Good to have tools
- [Postman](https://www.postman.com/product/rest-client/): For API testing
- [MongoDB Compass](https://www.mongodb.com/products/compass): To access db, and visualize content
- [Docker Desktop](https://www.docker.com/products/docker-desktop/): View the containers
