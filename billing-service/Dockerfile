FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package

# maven is builder after we build we dont need it, we just need a runner

FROM eclipse-temurin:21-jdk AS runner

WORKDIR /app

# pom.xml üzerindeki version ile aynı olmalı 0.1.1 değil de 0.0.1, ama direkt bulması için * otomatik billing algılar inşallah
COPY --from=builder /app/target/*.jar ./app.jar

EXPOSE 4001
EXPOSE 9001

ENTRYPOINT ["java", "-jar", "app.jar"]