FROM openjdk:11

RUN apt-get update && apt-get install -y git maven

RUN git clone https://github.com/HEIGVD-Course-API/MockMock.git

WORKDIR MockMock

RUN git checkout 69698c1b9241e27e8f52ea3e3624c31cb5ebbbba

RUN mvn clean install

EXPOSE 25/tcp
EXPOSE 8282/tcp

CMD java -jar target/MockMock-1.4.0.jar