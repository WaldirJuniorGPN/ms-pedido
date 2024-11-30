FROM maven:3.9.8-amazoncorretto-21 AS build
COPY /src /ms-pedido/src
COPY /pom.xml /ms-pedido
WORKDIR /ms-pedido
RUN mvn clean package -DskipTests

FROM amazoncorretto:21
ARG JAR_FILE=target/*.jar
COPY --from=build /ms-pedido/${JAR_FILE} /ms-pedido/api-pedido.jar
EXPOSE 8082
ENTRYPOINT ["java","-jar","/ms-pedido/api-pedido.jar"]