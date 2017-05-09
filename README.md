#Connoisseur WebService Backend - Account
 
 

## To start locally:

#### Steps:
1. Install [Gradle](https://gradle.org)

2. Install Postgresql,create a database user and an empty database

3. Update **src/resources/application.properties** file accordingly
   > spring.datasource.url= jdbc:postgresql://localhost:5432/connoisseurdb
 spring.datasource.username=connoisseurdev
 spring.datasource.password=connoisseurdev
 
 
4. Update **src/resources/application.properties** from
    >spring.jpa.hibernate.ddl-auto=update

    to

    >spring.jpa.hibernate.ddl-auto=create

    this step will help you create the tables and import the initial data.
    **After the tables created, please revert this change back.**, 


5. To start the application on 8080 port, go to project folder, and type
    >  ./gradlew bootRun

    For more options, run 
    >  ./gradlew tasks

    will list all options.frequent used options includes:
    
    - Static Analyze
      Run following command to perform code static analyze with FindBugs
      >./gradlew check

    - Unit test

      Run following command to execute all Junit test cases 
      >./gradlew test

    - Build the application jar file
 
      >./gradlew build

    


#### API endpoints

+ By default, use localhost:8080 to access. The backend service will start with /ws-account/api/ path

+ Access the swagger-ui.html to discover all potential api endpoints:

    [http://localhost:8080/ws-account/api/swagger-ui.html](http://localhost:8080/ws-account/api/swagger-ui.html)

+ Install [Postman](https://www.getpostman.com) for api debug, or you can use cURL command

And the steps will be:

1. Get authentication token
    - Authentication with cURL:
      > curl -X POST http://localhost:8080/ws-account/api/user/loginsession -d '{"email":"admin@diningconnoisseur.com","passord":"password1"}' -H "content-type:application/json"
    - Authentication with Postman:
    
    the result should looks like 
      > {"id":3,"userId":0,"token": "35c2deca-2ba9-4067-9314-8e093a78400a","expiration":1494323792523,"ttl":3600000,"clientSpec":""}
    
2. Extract the token field, add to **"X-Auth-Token"** header for all following requests.
    - Sample user list request with cURL:
      >curl -X GET http://localhost:8080/ws-account/api/user  -H "content-type:application/json" -H **"X-Auth-Token:35c2deca-2ba9-4067-9314-8e093a78400a"**
      
   All request without proper X-Auth-Token header will get an unauthorized error, except the **loginsession** api
    

## To start with QA Database in AWS

* Run command
    >SPRING_PROFILES_ACTIVE=qa ./gradlew bootRun

    or

    > java -Dspring.application.json='{"spring.profiles.active":"qa"}' -jar connoisseur-account-service-0.0.1-SNAPSHOT.jar

    by specify environment variable, the application-qa.properties will picked up. This file contains the database configuration to the QA database in AWS RDS. more information can be found from [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)



## Intellij IDEAConfiguration:

* Ticking the **"Enable annotation processing"** checkbox in **Settings->Compiler->Annotation Processors**.

* Install the **Lombok** plugin for IDEA and restart for change to take effect.




