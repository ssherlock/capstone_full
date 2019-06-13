FROM java:8-jre
WORKDIR /usr/lib
ADD ./target/zuulservice-0.0.1-SNAPSHOT.jar /usr/lib/zuulservice-0.0.1-SNAPSHOT.jar
ADD ./script/wait-for-it.sh .
RUN chmod +x wait-for-it.sh
ENTRYPOINT ["./wait-for-it.sh","musicuserservice:8083","musicauthservice:8084","orchestration:8082","--strict","--","java","-jar","zuulservice-0.0.1-SNAPSHOT.jar"]
