# My Restaurant

### Run Manually

##### Prerequisites
1. Install postgresql server
2. Enable java annotation processing for your IDE (IntelliJ recommended)
3. create myopenrestaurant schema

##### Steps
1. Update values of database user and password in application-dev.yml
2. Run MyRestaurantApp.java file
3. Liquibase will create tables, add data to tables from scripts/data.sql
4. Application will run at localhost:9000

### Run with docker
1. Create .env file and add following variables and assign values to them
   1. POSTGRES_ROOT_PASSWORD
   2. POSTGRES_DATABASE
   3. POSTGRES_USER 
2. Run ```docker compose up```
3. Application will run at localhost:9001


