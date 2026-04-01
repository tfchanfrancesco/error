# 多阶段：OpenShift Import from Git / 本机均可 docker build
# 使用镜像内 apt 安装的 Maven，避免 mvnw 在 Windows 上 CRLF 导致 Linux 构建失败，
# 也减少 only-script wrapper 在构建阶段再下载 Maven 分发包失败（出网/限流）的概率。
#
# 若只需复制已有 JAR：docker build -f Dockerfile.prebuilt -t error-app:local .

FROM eclipse-temurin:25-jdk AS builder

RUN apt-get update \
	&& DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends maven \
	&& rm -rf /var/lib/apt/lists/*

WORKDIR /build
COPY pom.xml .
COPY src ./src

# -Dhttps.protocols=TLSv1.2 在部分老旧代理环境下更稳；可按需删掉
ENV MAVEN_OPTS="-Dhttps.protocols=TLSv1.2"
RUN mvn -B -DskipTests package -Dnetworktimeout=100000

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=builder /build/target/error-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
