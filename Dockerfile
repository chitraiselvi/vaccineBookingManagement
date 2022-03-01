FROM openjdk:8-jdk-alpine
ADD ./target/vaccine-booking-management.jar vaccine-booking-management.jar
ENTRYPOINT ["java","-jar","vaccine-booking-management.jar"]
