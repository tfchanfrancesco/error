# 多阶段：集群或本机均可直接 `docker build`（无需事先 mvn package）
# 本机: docker build -t error-app:local .
# OpenShift: Import from Git，构建策略选 Dockerfile（或 Docker）
#
# 若只需复制已有 JAR（单阶段、更快），可用: docker build -f Dockerfile.prebuilt -t error-app:local .

FROM eclipse-temurin:25-jdk AS builder
WORKDIR /build
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw
COPY src ./src
RUN ./mvnw -B -DskipTests package

FROM eclipse-temurin:25-jre
WORKDIR /app
# 与 pom.xml 中 <artifactId>-<version>.jar 一致
COPY --from=builder /build/target/error-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
