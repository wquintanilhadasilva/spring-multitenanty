# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.5.4/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.5.4/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.5.4/reference/htmlsingle/#boot-features-developing-web-applications)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/2.5.4/reference/htmlsingle/#using-boot-devtools)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/2.5.4/reference/htmlsingle/#boot-features-jpa-and-spring-data)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/bookmarks/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

[Aplicações Multitentanty](https://gasparbarancelli.com/post/multi-tenancy-com-hibernate-e-spring-boot)


## Note: Once Multi-Tenancy is enabled all the DDL needs to be executed manually before staring the application

## Compile and package

Being Maven centric, you can compile and package it without tests using:

```
mvn clean package -Dmaven.test.skip=true
```

Once you have your jar file, you can run it.

## Run it

Before, create database and tabeles with commands in `resources/DDL.sql` file

To run it you can go to the Maven target folder generated and execute the following command:

```
java -jar multitenanty-XXX.jar
```

## Testing

Once started you can go and request the data using different tenants :

* `curl -X POST   http://localhost:8080/ -H 'Content-Type: application/json' -H 'schema: test1' -d '{"name":"Mumbai"}'`

* `curl -X POST   http://localhost:8080/ -H 'Content-Type: application/json' -H 'schema: test2' -d '{"name":"Kolkata"}'`

* `curl -X GET   http://localhost:8080/ -H 'Content-Type: application/json' -H 'schema: test1'`

* `curl -X GET   http://localhost:8080/ -H 'Content-Type: application/json' -H 'schema: test2'`