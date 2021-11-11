FROM alpine:latest AS builder

RUN apk update && apk add --no-cache openjdk11 git maven

RUN git clone https://github.com/HEIGVD-Course-API/MockMock.git

WORKDIR MockMock

RUN git checkout 69698c1b9241e27e8f52ea3e3624c31cb5ebbbba

RUN mvn clean package

FROM alpine:latest as app

RUN apk update && apk add --no-cache openjdk11-jre-headless

WORKDIR /home/MockMock

COPY --from=builder /MockMock/target/MockMock-1.4.0.one-jar.jar ./MockMock.jar

EXPOSE 25/tcp
EXPOSE 8282/tcp

CMD java -jar MockMock.jar