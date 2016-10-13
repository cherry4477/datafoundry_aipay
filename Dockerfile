FROM asiainfoldpone/baseimage-java

ENV TIME_ZONE=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TIME_ZONE /etc/localtime && echo $TIME_ZONE > /etc/timezone

ADD . /app

WORKDIR /app

RUN mvn install  -D maven.test.skip=true

RUN mv ./target/datahub_bill-1.0.jar .
RUN mv ./target/lib .

EXPOSE 8080


CMD ["java","-jar","datahub_bill-1.0.jar"]

