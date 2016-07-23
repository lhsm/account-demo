FROM maven

WORKDIR account-demo

ADD . /account-demo

RUN mvn verify

EXPOSE 8080

CMD mvn spring-boot:run