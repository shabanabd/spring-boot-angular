## SpringBoot (Java) Backend with Angular 17 Frontent
Application to demonstrate various parts of a service oriented RESTfull application.

### Technology Stack
Component         | Technology
---               | ---
Frontend          | Angular 17
Backend (REST)    | SpringBoot (Java)
Security          | Token Based (Spring Security and JWT )
DB                | MYSQL 
Persistence       | JPA (Using Spring Data)
Client Build Tools| angular-cli, Webpack, npm
Server Build Tools| Maven(Java)

## Prerequisites
Ensure you have this installed before proceeding further
- Java 17
- Maven 3.x.x
- Node 18.18.0,
- npm 8,
- Angular-cli 17.2.1
- MYSQL

## Features of the Project
* Backend
    * Token Based Security (using Spring security)
    * Using JPA and JDBC template to talk to relational database
    * How to request and respond for filtered, sorted and paginated data
    * CRUD Features

* Frontend
    * Organizing Components, Services, Directives, Pages etc in an Angular App
    * How to chain RxJS Observables
    * Load data set in a data-table
    * Routing and guarding pages that needs authentication
    * Basic visualization

## Steps to Setup
**1. Clone the application**

**2. Create Mysql database**
```bash
create database test_db
```
- run `src/main/resources/testDB.sql`

**3. Change mysql username and password as per your installation**
+ open `src/main/resources/application.properties`
+ change `spring.datasource.url` , `spring.datasource.username` and `spring.datasource.password` as per your mysql installation

**4. Build and run the backend app using maven**
```bash
# Maven Build : Navigate to the root folder where pom.xml is present 
mvn clean install
java -jar target/backend-1.0.0.jar
```
Alternatively, you can run the app without packaging it using -
```bash
mvn spring-boot:run
```
The backend server will start at <http://localhost:8080>

**5. Run the frontend app using npm**
```bash
cd frontend
npm install
```

```bash
npm start
```

Frontend server will run on <http://localhost:4200>

#### Database Schema
![ER Diagram](/screenshots/schema.png?raw=true)

