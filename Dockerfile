# 多阶段：OpenShift Import from Git / 本机 docker build
# 不用 apt 安装 maven：避免 Ubuntu 的 maven 依赖顺带装上旧版 JDK，导致与 Temurin 25 混用编译失败。
#
# 若只需复制已有 JAR：docker build -f Dockerfile.prebuilt -t error-app:local .

FROM eclipse-temurin:25-jdk AS builder

ARG MAVEN_VERSION=3.9.14

RUN apt-get update \
	&& DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends ca-certificates curl \
	&& rm -rf /var/lib/apt/lists/* \
	&& curl -fsSL "https://archive.apache.org/dist/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz" \
		| tar xz -C /opt \
	&& ln -sf "/opt/apache-maven-${MAVEN_VERSION}/bin/mvn" /usr/local/bin/mvn

WORKDIR /build
COPY pom.xml .
COPY src ./src

ENV MAVEN_OPTS="-Dhttps.protocols=TLSv1.2"

# maven.test.skip=true：不编译、不运行测试（OpenShift 构建里常不需要跑 SpringBootTest）
RUN java -version \
	&& mvn -version \
	&& mvn -B -Dmaven.test.skip=true package -Dnetworktimeout=100000

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=builder /build/target/error-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
