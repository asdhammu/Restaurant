FROM maven:3-amazoncorretto-17

WORKDIR /my-restaurant-app
COPY . .
RUN mvn clean install

CMD mvn spring-boot:run
