# Resource Service

> resource-service

The resource service holds all the resources of companyx.
These are for example the master data of users like name, address and contact information or
university specific information.
The resource server handles also the update and creation of new resources.

## Background information

#### OAuth2 resource server implementation

This resource service implementation is in its core an oauth 2 compliant resource server
and requires oauth 2 compliant access tokens for each resource request.

This resource server implementation covers only a small set of all possible resources from companyx,
in order to show the key functionality of a resource server for the bachelor thesis.

#### Blacklisted and expired access tokens

The authorization server is capable of blacklisting access tokens.
This indicates to the resource server, that these tokens should not be trusted anymore, f.e. because the access token
got compromised
by an attacker.

The blacklist is managed by a redis server, which is an in memory key-value database and allows
quicker access to the contained information than a relational database.

The resource server validates besides the oauth 2 standard specific access token validation
that the token is not on the blacklist and throws corresponding errors, if the tokens are blacklisted.

#### Cross Site Request Forgery (CSRF) protection

The protection against CSRF is not necessary, because the resource server does not contain
any state information (no authenticated user).
A attacker cannot compromise the session of an authenticated user in order to execute
malicious code in the name of an victim.

#### Database for the persistence of resources

Companyx uses in production a relational database based upon MariaDB.
Because of compatibility issues and dependency mismatches, only the version 10.6 of MariaDB is
currently supported, which is why the same requirement also applies for this implementation.

## Prerequisites

* Java 17 or higher
* Maven
* Redis (blacklist)
* MariaDB 10.6 (resources)
* CompanyX authorization server
* CompanyX resource server

## Java 17

We are using Spring Boot 3.0 as the underlying framework, which requires at least Java 17.

## Maven

Maven is used as the main dependency manager for this project.
It is recommended to also use maven.

## Redis

Redis is a key-value based in memory database, which provides faster access times than relational databases.
Redis is used as the blacklist repository for access tokens.

Make sure that you have an installed redis server that is configured for localhost:6379

## MariaDB / Resource database configuration

Before the service can be run, make sure install MariaDB 10.6 and create a
user and database, where the database schema can be automatically created by this service.
Open a terminal and run the following commands (after MariaDB is installed):

1. `mysql -u root` or open your mariadb console (root is important)
2. Create user:
    3. `CREATE USER 'companyx'@localhost IDENTIFIED BY 'companyx'`
4. Create database:
    2. `CREATE DATABASE companyx_backend;`
3. Grant database acces to the newly created user:
    3. `GRANT ALL ON companyx_backend.* TO 'companyx'@'%' IDENTIFIED BY 'companyx';`
4. `quit`

## Installing / Getting started

1. Checkout this repository to a new directory.
2. Open the repository directory in IntelliJ or any other IDE.
3. Reload the maven project.
4. Be sure that java 17 is selected as the SDK and the java language is at least 17.
5. `mvn clean compile` (either via terminal or using the sidebar Maven goals in IntelliJ)
6. Let IntelliJ build the project.

Alternative with the jar-File:
1. Navigate to the jar-File
2. Run the jar-File with the following command line code (or any other preferable way)
   * `java -jar resource-server-1.0.0.jar`

## Developing

In order to start the resource service and test new features or new bugs, the following steps are necessary:

* Make sure to import the necessary database test data in the resource server.
    * Start the server with the dev profile in order to automatically import the test data.
    * Running it from the jar file requires the following command
        * `java -jar resource-server-1.0.0.jar --spring.profiles.active=dev`
* Start the Redis-Server (necessary for the blacklist).
* Start the authentication server
* Start the resource server

## Links

The following projects are part of this implementation:

* [Spring Boot 3.0.2](https://spring.io/projects/spring-boot)
* [Spring Security 6.0.1](https://spring.io/projects/spring-security)
* [Lombok 1.18.24](https://projectlombok.org)
* [Spotbugs 4.7.3](https://spotbugs.github.io)
* [MariaDB Java Client 3.1.1](https://mariadb.com/kb/en/about-mariadb-connector-j/)
* [Redis 7.0](https://redis.io)
* [JOOQ 3.17.7](https://www.jooq.org)
* [Flyway 9.12.0](https://flywaydb.org)
* [MariaDB 10.6](https://mariadb.com)